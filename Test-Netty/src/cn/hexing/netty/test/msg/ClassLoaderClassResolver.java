package cn.hexing.netty.test.msg;

import org.jboss.netty.handler.codec.serialization.ClassResolver;

public class ClassLoaderClassResolver implements ClassResolver{
	 private final ClassLoader classLoader;

	    ClassLoaderClassResolver(ClassLoader classLoader) {
	        this.classLoader = classLoader;
	    }

	    public Class<?> resolve(String className) throws ClassNotFoundException {
	        try {
	            return classLoader.loadClass(className);
	        } catch (ClassNotFoundException e) {
	            return Class.forName(className, false, classLoader);
	        }
	    }
}
