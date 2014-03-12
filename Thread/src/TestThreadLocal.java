import java.util.concurrent.atomic.AtomicInteger;

public class TestThreadLocal extends Thread{

	private AtomicInteger uniq = new AtomicInteger();
	
	ThreadLocal<Integer> tl = new ThreadLocal<Integer>(){
		@Override
		protected Integer initialValue(){
			return uniq.getAndIncrement();
		}
	};
	
	public int get(){
		return uniq.get();
	}
	
	public static void main(String[] args) {
	new TestThreadLocal().start();
	}
	
}
