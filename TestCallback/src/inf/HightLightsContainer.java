package inf;

public interface HightLightsContainer {
	
	public void initialize(Callback callback);
	
	public void test();
	
	
	public static final class Callback{
		public Callback() {
			
		}
		
		public final void highlightsChanged(){
			
		}
	}
}
