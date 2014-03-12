package test.dynamic;

import java.lang.reflect.Method;

public interface Interceptor {
	public void before(Method method, Object[] args);

	public void after(Method method, Object[] args);

	public void afterThrowing(Method method, Object[] args, Throwable throwable);

	public void afterFinally(Method method, Object[] args); 
}
