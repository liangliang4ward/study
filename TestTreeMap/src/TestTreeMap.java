import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;


public class TestTreeMap {
	public static void main(String[] args) {
		
		BidiMap map = new TreeBidiMap();
		boolean s = true;
		if(s){
			System.out.println("Sdf");
		}else{
			System.out.println("dsf");
		}
		
		map.put("key1", "value1");
		map.put("key1", "value2");

	}
}
