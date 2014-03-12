package example;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestSubmit {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService es = Executors.newFixedThreadPool(2);
		
		Future<?> s = es.submit(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Sdf");
			}
		},"123");
		
		System.out.println(s.get());
		
		
		s = es.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return "456";
			}
			
		});
		
		System.out.println(s.get());
	}
}
