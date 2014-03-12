package pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class TestThreadPool1 {

	public TestThreadPool1(){
		ExecutorService s = Executors.newCachedThreadPool();
		for(int i=0;i<5;i++)
			s.execute(new WorkThread());
		for(int i=0;i<100;i++){
			queue.offer(""+i);
		}
	}
	public static void main(String[] args) {
		new TestThreadPool1();
	}
	
	BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
	
	private class WorkThread implements Runnable{

		@Override
		public void run() {
			try {
				while(true){
					Object s = queue.take();
					System.out.println(s);	
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
