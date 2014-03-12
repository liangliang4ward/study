package example;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Test {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
	
		ExecutorService executor = Executors.newFixedThreadPool(1);
		ExecutorCompletionService<String> ecs = new ExecutorCompletionService<String>(executor) ;
		Future<String> f = ecs.submit(new Callable<String>() {
			
			@Override
			public String call() throws Exception {
					Thread.sleep(1000);
					System.out.println("Abc");
					return "abc";
			}
		});
		Future<String> f2=ecs.submit(new Callable<String>() {
			
			@Override
			public String call() throws Exception {
					Thread.sleep(2000);
					System.out.println("Abc1");
					return "123";
			}
		});
		
		System.out.println(f.get());
		System.out.println(f2.get());
		executor.shutdown();
	}
	
	
}
