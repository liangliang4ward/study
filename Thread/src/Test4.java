import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Test4 {
	public static void main(String[] args) {
		
		List<String> str = Collections.synchronizedList(new ArrayList<String>());
		for(int i=0;i<10;i++){
			str.add(i+"");
		}
		
		for(String s:str){
			if(s.equals("1")){
				str.remove(2);
			}
		}
		
	}
}
