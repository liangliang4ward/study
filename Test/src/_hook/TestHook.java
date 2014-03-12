package _hook;

public class TestHook {
	

	
	static class Thread1 implements Runnable{
		@Override
		public void run() {
			System.out.println("sdfsdf");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		Thread1 t =new Thread1();
		
		Runtime.getRuntime().addShutdownHook(new Thread(t));

		System.out.println("gg");
		
		Thread.sleep(1000);
	}
}
