package test.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxyInvocationHandler implements InvocationHandler {
	Object target;

	private Interceptor[] interceptors;

	public DynamicProxyInvocationHandler(Object target, Interceptor interceptor) {
		this.target = target;
		this.interceptors = new Interceptor[]{interceptor};
	}
	public DynamicProxyInvocationHandler(Object target, Interceptor[] interceptors) {
		this.target = target;
		this.interceptors = interceptors;
	}
	

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		try {
			// 在执行method之前调用interceptor去做什么事
			for(Interceptor interceptor:interceptors){
				interceptor.before(method, args);
			}
			// 在这里我们调用原始实例的method
			result = method.invoke(this.target, args);
			// 在执行method之后调用interceptor去做什么事
			for(Interceptor interceptor:interceptors){
				interceptor.after(method, args);
			}
			
		} catch (Throwable throwable) {
			// 在发生异常之后调用interceptor去做什么事
			for (Interceptor interceptor : interceptors) {
				interceptor.afterThrowing(method, args, throwable);
			}
			throw throwable;
		} finally {
			// 在finally之后调用interceptor去做什么事
			for (Interceptor interceptor : interceptors) {
				interceptor.afterFinally(method, args);
			}
		}
		return result;
	}

}
