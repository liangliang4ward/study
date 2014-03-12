package example;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test2 {
	public static void main(String[] args) {
		
		PriorityBlockingQueue<Runnable> bq = new PriorityBlockingQueue<Runnable>();
		
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, bq);
		
		
		
	}
}
