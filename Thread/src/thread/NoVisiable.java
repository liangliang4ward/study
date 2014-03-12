package thread;

public class NoVisiable {

	public static boolean isReady;
	
	public static int num;
	
	private static class ReaderThread extends Thread{
		@Override
		public void run(){
			while(!isReady)
				Thread.yield();
			System.out.println(num);
		}
	}
	
	public static void main(String[] args) {
		new ReaderThread().start();
		num=42;
		isReady = true;
	}
	
	
}
