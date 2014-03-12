/**
 * date OCTET STRING (SIZE(5))
 * 		{
 * 			year highbyte,
 * 			year lowbyte,
 * 			month,
 * 			day of month,
 * 			day of week
 * 		}
 * date_time OCTET STRING (SIZE(12))
{
	year highbyte,
	year lowbyte,
	month,
	day of month,
	day of week,
	hour,
	minute,
	second,
	hundredths of second,
	deviation highbyte,
	deviation lowbyte,
	clock status
}
 * year: interpreted as long-unsigned
 * 		range 0¡­big  0xFFFF = not specified
 * year highbyte and year lowbyte reference the 2 bytes of the long-unsigned
 * month: interpreted as unsigned
 * 		range 1¡­12, 0xFD, 0xFE, 0xFF
 * 		1 is January
 * 		0xFD = daylight_savings_end
 * 		0xFE = daylight_savings_begin
 * 		0xFF = not specified
 * dayOfMonth: interpreted as unsigned
 * 		range 1¡­31, 0xFD, 0xFE, 0xFF
 * 		0xFD = 2nd last day of month
 * 		0xFE = last day of month
 * 		0xE0 to 0xFC = reserved
 * 		0xFF = not specified
 * dayOfWeek: interpreted as unsigned
 * 		range 1¡­7, 0xFF
 * 		1 is Monday
 * 		0xFF = not specified
 * For dayOfMonth and dayOfWeek:
 * 	For repetitive dates, the unused parts must be set to ¡°not specified¡±.
 * 	The elements dayOfMonth and dayOfWeek shall be interpreted together:
 *  - if last dayOfMonth is specified (0xFE) and dayOfWeek is wildcard, this specifies the last calendar day of the month;
	- if last dayOfMonth is specified (0xFE) and an explicit dayOfWeek is specified (for example 7, Sunday) then it is the last occurrence of the weekday specified in the month, i.e. the last Sunday;
	- if the year is not specified (FFFF), and dayOfMonth and dayOfWeek are both explicitly specified, this shall be interpreted as the dayOfWeek on, or following dayOfMonth;
	- if the year and month are specified, and both the dayOfMonth and dayOfWeek are explicitly specified but the values are not consistent it shall be considered as an error.
 */
package com.hx.dlms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DlmsDateTime {
	protected byte[] value;

	public DlmsDateTime() {
		byte notSpec = (byte)0xFF;
		value = new byte[] { notSpec,notSpec,notSpec,notSpec,notSpec, 0,0,0,0, 0,0,0 };
	}
	
	public DlmsDateTime(Date date) {
		this();
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		value[0] = (byte) ( (year>>8)& 0xff);
		value[1] = (byte) ( year & 0xFF ) ;
		value[2] = (byte)( ca.get(Calendar.MONTH)+1 );
		value[3] = (byte) ( ca.get(Calendar.DAY_OF_MONTH) ) ;
		int dayofweek = ca.get(Calendar.DAY_OF_WEEK) - 1;
		if( dayofweek == 0 )
			dayofweek = 7;
		value[4] = (byte) dayofweek;
		value[5] = (byte)( ca.get(Calendar.HOUR_OF_DAY));
		value[6] = (byte)( ca.get(Calendar.MINUTE));
		value[7] = (byte) ca.get(Calendar.SECOND);
		value[8] = (byte) ( ca.get(Calendar.MILLISECOND)/10);
		value[9] = (byte) 0x80;
		value[10] = 0;
		value[11] = 0;
	}
	/**include date saving active*/
	public DlmsDateTime(Date date,boolean isDayLightSavingActive){
		this(date);
		value[11] = isDayLightSavingActive?(byte)0x80:0;
	}
	/**include date saving active*/
	public DlmsDateTime(String date,boolean isDayLightSavingActive){
		this(date);
		value[11]=isDayLightSavingActive?(byte)0x80:0;
	}
	
	public DlmsDateTime( DlmsData data ) {
		this();
		if( data.identifier() == DlmsDataType.DATE_TIME || data.identifier() == DlmsDataType.DATE || data.identifier() == DlmsDataType.TIME )
			setDlmsDataValue(data.getValue(),0);
		else
			throw new RuntimeException("DlmsData type incorrect:" + data.getTypeName() );
	}
	/**
	 * format:yyyy-MM-dd HH:mm:ss
	 * @param dateTime
	 */
	public DlmsDateTime(String dateTime) {
		this();
		byte any = (byte) 0xFF;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		try {
			d=sdf.parse(dateTime);
		} catch (ParseException e) {
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		String strYear = dateTime.substring(0,4);
		if("FFFF".equals(strYear))
		{
			value[0] =any;
			value[1] =any;
		}else{
			int year = Integer.parseInt(strYear);
			value[0] = (byte) ( (year>>8)& 0xff);
			value[1] = (byte) ( year & 0xFF ) ;
		}
		String strMonth = dateTime.substring(5,7);
		if("FF".equals(strMonth)){
			value[2]=any;
		}else{
			int month = Integer.parseInt(strMonth);
			value[2] = (byte)( month );
		}
		String strDay = dateTime.substring(8,10);
		if("FF".equals(strDay)){
			value[3]=any;
		}else{
			int day = Integer.parseInt(strDay);
			value[3] = (byte) (day) ;
		}

		int dayofweek = ca.get(Calendar.DAY_OF_WEEK) - 1;
		if( dayofweek == 0 )
			dayofweek = 7;
		value[4] = (byte) dayofweek;
		
		String strHour = dateTime.substring(11,13);
		if("FF".equals(strHour)){
			value[5]=any;
		}else{
			int hour = Integer.parseInt(strHour);
			value[5] = (byte)( hour);
		}
		
		String strMinute = dateTime.substring(14, 16);
		if("FF".equals(strMinute)){
			value[6]= any;
		}else{
			int minute = Integer.parseInt(dateTime.substring(14,16));
			value[6] = (byte)( minute);
		}
		String strSecond = dateTime.substring(17, 19);
		if("FF".equals(strSecond)){
			value[7] = any;
		}else{
			int second = Integer.parseInt(dateTime.substring(17,19));
			value[7] = (byte) (second);
		}
		
		value[8] =  0;
		value[9] = (byte) 0x80;
		value[10] = 0;
		value[11] = 0;
	}

	public void setDlmsDataValue(byte[] dlmsDataValue, int offset ) {
		int index = offset;
		int index0 = 0;
		int len = dlmsDataValue.length - offset ;
		if( len == 4 ){
			//DLMS TIME type
			index0 +=5;
		}
		for(int i=0 ; i< len ; i++ ){
			value[i+index0] = dlmsDataValue[index+i];
		}
	}

	public final Date getDate() {
		int year = ((0xFF & value[0])<< 8) | (0xFF&value[1]);
		int month = 0xFF & value[2];
		int dayofmonth = 0xFF & value[3];
		if( year == 0xFFFF || month > 12 || dayofmonth> 31 ){
			//ANY year
			return null;
		}
		month = month -1;
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.MONTH,month);
		ca.set(Calendar.DAY_OF_MONTH, dayofmonth);
		ca.set(Calendar.HOUR_OF_DAY, 0xFF & value[5]);
		ca.set(Calendar.MINUTE, 0xFF & value[6] );
		ca.set(Calendar.SECOND, 0xFF & value[7]);
		ca.set(Calendar.MILLISECOND, (0xFF & value[8])*10 );
		return ca.getTime();
	}
	
	public byte[] getDateValue() {
		byte[] result = new byte[5];
		for(int i=0; i<result.length; i++)
			result[i] = value[i];
		return result;
	}
	
	public byte[] getTimeValue() {
		byte[] result = new byte[4];
		for(int i=0; i<result.length; i++ )
			result[i] = value[i+5];
		return result;
	}
	
	public byte[] getDateTimeValue() {
		byte[] result = new byte[value.length];
		for(int i=0; i<result.length; i++)
			result[i] = value[i];
		return result;
	}
	
	public void setLastDayofMonth() {
		value[3] = (byte)0xFE;
	}
	
	public void setDayofWeek(int dayofweek ) {
		dayofweek -= 1;
		if( dayofweek == 0 )
			dayofweek = 7;
		value[4] = (byte)dayofweek;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int year = ((0xFF & value[0])<< 8) | (0xFF&value[1]);
		int month = 0xFF & value[2];
		int dayofmonth = 0xFF & value[3];
		int dayofweek = 0xFF & value[4];
		int hour = 0xFF & value[5];
		int minute = 0xFF & value[6];
		int second = 0xFF & value[7];
		int millisecond = (0xFF & value[8])*10;
		
		boolean onlyTime = (year == 0xFFFF && month == 0xFF && dayofmonth == 0xFF && dayofweek == 0xFF) ;
		boolean onlyDate = ( hour == 0 && minute == 0 && second == 0 && millisecond == 0 );
		//populate date
		if( ! onlyTime ){
			if(year!=0xFFFF){
				String strYear = ""+year;
				if(strYear.length()<4){
					sb.append("0000".substring(0, 4-strYear.length())+year).append("-");					
				}else{
					sb.append(year).append("-");
				}
			}else{
				sb.append("FFFF-");
			}
			if(month!=0xFF){
				if( month<10 )
					sb.append("0");
				sb.append(month).append("-");
			}else{
				sb.append("FF-");
			}
			if(dayofmonth!=0xFF){
				if( dayofmonth<10 )
					sb.append("0");
				sb.append(dayofmonth);
			}else{
				sb.append("FF");
			}
		}
		//TIME
		if( (! onlyDate) || (onlyDate && onlyTime) ){
			if( sb.length()>0 )
				sb.append(" ");
			if(hour!=0xFF){
				if( hour<10 )
					sb.append("0");
				sb.append(hour).append(":");
			}
			if(minute!=0xFF){
				if( minute<10 )
					sb.append("0");
				sb.append(minute).append(":");
			}
			if(second!=0xFF){
				if( second<10 )
					sb.append("0");
				sb.append(second);
			}else{
				sb.deleteCharAt(sb.length()-1);
			}
		}
		return sb.toString();
	}

}
