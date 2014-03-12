package _reflect;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class VerboseLoader extends URLClassLoader {

	public VerboseLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public Class loadClass(String name) throws ClassNotFoundException {
		System.out.println("loadClass: " + name);
		return super.loadClass(name);
	}

	protected Class findClass(String name) throws ClassNotFoundException {
		Class clas = super.findClass(name);
		System.out.println("findclass: loaded " + name + " from this loader");
		return clas;
	}
	public static void main(String[] args) throws Exception{
		
		args  = new String[]{"_reflect.Run"};
        if (args.length >= 1) {
                // get paths to be used for loading
                ClassLoader base =
                    ClassLoader.getSystemClassLoader();
                URL[] urls;
                if (base instanceof URLClassLoader) {
                    urls = ((URLClassLoader)base).getURLs();
                } else {
                    urls = new URL[]
                        { new File(".").toURI().toURL() };
                }
                
                // list the paths actually being used
                System.out.println("Loading from paths:");
                for (int i = 0; i < urls.length; i++) {
                    System.out.println(" " + urls[i]);
                }
                
                // load target class using custom class loader
                VerboseLoader loader =
                    new VerboseLoader(urls, base.getParent());
                Class clas = loader.loadClass(args[0]);
                    
                // invoke "main" method of target class
                Class[] ptypes =
                    new Class[] { args.getClass() };
                Method main =clas.getDeclaredMethod("main", ptypes);
                String[] pargs = new String[args.length-1];
                System.arraycopy(args, 1, pargs, 0, pargs.length);
                Thread.currentThread().
                    setContextClassLoader(loader);
                main.invoke(null, new Object[] { pargs });
                
            
        } else {
            System.out.println
                ("Usage: VerboseLoader main-class args...");
        }
    }
}
