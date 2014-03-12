package _thread;

public class Single {

	
	private static Single in = null;
	
	private Single(){
	}
	
	public static Single getInstance(){

		if(in == null){
			synchronized (Single.class) {
				if( in == null){
					in= new Single();
					return in;
				}
			}
		}
		return in;
	}
	
	
	private static class Thread1 extends Thread{
		
		public void run(){
			Single.getInstance();
		}
	}
	public static void main(String[] args) {
 		new Thread1().start();
		new Thread1().start();
	}
}
