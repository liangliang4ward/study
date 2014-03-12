import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Test1 {

	ReentrantReadWriteLock rrw = new ReentrantReadWriteLock(true);
	
	public Test1(){
		new Thread(new Thread1()).start();
		new Thread(new Thread2()).start();

	}
	public static void main(String[] args) {
		new Test1();
	}
	
	private void test2() {
		rrw.writeLock().lock();
		
		System.out.println("write");
		
		rrw.writeLock().unlock();
	}
	
	public void test1(){
		rrw.readLock().lock();
		
		System.out.println("read");
		
		rrw.readLock().unlock();
	}
	class Thread1 implements Runnable{

		@Override
		public void run() {
			test1();
		}
		
	}
	
	class Thread2 implements Runnable{

		@Override
		public void run() {
			test2();
		}

	}
}
