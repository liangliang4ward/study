package _volatile;

public class Test11 {

	public static void main(String[] args) {
		Test11 t = new Test11();
		t.run();
		t.test();
	}
	
	private int foo =0;
	
	
	public void test(){
	
		while(foo!=255){
			System.out.println("run...");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void run(){
		new Thread1().start();
	}
	
	class Thread1 extends Thread{
		
		
		@Override
		public void run(){
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("after sleep");
			
			while(foo!=255){
				foo++;
				
			}
		}
		
	}
	
}
