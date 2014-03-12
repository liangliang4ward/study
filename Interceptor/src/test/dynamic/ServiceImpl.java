package test.dynamic;

public class ServiceImpl implements Service {

	@Override
	public String greet(String hello) {
		if(hello.equals("Hello")) throw new RuntimeException("Exception..");
		String result = "Hello, " + hello;
		System.out.println(result);
		return result;
	}

}
