package thread.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class Driver {
	private static final int N = 3;
	
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(N);
		
		 for (int i = 0; i < N; ++i) // create and start threads
		       new Thread(new Worker(startSignal, doneSignal)).start();
		 doSomethingElse();            // don't let run yet
	     startSignal.countDown();      // let all threads proceed
	     doSomethingElse();
	     doneSignal.await();           // wait for all to finish

	}


	private static void doSomethingElse() {
		System.out.println("do sth.");
	}

}
