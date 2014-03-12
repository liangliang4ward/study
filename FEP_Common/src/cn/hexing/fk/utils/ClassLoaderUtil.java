/**
 * 小工具类提供了对系统类加载器和扩展类加载器的动态控制能力.
 * 可以在程序中加入classpath,当然也可以获得类加载器加载的类列表.
 */
package cn.hexing.fk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import sun.misc.Launcher;

/**
 *
 */
public class ClassLoaderUtil {
    private static Field classes;

    private static Method addURL;
    static {
        try {
            classes = ClassLoader.class.getDeclaredField("classes");
            addURL = URLClassLoader.class.getDeclaredMethod("addURL",
                    new Class[] { URL.class });
        } catch (Exception e) {
            //won't happen ,but remain it
            throw new RuntimeException(e);
        }
        classes.setAccessible(true);
        addURL.setAccessible(true);
    }

    private static URLClassLoader system = (URLClassLoader) getSystemClassLoader();

    private static URLClassLoader ext = (URLClassLoader) getExtClassLoader();

    public static ClassLoader getSystemClassLoader() {
//    	return Thread.currentThread().getContextClassLoader();
    	return ClassLoader.getSystemClassLoader();
    }

    public static ClassLoader getExtClassLoader() {
        return getSystemClassLoader().getParent();
    }

    /**
     * 获得加载的类
     * 
     * @return
     */
    public static List<Class<?>> getClassesLoadedBySystemClassLoader() {
        return getClassesLoadedByClassLoader(getSystemClassLoader());
    }

    public static List<Class<?>> getClassesLoadedByExtClassLoader() {
        return getClassesLoadedByClassLoader(getExtClassLoader());
    }

    @SuppressWarnings("unchecked")
	public static List<Class<?>> getClassesLoadedByClassLoader(ClassLoader cl) {
        try {
            return (List<Class<?>>) classes.get(cl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static URL[] getBootstrapURLs() {
        return Launcher.getBootstrapClassPath().getURLs();
    }

    public static URL[] getSystemURLs() {
        return system.getURLs();
    }

    public static URL[] getExtURLs() {
        return ext.getURLs();
    }

    private static void list(PrintStream ps, URL[] classPath) {
        for (int i = 0; i < classPath.length; i++) {
            ps.println(classPath[i]);
        }
    }

    public static void listBootstrapClassPath() {
        listBootstrapClassPath(System.out);
    }

    public static void listBootstrapClassPath(PrintStream ps) {
        ps.println("BootstrapClassPath:");
        list(ps, getBootstrapClassPath());
    }

    public static void listSystemClassPath() {
        listSystemClassPath(System.out);
    }

    public static void listSystemClassPath(PrintStream ps) {
        ps.println("SystemClassPath:");
        list(ps, getSystemClassPath());
    }

    public static void listExtClassPath() {
        listExtClassPath(System.out);
    }

    public static void listExtClassPath(PrintStream ps) {
        ps.println("ExtClassPath:");
        list(ps, getExtClassPath());
    }

    public static URL[] getBootstrapClassPath() {
        return getBootstrapURLs();
    }

    public static URL[] getSystemClassPath() {
        return getSystemURLs();
    }

    public static URL[] getExtClassPath() {
        return getExtURLs();
    }

    public static void addURL2SystemClassLoader(URL url) {
        try {
            addURL.invoke(system, new Object[] { url });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addURL2ExtClassLoader(URL url) {
        try {
            addURL.invoke(ext, new Object[] { url });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addClassPath(String path) {
        addClassPath(new File(path));
    }

    public static void addExtClassPath(String path) {
        addExtClassPath(new File(path));
    }

    public static void addClassPath(File dirOrJar) {
        try {
            addURL2SystemClassLoader(dirOrJar.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addExtClassPath(File dirOrJar) {
        try {
            addURL2ExtClassLoader(dirOrJar.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    
	public static void initializeClassPath(){
		String workDir = System.getProperty("user.dir");
		String classRootPath = PathUtil.getRootPath(ClassLoaderUtil.class);
		//找当前路径
		int cnt = 1;
		while( (null != classRootPath)&& (cnt-->0) ){
			try{
				boolean same,upsame;
				File froot = new File(classRootPath);
				File fwork = new File(workDir);
				same = froot.getCanonicalPath().equalsIgnoreCase(fwork.getCanonicalPath());
				if( same )
					break;

				File fup = froot.getParentFile();
				upsame = fup.getCanonicalPath().equalsIgnoreCase(fwork.getCanonicalPath());
				if( upsame )
					break;
				
				//当前应用在plugins目录下
				boolean rootIsPlugins = froot.getName().equalsIgnoreCase("plugins");
				if( rootIsPlugins ){
					System.setProperty("user.dir",fup.getCanonicalPath() );
					workDir = fup.getCanonicalPath();
					break;
				}
				
				//当前jar在lib目录或者libs目录下
				if( froot.getName().equalsIgnoreCase("lib")||
						froot.getName().equalsIgnoreCase("libs")){
					addDir2ClassPath(froot.getAbsolutePath(),true);
					ClassLoaderUtil.addClassPath(fup.getAbsolutePath());
					
					System.setProperty("user.dir",fup.getCanonicalPath() );
					workDir =fup.getCanonicalPath();
					break;
				}
				//parent is lib or libs
				if( fup.getName().equalsIgnoreCase("lib")||
						fup.getName().equalsIgnoreCase("libs")){
					File fupup = fup.getParentFile();
					
					addDir2ClassPath(fup.getAbsolutePath(),true);
					ClassLoaderUtil.addClassPath(fupup.getAbsolutePath());
					
					System.setProperty("user.dir",fupup.getCanonicalPath() );
					workDir = fupup.getCanonicalPath();
					break;
				}
				
				//当前jar父目录是plugins
				boolean upIsPlugins = fup.getName().equalsIgnoreCase("plugins");
				if( upIsPlugins ){
					System.setProperty("user.dir",fup.getParentFile().getCanonicalPath() );
					break;
				}
				
				//
				break;
			}
			catch(Exception exp){
				exp.printStackTrace();
				return;
			}
		}
		
		File file = new File("log");
		if( null != file )
		{
			if( !file.isDirectory())
				file.mkdir();
		}
//		System.out.println("working dir="+workDir);
		addDir2ClassPath(".",false);
		addDir2ClassPath(workDir+File.separator+"classess",false);
		addDir2ClassPath(workDir+File.separator+"lib",true);
		addDir2ClassPath(workDir+File.separator+"libs",true);
		addDir2ClassPath(workDir+File.separator+"config",false);
		addDir2ClassPath(workDir+File.separator+"images",false);
		addDir2ClassPath(workDir+File.separator+"icons",false);
		addDir2ClassPath(workDir+File.separator+"configuration",false);
		addDir2ClassPath(workDir+File.separator+"cfg",false);
		ClassLoaderUtil.listSystemClassPath();
		loadPropertieFile2SystemProperties();
	}
	
	private static void addFile2ClassPath(String filename){
		File file = new File(filename);
		if( !file.isFile())
			return;
		if( filename.toLowerCase().endsWith(".jar")){
			ClassLoaderUtil.addClassPath(file);
		}
		if( filename.toLowerCase().endsWith(".zip")){
			ClassLoaderUtil.addClassPath(file);
		}
	}
	
	private static void addDir2ClassPath(String dir,boolean includeSub){
		File file = new File(dir);
		if( !file.exists() || !file.isDirectory())
			return;
		//当前路进
		if( dir.equalsIgnoreCase("."))
		{
			String classPath = null;
			try{
				classPath = file.getCanonicalPath();
			}
			catch(Exception exp){
				return;
			}
			ClassLoaderUtil.addClassPath(classPath);
			return;
		}
		
		dir = file.getName();
		String classPath = null;
		try{
			classPath = file.getCanonicalPath();
		}
		catch(Exception exp){
			return;
		}
		
		if( dir.equalsIgnoreCase("com")||
				dir.equalsIgnoreCase("org") )
		{
			ClassLoaderUtil.addClassPath(file.getParent());
			return;
		}
		if( dir.equalsIgnoreCase("config")||
				dir.equalsIgnoreCase("configuration")||
				dir.equalsIgnoreCase("classes")||
				dir.equalsIgnoreCase("cfg") )
		{
			ClassLoaderUtil.addClassPath(classPath);
			return;
		}
/*		
		boolean isLib = dir.equalsIgnoreCase("lib")||
			dir.equalsIgnoreCase("libs");
		File parent = file.getParentFile();
		dir = parent.getName();
		boolean parentIsLib = dir.equalsIgnoreCase("lib")||
		dir.equalsIgnoreCase("libs");
*/		
		
		if( !includeSub )
			return;
		
		File[] files = file.listFiles();
		for(int i=0;i<files.length;i++){
			if(files[i].isFile())
				addFile2ClassPath(files[i].getPath());
			else if( files[i].isDirectory()){
//				System.out.println(files[i].getPath());
				addDir2ClassPath(files[i].getPath(),includeSub);
			}
		}
	}
	
	/**
	 * 把当前目录或者config目录或者bin目录下的*.properties 加载到系统属性中.
	 * 所有属性名自动加 "app.",防止出现冲突
	 */
	public static final void loadPropertieFile2SystemProperties(){
		String curPath = System.getProperty("user.dir");
		String propFilePattern = ".*\\.properties";
		final Pattern pattern = Pattern.compile(propFilePattern);
		final FilenameFilter filter = new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return pattern.matcher(name).matches();
			}
		};
		searchPropertiesFile(curPath,filter);
		searchPropertiesFile(curPath+File.separator+"cfg",filter);
		searchPropertiesFile(curPath+File.separator+"config",filter);
		searchPropertiesFile(curPath+File.separator+"configuration",filter);
		searchPropertiesFile(curPath+File.separator+"bin",filter);
		searchPropertiesFile(curPath+File.separator+"classess",filter);
	}
	
	private static final void searchPropertiesFile(String dirPath, FilenameFilter filter){
		try{
			File f = new File(dirPath);
			if( !f.exists() || !f.isDirectory() )
				return;
			
			File[] pfs = f.listFiles(filter);
			for( File pf: pfs ){
				Properties props = new Properties();
				props.load(new FileInputStream(pf));
				Enumeration<?> pnames = props.propertyNames();
				while(pnames.hasMoreElements()){
					String propName = (String)pnames.nextElement();
					String propValue = props.getProperty(propName);
					System.setProperty(propName, propValue);
					System.out.println("add sys propertie:("+propName+","+propValue+")");
				}
			}
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}

}
