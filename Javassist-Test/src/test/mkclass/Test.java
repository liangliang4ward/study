package test.mkclass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class Test {
	public static void main(String[] args) throws CannotCompileException, SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ClassPool ctPool = ClassPool.getDefault();
		CtClass mk = ctPool.makeClass("MkClass");
		CtMethod cm = CtNewMethod.make("public void test(){System.out.println(\"xx test\");}", mk);
		mk.addMethod(cm);
		Class<?> s = mk.toClass();
		Method m = s.getMethod("test");
		Object o = s.newInstance();
		m.invoke(o);
		
	}
}
