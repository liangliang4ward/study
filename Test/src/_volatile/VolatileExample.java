package _volatile;

public class VolatileExample {
	int x = 0;
	volatile int b = 0;

	private void write() {
		x = 5;
		b = 1;
	}

	private void read() {
		int dummy = b;
		while (x != 5) {
		}
	}

	public static void main(String[] args) throws Exception {
		final VolatileExample example = new VolatileExample();
		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				example.write();
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				example.read();
			}
		});
		thread1.start();
		thread2.start();
		thread1.join();
		thread2.join();
	}
}