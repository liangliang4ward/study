package example;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

	private CompletionService<String> service ;
	
	public Test(){
		Executor executor = Executors.newCachedThreadPool();
		service= new ExecutorCompletionService<String>(executor );
	}
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new Test().runTask();
	}
	
	public void runTask() throws InterruptedException, ExecutionException{
		service.submit(new Task());
		System.out.println(service.take().get());
	}
	
	
	class Task implements Callable<String>{

		@Override
		public String call() throws Exception {
			TimeUnit.SECONDS.sleep(10);
			return UUID.randomUUID().toString();
		}
		
	}
}
