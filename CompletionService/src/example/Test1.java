package example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test1 {
	public static AtomicInteger ai = new AtomicInteger(7);
	public static void main(String[] args) throws InterruptedException {
		ExecutorService es = Executors.newFixedThreadPool(3);
		
		List<Callable<String>> cs = new ArrayList<Callable<String>>();
		long t1=System.currentTimeMillis();
		for(int i=0;i<5;i++){
			cs.add(new Callable<String>() {

				@Override
				public String call() throws Exception {
					int i = 0;
					while(i<1000000000){
						i++;
					}
					System.out.println("over="+i);
					return null;
				}
			});
		}
		es.invokeAll(cs, 100, TimeUnit.MILLISECONDS);
		long t2=System.currentTimeMillis();
		System.out.println((t2-t1)/1000);
//		for(Callable<String> c : cs){
//			es.submit(c);
//		} 
		System.out.println("Sdf");
		es.shutdown();
	}
}
