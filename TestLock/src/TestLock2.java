
public class TestLock2 {

	
	public TestLock2(){
		new Thread1(1).start();
		new Thread1(1).start();
	}
	public static void main(String[] args) {
		
		int i=1;
		int i2=1;
		
		String meterId="123"+i;
		String meterId2="123"+i2;
		System.out.println(meterId==meterId2);
		System.out.println(meterId.equals(meterId2));

//		new TestLock2();
	}
	
	
	
	class Thread1 extends Thread{
		int i;
		public Thread1(int i){
			this.i = i;
		}
		@Override
		public void run(){
			String meterId="123"+i;
			synchronized (meterId) {
				System.out.println(meterId);
			}
		}
	}
}
