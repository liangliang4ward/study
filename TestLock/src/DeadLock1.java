
public class DeadLock1 {

	
	Object lock1 = new Object();
	
	Object lock2 = new Object();
	
	public static void main(String[] args) {
		new DeadLock1();
	}
	public DeadLock1(){
		
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (lock1) {
				
					System.out.println("lock1");
					
					synchronized (lock2) {
						System.out.println("lock2");
					}
					
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (lock2) {
				
					System.out.println("lock2");
					
					synchronized (lock1) {
						System.out.println("lock1");
					}
					
				}
			}
		});
		t1.start();
		t2.start();
	}
	
	
	
	
	
}
