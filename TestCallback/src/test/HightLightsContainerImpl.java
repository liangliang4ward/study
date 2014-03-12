package test;
import inf.HightLightsContainer;


public class HightLightsContainerImpl implements HightLightsContainer{
	Callback callback;
	public void initialize(Callback callback) {
		this.callback=callback;
	}

	public void test() {
		
	}
	
	public static void main(String[] args) {
		HightLightsContainerImpl hm = new HightLightsContainerImpl();
		hm.initialize(new Callback());
	}

}
