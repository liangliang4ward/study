package thread.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Driver2 {
	private static final int N = 3;

	
	  public static  void main(String[] args) throws InterruptedException {
		     CountDownLatch doneSignal = new CountDownLatch(N);
		     Executor e = Executors.newFixedThreadPool(1);
		     for (int i = 0; i < N; ++i) // create and start threads
		       e.execute(new WorkerRunnable(doneSignal, i));

		     doneSignal.await();           // wait for all to finish
		   }

}
