package test.mkclass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtField.Initializer;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class JavassistLearn1{
	
	
	public static void main(String[] args) throws Exception{
		ClassPool cp=ClassPool.getDefault();
		CtClass ctClass=cp.makeClass("test");
		
		StringBuffer body=null;
		//参数  1：属性类型  2：属性名称  3：所属类CtClass
		CtField ctField=new CtField(cp.get("java.lang.String"), "name", ctClass);
		ctField.setModifiers(Modifier.PRIVATE);
		//设置name属性的get set方法
		ctClass.addMethod(CtNewMethod.setter("setName", ctField));
		ctClass.addMethod(CtNewMethod.getter("getName", ctField));
		ctClass.addField(ctField, Initializer.constant("default"));
		
		//参数  1：参数类型   2：所属类CtClass
		CtConstructor ctConstructor=new CtConstructor(new CtClass[]{}, ctClass);
		body=new StringBuffer();
		body.append("{\n name=\"me\";\n}");
		ctConstructor.setBody(body.toString());
		ctClass.addConstructor(ctConstructor);
		
		//参数：  1：返回类型  2：方法名称  3：传入参数类型  4：所属类CtClass
		CtMethod ctMethod=new CtMethod(CtClass.voidType,"execute",new CtClass[]{},ctClass);
		ctMethod.setModifiers(Modifier.PUBLIC);
		body=new StringBuffer();
		body.append("{\n System.out.println(name);");
		body.append("\n System.out.println(\"execute ok\");");
		body.append("\n return ;");
		body.append("\n}");
		ctMethod.setBody(body.toString());
		ctClass.addMethod(ctMethod);
		Class<?> c=ctClass.toClass();
		Object o=c.newInstance();
		Method setMethod = o.getClass().getMethod("setName", new Class[]{String.class});
		setMethod.invoke(o, "asssdasd");
		Method method=o.getClass().getMethod("execute", new Class[]{});
		//调用字节码生成类的execute方法
		method.invoke(o, new Object[]{});
	}

}

