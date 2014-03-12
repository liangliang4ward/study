import java.util.concurrent.locks.LockSupport;



public class TestLockSupport {
	
	
	class Thread1 extends Thread{
		
		public void run(){
			LockSupport.park();
		}
	}
	
	class Thread2 extends Thread{
		public void run(){
		}
	}
	
	public static void main(String[] args) {
	}
}
