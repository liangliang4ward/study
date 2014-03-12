package test.dynamic;

public class Test {
	public static void main(String[] args) {
		
		Interceptor interceptor = new InterceptorImpl();
		Service target = new ServiceImpl();
		
		DynamicProxyFactory dynamicProxyFactory = new DynamicProxyFactoryImpl();   
		
		Service s = dynamicProxyFactory.createProxy(target, interceptor);
		
		s.greet("Hello");
	}
}
