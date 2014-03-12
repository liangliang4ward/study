import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TestList {
	
	public static void main(String[] args) {
		Map<String,List<String>> rtusClientIdsMap = new ConcurrentHashMap<String, List<String>>();
		List<String> list = Collections.synchronizedList(new LinkedList<String>());
		rtusClientIdsMap.put("000020130716", list);	
		
		rtusClientIdsMap.get("000020130716");
		
	}
}
