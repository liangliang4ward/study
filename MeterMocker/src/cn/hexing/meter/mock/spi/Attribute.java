package cn.hexing.meter.mock.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Attribute {

	private Map<AttributeOption,Object> map = new ConcurrentHashMap<AttributeOption,Object>();
	
	public boolean contains(AttributeOption key) {
		return map.containsKey(key);
	}

	public Object get(AttributeOption key) {
		return map.get(key);
	}

	public void set(AttributeOption key, Object value) {
		map.put(key, value);
	}
	
	public Object remove(AttributeOption key){
		return map.remove(key);
	}

}
