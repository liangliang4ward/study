package example;

import java.util.concurrent.FutureTask;

public class Test {

	volatile int x ;
	
	public Test(){
		new Thread1().start();
		new Thread2().start();
	}
	
	public static void main(String[] args) {
		new Test();
	}
	
	class Thread1 extends Thread{
		@Override
		public void run(){
			x+=1;
			System.out.println("thread-1->"+x);
		}
	}
	class Thread2 extends Thread{
		@Override
		public void run(){
			System.out.println("thread-2->"+x);
		}
	}
}
