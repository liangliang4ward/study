/**
 * Resolve the NotificationRequest into DlmsAlarmItems
 */
package com.hx.dlms.applayer.eventnotification;

import java.util.HashMap;

import com.hx.dlms.DlmsData;
import com.hx.dlms.DlmsDataType;
import com.hx.dlms.DlmsDateTime;
import com.hx.dlms.applayer.ObisCodeMap;

/**
 * @author Bao Hongwei
 *
 */
public class DlmsEventAlarmResolver {
	
	private static DlmsEventAlarmResolver instance = null;
	private DlmsEventAlarmResolver(){}
	public enum DlmsAlarmType { UNDEF, DETAIL, STANDAR, FRAUD, RELAY, GRIDEVENT, PREPAY, OUTAGE, MBUS, MAGNETIC,REVERSE, OPENMETER, OPENTERMINAL, PROGRAM,POWERDOWN, BYPASS, OPTICAL, LOWVA,LOWVB,LOWVC,OVERVA,OVERVB,OVERVC,OVERCURR };

	public static final DlmsEventAlarmResolver getInstance(){
		if( null == instance ){
			instance = new DlmsEventAlarmResolver();
			instance.init();
		}
		return instance; 
	}
	
	private HashMap<String,DlmsAlarmType> types = new HashMap<String,DlmsAlarmType>();
	
	public void init(){
		types.put("0.0.99.99.0.255", DlmsAlarmType.DETAIL);
		types.put("7.0.0.99.99.0.255.2", DlmsAlarmType.DETAIL);

		types.put("0.0.99.98.0.255", DlmsAlarmType.STANDAR);
		types.put("7.0.0.99.98.0.255.2|", DlmsAlarmType.STANDAR);
		types.put("0.0.99.98.1.255", DlmsAlarmType.FRAUD);
		types.put("7.0.0.99.98.1.255.2|", DlmsAlarmType.FRAUD);
		
		types.put("0.0.99.98.2.255", DlmsAlarmType.RELAY);
		types.put("7.0.0.99.98.2.255.2|", DlmsAlarmType.RELAY);
		types.put("0.0.99.98.4.255", DlmsAlarmType.GRIDEVENT);
		types.put("7.0.0.99.98.4.255.2|", DlmsAlarmType.GRIDEVENT);
		types.put("0.0.99.98.5.255", DlmsAlarmType.PREPAY);
		types.put("7.0.0.99.98.5.255.2|", DlmsAlarmType.PREPAY);
		
		types.put("1.0.99.97.0.255", DlmsAlarmType.OUTAGE);
		types.put("7.1.0.99.97.0.255.2|", DlmsAlarmType.OUTAGE);
		types.put("0.10.24.5.0.255", DlmsAlarmType.OUTAGE);
		types.put("7.0.10.24.5.0.255.2|", DlmsAlarmType.OUTAGE);

		types.put("0.0.99.98.3.255", DlmsAlarmType.MBUS);
		types.put("7.0.0.99.98.3.255.2|", DlmsAlarmType.MBUS);
		
		types.put("0.5.24.5.0.255", DlmsAlarmType.MAGNETIC);
		types.put("7.0.5.24.5.0.255.2|", DlmsAlarmType.MAGNETIC);
		types.put("0.6.24.5.0.255", DlmsAlarmType.OPENMETER);		//	open meter 
		types.put("7.0.6.24.5.0.255.2|", DlmsAlarmType.OPENMETER);
		types.put("0.7.24.5.0.255", DlmsAlarmType.OPENTERMINAL);    //开端钮盖事件, open terminal joint event
		types.put("7.0.7.24.5.0.255.2|", DlmsAlarmType.OPENTERMINAL);
		types.put("0.8.24.4.0.255", DlmsAlarmType.REVERSE);//电流反向
		types.put("7.0.8.24.4.0.255.2|", DlmsAlarmType.REVERSE);
		
		types.put("0.9.24.5.0.255", DlmsAlarmType.PROGRAM);//重新编程
		types.put("7.0.9.24.5.0.255.2|", DlmsAlarmType.PROGRAM);//重新编程
		
		types.put("0.10.24.5.0.255", DlmsAlarmType.POWERDOWN);  //电网掉电
		types.put("7.0.10.24.5.0.255.2|", DlmsAlarmType.POWERDOWN);//电网掉电

		types.put("0.18.24.5.0.255", DlmsAlarmType.OPTICAL);
		types.put("7.0.18.24.5.0.255.2|", DlmsAlarmType.OPTICAL);
		types.put("1.0.99.128.0.255", DlmsAlarmType.BYPASS);
		types.put("7.1.0.99.128.0.255.2|", DlmsAlarmType.BYPASS);
		
		types.put("0.11.24.5.0.255", DlmsAlarmType.LOWVA);  //A相失压
		types.put("7.0.11.24.5.0.255.2|", DlmsAlarmType.LOWVA);
		types.put("0.12.24.5.0.255", DlmsAlarmType.LOWVB);  //B相失压
		types.put("7.0.12.24.5.0.255.2|", DlmsAlarmType.LOWVB);
		types.put("0.13.24.5.0.255", DlmsAlarmType.LOWVC);  //C相失压
		types.put("7.0.13.24.5.0.255.2|", DlmsAlarmType.LOWVC);
		types.put("0.14.24.5.0.255", DlmsAlarmType.OVERVA);  //A相过压
		types.put("7.0.14.24.5.0.255.2|", DlmsAlarmType.OVERVA);
		types.put("0.15.24.5.0.255", DlmsAlarmType.OVERVB);  //B相过压
		types.put("7.0.15.24.5.0.255.2|", DlmsAlarmType.OVERVB);
		types.put("0.16.24.5.0.255", DlmsAlarmType.OVERVC);  //C相过压
		types.put("7.0.16.24.5.0.255.2|", DlmsAlarmType.OVERVC);

		types.put("0.17.24.5.0.255", DlmsAlarmType.OVERCURR);  //C相过压
		types.put("7.0.17.24.5.0.255.2|", DlmsAlarmType.OVERCURR);
	}
	
	/**
	 * Resolve EventNotificationRequest's value. 
	 * It is applicable for journal events or running events.  只应用于流水帐事件结果解析//
	 * This function only resolve array-structure type.
	 * @param parentCode
	 * @param obis
	 * @param arraystruct
	 * @return
	 */
	public final DlmsAlarmItem[] resolveRunningEventArray(String parentCode,String obis, DlmsData arraystruct){
		if( arraystruct == null ||arraystruct.type() != DlmsDataType.ARRAY )
			return null;

		DlmsAlarmType type = types.get(obis);
		if( null == type )
			type = DlmsAlarmType.UNDEF;
		DlmsData[] ar = arraystruct.getArray();
		DlmsAlarmItem[] result = new DlmsAlarmItem[ar.length];

		short evtCode = 0;
		
		switch(type){
		case BYPASS:
			/**
			 *    structure include {
					开始时间date&time;
					正向有功电量  U32;
					A相电压  U16;
					B相电压  U16;
					C相电压  U16;
					A相电流  U16;
					B相电流  U16;
					C相电流  U16;  		}
			 */
			for(int i=0; i<ar.length; i++ ){
				int structSize = ar[i].getStructureSize();
				if( structSize<=0 )
					return null;
				int offset = 0;
				DlmsDateTime dlmsTime = ar[i].getArrayAt(offset++).getDateTime();
				result[i] = new DlmsAlarmItem(DlmsAlarmEventCode.GRID_BY_PASS_BEGIN,dlmsTime.toString());
				if( offset < structSize ){
					long totalActiveEnergy = ar[i].getArrayAt(offset++).getDoubleLongUnsigned()*10;
					result[i].setRelatedData(ObisCodeMap.TOTAL_ACTIVE_ENERGY, Long.toString(totalActiveEnergy));
				}
				if( offset+6 <= structSize ){
					double v = (ar[i].getArrayAt(offset++).getUnsignedLong()*1.0 / 100);
					result[i].setRelatedData(ObisCodeMap.VOLTAGE_A, Double.toString(v));
					v = (ar[i].getArrayAt(offset++).getUnsignedLong()*1.0 / 100);
					result[i].setRelatedData(ObisCodeMap.VOLTAGE_B, Double.toString(v));
					v = (ar[i].getArrayAt(offset++).getUnsignedLong()*1.0 / 100);
					result[i].setRelatedData(ObisCodeMap.VOLTAGE_C, Double.toString(v));
					
					int c = ar[i].getArrayAt(offset++).getUnsignedLong();
					result[i].setRelatedData(ObisCodeMap.CURRENT_A, Integer.toString(c));
					c = ar[i].getArrayAt(offset++).getUnsignedLong();
					result[i].setRelatedData(ObisCodeMap.CURRENT_B, Integer.toString(c));
					c = ar[i].getArrayAt(offset++).getUnsignedLong();
					result[i].setRelatedData(ObisCodeMap.CURRENT_C, Integer.toString(c));
				}
			}
			return result;
		case STANDAR:
			if(evtCode == 0 )
				evtCode = 0x0100;
		case GRIDEVENT:
			if(evtCode == 0 )
				evtCode = 0x0400;
		case RELAY:
			if(evtCode == 0 )
				evtCode = 0x0300;
		case FRAUD:{
			if(evtCode == 0 )
				evtCode = 0x0200;
			//time+subCode or counts+ subCode
			for(int i=0; i<ar.length; i++ ){
				int structSize = ar[i].getStructureSize();
				if( structSize< 2 )
					return result;
				int offset = 0;
				int subCode = 0;
				DlmsData member = ar[i].getStructureItem(offset++);
				if( member.type() == DlmsDataType.UNSIGNED_LONG ){  //U16
					int count = member.getUnsignedLong();
					member = ar[i].getStructureItem(offset++);
					if( member.type() == DlmsDataType.UNSIGNED )
						subCode = member.getUnsigned();
					else if( member.type() == DlmsDataType.UNSIGNED_LONG )
						subCode = member.getUnsignedLong() & 0xFF ;
					result[i] = new DlmsAlarmItem( (short)(evtCode | subCode) );
					result[i].setRelatedData("count",Integer.toString(count));
				}
				else{
					String occurTime = null;
					try{
						DlmsDateTime dlmsTime = member.getDateTime();
						if( null != dlmsTime )
							occurTime = dlmsTime.toString();
					}catch(Exception e){}
					member = ar[i].getStructureItem(offset++);
					if( member.type() == DlmsDataType.UNSIGNED )
						subCode = member.getUnsigned();
					else if( member.type() == DlmsDataType.UNSIGNED_LONG )
						subCode = member.getUnsignedLong() & 0xFF ;
					result[i] = new DlmsAlarmItem( (short)( evtCode + subCode), occurTime);
				}
			}
			return result;
		}
		/**
		 * {
			   发生时间 Date_time
			   结束时间 Date_time
			   电压（失压时最低电压，过压时最高电压） U16
			}
		 */
		case LOWVA:
			break;
		case LOWVB:
			break;
		case LOWVC:
			break;
		case MBUS:
			break;
		case MAGNETIC://强磁场
		case OPENMETER://开表盖
		case OPENTERMINAL://开端盖
		case PROGRAM://重新编程
		case REVERSE://电流反向
		case POWERDOWN://电网掉电
			System.out.println("OBIS:"+obis+",UnSupport...");
			break;
		case OPTICAL:
			break;
		case OUTAGE:
			break;
		case OVERCURR:
			break;
		case OVERVA:
			break;
		case OVERVB:
			break;
		case OVERVC:
			break;
		case PREPAY:
			break;
		case DETAIL:{  // OBIS: 0-0:99.99.0.255
			break;
		}
		case UNDEF:
		default:
			break;
		}
		return result;
	}
}
