/**
 * 在java中，经常要定位某些文件的位置，为了能让程序与物理位置无关，就要使用相对路径。
 * 但java中使用相对路径总会遇到一些很麻烦的问题，就是到底相对于哪个参照物的问题。
 * 因为我们平时使用相对路径总是相对当前工作目录而言的，但有时需求并非如此。比如，要在一个开发包中使用相对路径，
 * 却不知道开发包被其他程序调用时的所在路径，而且特别是在web应用中，很难确定某个文件在整个应用中
 * 的相对路径。
 * 
 * 所以使用相对路径最好的办法就是让路径相对的参照物是我的开发包或我的应用本身的东西，最好的
 * 就是用我开发包中的类的class文件。只要知道了某个class文件的绝对路径，就可以以它为参照物，
 * 使用相对路径来定位其他任何文件了。
 * 
 * 为了实现这个想法，我写了这个Path类，这个类提供了两个静态公共方法:
 *     一个用来定位类的class文件的位置，
 *     另一个以某个类为参照物来定位一个相对路径。
 * 使用这两个方法，我们可以完全不必理会应用的当前工作路径，随心所欲的根据自己的位置来寻找任何文件。
 * 比如在编写某个功能性开发包时，就可以完全不用管调用这个开发包的应用的路径情况，而仅仅根据开发包
 * 本身的位置来定位文件，这样很好的实现了封装性，将文件的路径处理完全封闭在了开发包自身之内。
 * 
 */
package cn.hexing.fk.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

//import org.apache.log4j.Logger;


/**
 *
 */
public class PathUtil {
	/**
	 * 获取一个类的class文件所在的绝对路径。 这个类可以是JDK自身的类，也可以是用户自定义的类，或者是第三方开发包里的类。
	 * 只要是在本程序中可以被加载的类，都可以定位到它的class文件的绝对路径。
	 * 
	 * @param cls
	 *            一个对象的Class属性
	 * @return 这个类的class文件位置的绝对路径。 如果没有这个类的定义，则返回null。
	 */
	public static String getPathFromClass(Class<?> cls) throws IOException {
		String path = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			path = file.getCanonicalPath();
		}
		return path;
	}

	/**
	 * 这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常在程序中很难定位某个相对路径，特别是在B/S应用中。
	 * 通过这个方法，我们可以根据我们程序自身的类文件的位置来定位某个相对路径。
	 * 比如：某个txt文件相对于程序的Test类文件的路径是../../resource/test.txt，
	 * 那么使用本方法Path.getFullPathRelateClass("../../resource/test.txt",Test.class)
	 * 得到的结果是txt文件的在系统中的绝对路径。
	 * 
	 * @param relatedPath
	 *            相对路径
	 * @param cls
	 *            用来定位的类
	 * @return 相对路径所对应的绝对路径
	 * @throws IOException
	 *             因为本方法将查询文件系统，所以可能抛出IO异常
	 */
	public static String getFullPathRelateClass(String relatedPath, Class<?> cls)
			throws IOException {
		String path = null;
		if (relatedPath == null) {
			throw new NullPointerException();
		}
		String clsPath = getPathFromClass(cls);
		File clsFile = new File(clsPath);
		String tempPath = clsFile.getParent() + File.separator + relatedPath;
		File file = new File(tempPath);
		path = file.getCanonicalPath();
		return path;
	}

	/**
	 * 获取类的class文件位置的URL。这个方法是本类最基础的方法，供其它方法调用。
	 */
	private static URL getClassLocationURL(final Class<?> cls) {
		if (cls == null)
			throw new IllegalArgumentException("null input: cls");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(
				".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		// java.lang.Class contract does not specify
		// if 'pd' can ever be null;
		// it is not the case for Sun's implementations,
		// but guard against null
		// just in case:
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			// 'cs' can be null depending on
			// the classloader behavior:
			if (cs != null)
				result = cs.getLocation();

			if (result != null) {
				// Convert a code source location into
				// a full class file location
				// for some common cases:
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar")
								|| result.toExternalForm().endsWith(".zip"))
							result = new URL("jar:".concat(
									result.toExternalForm()).concat("!/")
									.concat(clsAsResource));
						else if (new File(result.getFile()).isDirectory())
							result = new URL(result, clsAsResource);
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}

		if (result == null) {
			// Try to find 'cls' definition as a resource;
			// this is not
			// document．d to be legal, but Sun's
			// implementations seem to //allow this:
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource)
					: ClassLoader.getSystemResource(clsAsResource);
		}
		return result;
	}
	
	public static String getRootPath(Class<?> cls){
		try{
			if( null == cls )
				cls = PathUtil.class;
			String classPath = getPathFromClass(cls);
			if( null == classPath )
				return null;
			String lowClassPath = classPath.toLowerCase();
			if( lowClassPath.endsWith(".jar")||
					lowClassPath.endsWith(".zip")){
				File file = new File(classPath);
				return file.getParent();
			}
			else{
				String className = cls.getName().replace('.',File.separatorChar);
				int index = classPath.lastIndexOf(className);
				if( index<0 )
					return null;
				return classPath.substring(0,index);
			}
		}
		catch( Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getConfigFilePath(String filename){
		try{
			//检测当前工作目录
			File file = new File(filename);
			if( file.exists() )
				return file.getCanonicalPath();
			
			//检测当前工作目录下子目录
			String curPath = System.getProperty("user.dir")+File.separator;
			file = new File(curPath+"config"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();
			file = new File(curPath+"configuration"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();
			file = new File(curPath+"cfg"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();
			
			//相对路进
			String rootPath = getRootPath(null);
			if( null == rootPath )
				return null;
			if( rootPath.charAt(rootPath.length()-1) != File.separatorChar)
				rootPath += File.separator;
			String path = rootPath + filename;
			file = new File(path);
			if( file.exists() )
				return file.getCanonicalPath();
			//配置文件不在class文件根目录下。需要检测config、cfg、configuration目录
			file = new File(rootPath+"config"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();
			file = new File(rootPath+"configuration"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();
			file = new File(rootPath+"cfg"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();

			//检测当前类根目录的上一级目录
			file = new File(rootPath);
			rootPath = file.getParent()+File.separator;
			file = new File(rootPath+filename);
			if( file.exists() )
				return file.getCanonicalPath();
			
			//检测当前类根目录的上一级目录的下级子目录（config cfg configuration)
			file = new File(rootPath+"config"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();
			file = new File(rootPath+"configuration"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();
			file = new File(rootPath+"cfg"+File.separator+filename);
			if( file.exists() )
				return file.getCanonicalPath();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			System.out.println("path from class 'PathUtil.class'="+getPathFromClass(PathUtil.class));
			System.out.println("PathUtil's class root path="+getRootPath(PathUtil.class));
//			System.out.println("Logger's class root path="+getRootPath(Logger.class));
			System.out.println(getConfigFilePath("fas.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
