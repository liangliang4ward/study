package example;

import java.util.concurrent.Semaphore;

public class Test {
	Semaphore s = new Semaphore(5);
	
	
	public Test(){
		new Thread1().start();
		new Thread1().start();
	}
	
	public static void main(String[] args) {
		new Test();
	}
	class Thread1 extends Thread{
		
		
		@Override
		public void run(){
			try {
				System.out.println(this.getName()+",try 5");
				s.acquire(5);
				System.out.println(this.getName()+" get "+5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			s.release(5);
			System.out.println(this.getName()+" release "+1);
		}
	}
}
