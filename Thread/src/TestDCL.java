
public class TestDCL {

	
	
	
	public static void main(String[] args) throws InterruptedException {
		for(int i=0;i<100;i++){
			new Thread1().start();			
		}
		System.gc();
		Thread.sleep(1000);
	}
	

}
class Thread1 extends Thread{
	
	@Override
	public void run(){
		Singleton.getInstance();
		System.gc();
	}
}
class Singleton{
	private Singleton(){
		System.out.println("This consuture of singleton");
	}
	
	
	private static Singleton instance=null;
	
	public static Singleton getInstance(){
		if(instance == null){
			synchronized (Singleton.class) {
				if(instance == null)
					instance = new Singleton();				
			}
		}
		return instance;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("sdf");
	}
} 