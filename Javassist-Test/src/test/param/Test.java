package test.param;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Test {
	public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		ClassPool pool = ClassPool.getDefault();
		
		CtClass ctClass = pool.get("test.param.Screen");
		CtMethod ctm = ctClass.getDeclaredMethod("draw");
		ctm.insertBefore("{System.out.println(\"i=\"+($1)+\",j=\"+$2);}");
		ctClass.writeFile();
		
		Method m = ctClass.toClass().getMethod("draw",Integer.TYPE,Integer.TYPE);
		Screen s = new Screen();
		m.invoke(s, 1,2);
	}
}
