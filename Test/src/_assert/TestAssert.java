package _assert;

public class TestAssert {

	public static void main(String[] args) {
		boolean isOpen = false;  
		   assert isOpen=true; //如果开启了断言，会将isOpen的值改为true 
		   System.out.println(isOpen);//打印是否
		TestAssert ta = new TestAssert ();
		assert true;
		ta.test(null);
	}
	
	
	public void test(String msg){
		assert msg!=null:"msg is not null";
		System.out.println(msg.split("sdf"));
	}
	
}
