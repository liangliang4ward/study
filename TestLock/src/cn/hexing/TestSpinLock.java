package cn.hexing;

public class TestSpinLock {
	
	SpinLock lock = new SpinLock();
	
	class Thread1 extends Thread{
		
		@Override
		public void run(){
			
			lock.lock();
			
			System.out.println("关键代码");
			
			lock.unlock();
		}
	}
	
	public TestSpinLock(){
		new Thread1().start();
		new Thread1().start();
	}
	public static void main(String[] args) {
		new TestSpinLock();
	}
}
