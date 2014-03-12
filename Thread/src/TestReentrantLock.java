import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;


public class TestReentrantLock {

	ReentrantLock LOCK = new ReentrantLock();
	CountDownLatch cdl = new CountDownLatch(3);
	public TestReentrantLock(){
		new Thread1().start();
		new Thread2().start();
	}
	
	public static void main(String[] args) {
		TestReentrantLock trl = new TestReentrantLock();
	}
	
	class Thread1 extends Thread{
		@Override
		public void run(){
			
			LOCK.lock();
			
			System.out.println("World");
			
			LOCK.unlock();
			
		}
	}
	
	class Thread2 extends Thread{
		
		@Override
		public void run(){
			LOCK.lock();
			
			System.out.println("Hello");
			
			LOCK.unlock();
			
		}
	}
	
}
