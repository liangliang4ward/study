import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class TestPoolExpansion {
	
	public static void main(String[] args) throws InterruptedException {
		int MAX_SIEZ=10;
		
		ExecutorService es = Executors.newFixedThreadPool(MAX_SIEZ);
		
		for(int i=0;i<10*MAX_SIEZ;i++){
			
			es.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(Long.MAX_VALUE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			
		}
		System.out.println(System.nanoTime()+"-"+System.currentTimeMillis());
		TestThreadFactory threadFactory =new TestThreadFactory();
		for(int i=0; i<20 && threadFactory.num.get()<MAX_SIEZ;i++){
			Thread.sleep(100);
		}
		System.out.println(threadFactory.num.get());
		System.out.println(threadFactory.num.get() == MAX_SIEZ);
		es.shutdown();
	}
	
	static class TestThreadFactory implements ThreadFactory{
		private final ThreadFactory  factory = Executors.defaultThreadFactory();
		private final AtomicInteger num = new AtomicInteger(0);
		
		
		@Override
		public Thread newThread(Runnable r) {
			num.incrementAndGet();
			return factory.newThread(r);
		}
		
	}
}
