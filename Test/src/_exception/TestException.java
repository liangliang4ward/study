package _exception;

public class TestException {
	public static void main(String[] args) {
		
		
		try {
			throw new Error();
		} catch (Exception e) {
			System.out.println("C");
		}finally{
			System.out.println("A");
		}
		System.out.println("B");
	}
}
