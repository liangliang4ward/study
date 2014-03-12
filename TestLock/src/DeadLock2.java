


public class DeadLock2 {
	
	
	
	public static void main(String[] args) {
		new DeadLock2();
	}
	
	public DeadLock2(){
		final Child child = new Child();
		new Thread(){
			public void run(){
				child.doSth();				
			}
		}.start();
		new Thread(){
			public void run(){
				child.doSth();				
			}
		}.start();
	}
	
	
	class Parent {
		
		public synchronized void doSth(){
			System.out.println("I'm in parent");
		}
	}
	
	class Child extends Parent{
		public synchronized void doSth(){
			System.out.println("I'm in child");
			super.doSth();
		}
	}
}
