package example;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {

	private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>(2);
	
	public static void main(String[] args) {
		new Test();
	}
	
	public Test(){
		new Thread(new Producer()).start();
		new Thread(new Consumer()).start();
		new Thread(new Consumer()).start();
	}
	
	class Producer implements Runnable{

		@Override
		public void run() {
			while(true){
				produce();
			}
		}
		public void produce(){
			String thing = UUID.randomUUID().toString();
			queue.offer(thing);
		}
		
	}
	
	class Consumer implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					consume();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void consume() throws InterruptedException {
			System.out.println(queue.take());
		}
		
	}
	
	
}
