
public class TestThreadLocal {

	
	
	public static ThreadLocal<Integer> local = new ThreadLocal<Integer>(){

		@Override
		protected Integer initialValue() {
			return 1;
		}
		
	};
	
	public static void main(String[] args) {
		
		for(int i=0;i<5;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					int num = local.get();
					for(int i=0;i<5;i++){
						num++;
					}
					local.set(num);
					System.out.println(local.get());
					
				}
			}).start();
		}
	}
	
}
