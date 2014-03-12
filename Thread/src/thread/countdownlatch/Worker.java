package thread.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			startSignal.await();
	        doWork();
	        doneSignal.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void doWork() {
		System.out.println("doWork");
	}

	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;

	Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

}
