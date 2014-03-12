import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TestMap {
	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> q = new LinkedBlockingQueue<String>();
		System.out.println(q.take());
		for(int i=0;i<1000;i++){
			q.offer(""+i);
		}
		System.out.println(q.size());
		System.out.println(q.poll());
		System.out.println(q.poll());

	}
}
