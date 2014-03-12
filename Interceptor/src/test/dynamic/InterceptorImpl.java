package test.dynamic;

import java.lang.reflect.Method;

public class InterceptorImpl implements Interceptor {

	@Override
	public void before(Method method, Object[] args) {
		System.out.println("before invoe method:"+method.getName());
	}

	@Override
	public void after(Method method, Object[] args) {
		System.out.println("after invoe method:"+method.getName());
	}

	@Override
	public void afterThrowing(Method method, Object[] args, Throwable throwable) {
		System.out.println("afterThrowing invoe method:"+method.getName());
	}

	@Override
	public void afterFinally(Method method, Object[] args) {
		System.out.println("afterFinally invoe method:"+method.getName());
	}

}
