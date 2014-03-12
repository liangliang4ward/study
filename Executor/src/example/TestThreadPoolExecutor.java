package example;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPoolExecutor {
	public static void main(String[] args) {
		LinkedBlockingQueue<Runnable> lbq = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(1, 2, 0, TimeUnit.SECONDS, lbq);
		tpe.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					System.out.println("abc");
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
}	
