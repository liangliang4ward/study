package cn.hexing.fk.common.events.event;

import java.text.DecimalFormat;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.common.spi.IProfile;
import cn.hexing.fk.message.IMessage;

public class MemoryProfileEvent implements IEvent,IProfile {
	private EventType type = EventType.SYS_MEMORY_PROFILE;
	public static long maxMemory = Runtime.getRuntime().maxMemory()>>10;
	public static long totalMemory = Runtime.getRuntime().totalMemory()>>10;
	public static long freeMemory = Runtime.getRuntime().freeMemory()>>10;
	private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

	public MemoryProfileEvent(){
	}
	
	private void update(){
		maxMemory = Runtime.getRuntime().maxMemory()>>10;
		totalMemory = Runtime.getRuntime().totalMemory()>>10;
		freeMemory = Runtime.getRuntime().freeMemory()>>10;
	}
	
	public IMessage getMessage() {
		return null;
	}

	public Object getSource() {
		return null;
	}

	public EventType getType() {
		return type;
	}

	public void setSource(Object src) {
	}

	public String profile(){
		update();
		StringBuffer sb = new StringBuffer(128);
		sb.append("<memory-profile type=\"memory\">");
		sb.append("\r\n        <totalMemory>");
		if( totalMemory> 1024 )
			sb.append(decimalFormat.format(totalMemory>>10)).append("M</totalMemory>");
		else
			sb.append(decimalFormat.format(totalMemory)).append("K</totalMemory>");
		
		sb.append("\r\n        <freeMemory>");
		if( freeMemory> 1024 )
			sb.append(decimalFormat.format(freeMemory>>10)).append("M</freeMemory>");
		else
			sb.append(decimalFormat.format(freeMemory)).append("K</freeMemory>");
		
		sb.append("\r\n        <maxMemory>");
		if( maxMemory> 1024 )
			sb.append(decimalFormat.format(maxMemory>>10)).append("M</maxMemory>");
		else
			sb.append(decimalFormat.format(maxMemory)).append("K</maxMemory>");
		sb.append("\r\n    </memory-profile>");
		return sb.toString();
	}
	
	public String toString(){
		return profile();
	}
}
