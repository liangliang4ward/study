package test.calc;

import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class TestHello {
	
	public static void main(String[] args) throws Exception {
		CtClass ct = ClassPool.getDefault().get("test.calc.Hello");
		CtMethod method = ct.getDeclaredMethod("test");
		modifyMethod(method, ct);

		Method m = ct.toClass().getMethod("test", null);
		Hello h = new Hello();

		m.invoke(h);
		
		Hello h1 = new Hello();
		System.out.println(h1.test());
	}
	
	public static void modifyMethod(CtMethod method, CtClass clazz)
			throws Exception {

		// 从原方法复制产生一个新的方法
		CtMethod newMethod = CtNewMethod.copy(method, clazz, null);

		// 重命名原方法
		String methodName = method.getName();
		String oldName = method.getName() + "$Impl";

		method.setName(oldName);
		StringBuilder body = new StringBuilder();
		body.append("{System.out.println(\"xix\");long start = System.currentTimeMillis();");

		// 如果有返回值，则记录返回值，没有则不记录
		if (method.getReturnType() == CtClass.voidType) {
			body.append(oldName + "($$);");
		} else {
			body.append("Object result = " + oldName + "($$);");
		}
		body.append(" long end = System.currentTimeMillis();"
				+ "System.out.println(\"" + methodName + "\""
				+ "\"time used:\"+" + "(end - start));");

		// 如果有返回值，则添加return 语句
		if (method.getReturnType() == CtClass.voidType) {
			body.append("}");
		} else {
			body.append("return result;}");
		}
		newMethod.setBody(body.toString());
		newMethod.insertBefore("System.out.println(\"gaga\");");
		clazz.addMethod(newMethod);
	}
}
