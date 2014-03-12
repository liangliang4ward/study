import java.util.concurrent.atomic.AtomicInteger;


public class TestAtoic {
	public static void main(String[] args) throws InterruptedException {
		
		final AtomicInteger ai =new AtomicInteger(0);
		final int[] ns = new int[1];
		Thread[] t = new Thread[2];
		
		for(int i=0;i<2;i++){
			t[i] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(int i=0;i<10000;i++){
						ai.incrementAndGet();
						ns[0]++;
					}
				}
			});
		}
		for(int i=0;i<2;i++){
			t[i].start();
		}
		
		for(int i=0;i<2;i++){
			t[i].join();
		}
		
		System.out.println(ai.get()+"-"+ns[0]);
		
		
	}
}
