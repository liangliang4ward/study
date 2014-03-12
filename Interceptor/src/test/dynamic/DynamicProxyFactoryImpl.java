package test.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxyFactoryImpl implements DynamicProxyFactory {

	@Override
	public <T> T createProxy(T target, Interceptor interceptor) {
		
		InvocationHandler handler = new DynamicProxyInvocationHandler(target,interceptor);
		
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), handler);
	}

}
