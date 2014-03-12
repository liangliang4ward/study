
public class TestVolate {

	 volatile int b = 0;
	
	
	public TestVolate(){
		for(int i=0;i<1000;i++){
			new Thread1().start();
		}
	}
	
	class Thread1 extends Thread{
		
		@Override
		public void run(){
			b++;
			System.out.println(b);
		}
	}
	
	public static void main(String[] args) {
		new TestVolate();
	}
}

