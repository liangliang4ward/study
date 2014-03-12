import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class FIFOMutex {
	private final AtomicBoolean locked = new AtomicBoolean(false);
	private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();

	public void lock() {
		boolean wasInterrupted = false;
		Thread current = Thread.currentThread();
		waiters.add(current);

		// Block while not first in queue or cannot acquire lock
		while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
			LockSupport.park(this);
			if (Thread.interrupted()) // ignore interrupts while waiting
				wasInterrupted = true;
		}

		waiters.remove();
		if (wasInterrupted) // reassert interrupt status on exit
			current.interrupt();
	}

	public void unlock() {
		locked.set(false);
		LockSupport.unpark(waiters.peek());
	}

	static class Thread1 extends Thread{
		FIFOMutex fm ;
		public Thread1(FIFOMutex fm){
			this.fm = fm;
		}
		
		@Override
		public void run(){
			fm.lock();
			System.out.println("Sdf");
			fm.unlock();
		}
	}
	
	public static void main(String[] args) {
		FIFOMutex fm = new FIFOMutex();
		Thread1 t1 = new Thread1(fm);
		Thread1 t2 = new Thread1(fm);
		t1.start();
		t2.start();

	}
}
