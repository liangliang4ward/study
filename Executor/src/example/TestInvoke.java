package example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TestInvoke {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService es = Executors.newCachedThreadPool();
		List<Callable<String>> tasks = new ArrayList<Callable<String>>();
		for(int i=0;i<10;i++){
			tasks.add(new Task());
		}
		List<Future<String>> res = es.invokeAll(tasks);
		System.out.println("InvokeAll");
		for(Future<String> f : res){
			System.out.println(f.get());
		}
		
	}
	
	static class Task implements Callable<String>{

		@Override
		public String call() throws Exception {
			TimeUnit.SECONDS.sleep(1);
			return UUID.randomUUID().toString();
		}
		
	}
}
