import java.util.concurrent.TimeUnit;


public class TestJoin {
	
	
	
	static class Thread1 extends Thread{
		
		@Override
		public void run(){
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread1 t1 = new Thread1();
		t1.start();
		t1.join();
	}
	
}
