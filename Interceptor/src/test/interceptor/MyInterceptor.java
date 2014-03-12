package test.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInterceptor implements InvocationHandler{

	private Object target;
	
	public MyInterceptor(Object target){
		this.target = target;
	}
	
	public void setModel(Object target){
		this.target = target;
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		return method.invoke(this.target, args);
	}

	public static void main(String[] args) {
		IUser str = new User();
		str.setName("Jon");
		ClassLoader classLoader = str.getClass().getClassLoader();
		Class[] interfaces = str.getClass().getInterfaces();   
		InvocationHandler handler = new MyInterceptor(str); 
		IUser s = (IUser) Proxy.newProxyInstance(classLoader, interfaces,    handler);
		System.out.println(s);
		s.setName("xixi");
	}
}
