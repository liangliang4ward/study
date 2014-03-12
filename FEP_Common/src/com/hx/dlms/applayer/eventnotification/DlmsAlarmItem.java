package com.hx.dlms.applayer.eventnotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DlmsAlarmItem {
	private String alarmCode;
	private String   time = null;		// time of alarm/event
	private HashMap<String,String> relatedData = null;
	private String prefix = "03";
	
	
	public DlmsAlarmItem(){ 
		Date date=new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time=sdf.format(date);
	}
	
	public DlmsAlarmItem( short eventCode ){ alarmCode = prefix+DlmsAlarmEventCode.toString(eventCode); }

	public DlmsAlarmItem( short eventCode, String occurTime ){
		alarmCode = prefix+DlmsAlarmEventCode.toString(eventCode);
		if( null != occurTime )
			time = occurTime;
	}
	
	public String getClassId(){
		if( null == alarmCode )
			return null;
		int index = alarmCode.indexOf('.');
		if( index<=0 )
			return null;
		return alarmCode.substring(0, index);
	}
	
	public String getObis(){
		if( null == alarmCode )
			return null;
		String sub = alarmCode;
		int i1 = alarmCode.indexOf('|');
		if( i1>0 )
			sub = alarmCode.substring(0, i1);
		i1 = sub.indexOf('.');
		int i2 = sub.lastIndexOf('.');
		if( i1<=0 || i2<=0 )
			return null;
		return sub.substring(i1+1,i2);
	}
	
	public String getAttributeId(){
		if( null == alarmCode )
			return null;
		String sub = alarmCode;
		int i1 = alarmCode.indexOf('|');
		if( i1>0 )
			sub = alarmCode.substring(0, i1);
		i1 = sub.lastIndexOf('.');
		if( i1<=0 )
			return null;
		return sub.substring(i1+1);
	}
	
	public void setRelatedData(String itemId, String itemValue){
		if( null == relatedData )
			relatedData = new HashMap<String,String>();
		relatedData.put(itemId, itemValue);
	}
	
	public String getRelatedString(){
		if( null == relatedData )
			return "";
		StringBuilder sb = new StringBuilder(256);
		for( Map.Entry<String,String> entry : relatedData.entrySet() ){
			if( sb.length()>0 )
				sb.append(";");
			sb.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return sb.toString();
	}
	
	@Override
	public String toString(){
		if( null == alarmCode )
			return "";
		if( null == relatedData || relatedData.size()==0 )
			return alarmCode;
		StringBuilder sb = new StringBuilder(256);
		sb.append(alarmCode);
		sb.append(":");
		for( Map.Entry<String,String> entry : relatedData.entrySet() ){
			if( sb.length()>0 )
				sb.append(";");
			sb.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return sb.toString();
	}

	public final String getAlarmCode() {
		return alarmCode;
	}

	public final void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public final String getTime() {
		return time;
	}

	public final void setTime(String time) {
		this.time = time;
	}

	public final HashMap<String, String> getRelatedData() {
		return relatedData;
	}
	
}
