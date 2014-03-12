package org.chinasb.client;

import java.util.HashMap;
import java.util.Map;

public class Test11 {

	
	
	public static Map<String,String> map  =new HashMap<String, String>();
	
	public static void main(String[] args) {
	
		map.put("1", "2,3");
		map.put("2","4,5");
		map.put("3", "6,7");
		map.put("6", "8");
		
		
		
		System.out.println(findDeep("1"));
	}
	
	
	public static int findDeep(String item){
		int deep=0;
		String value = map.get(item);
		if(value ==null){
			return deep;
		}else{
			String[] values=value.split(",");
			deep++;
			int maxDeep =-1;
			for(String v:values){
				deep+=findDeep(v);
				if(deep >maxDeep)
					maxDeep = deep;
				deep=0;
			}
			deep = maxDeep;
		}
		
		return deep;
	}
	
}


