
public class TestLockMethod {

	
	public TestLockMethod(){
		new Thread1().start();
		new Thread2().start();
	}
	
	class Thread1 extends Thread{
		@Override
		public void run(){
			method1();
		}
	}
	
	class Thread2 extends Thread{
		@Override
		public void run(){
			method2();
		}
	}
	public static void main(String[] args) {
		new TestLockMethod();
	}
	
	public synchronized void method1(){
		System.out.println("method1");
		method2();
	}
	
	public synchronized void method2(){
		System.out.println("method2");
	}
	
}
