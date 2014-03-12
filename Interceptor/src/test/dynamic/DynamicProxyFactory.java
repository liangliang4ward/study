package test.dynamic;

public interface DynamicProxyFactory {
	  public <T> T createProxy(T target, Interceptor interceptor);   
}
