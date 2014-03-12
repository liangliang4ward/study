/**
 * Type GeneralizedTime takes values of the year, month, day, hour, time, minute,second, 
 * and second fraction in any of three forms.
 * ASN.1 definition:  YYYYMMDDhhmm[ss[.s...]]{Z|+hhmm|-hhmm}
 * 
 * 1. Local time only. 'YYYYMMDDHHMMSS.fff', where the optional fff is 
 *     accurate to three decimal places. 
 * 2. Universal time (UTC time) only. 'YYMMDDHHMMSS.fffZ'
 * 3. Difference between local and UTC times. 'YYYYMMDDHHMMSS.fff+-HHMM'
 * 
 * 	11.7 GeneralizedTime
	11.7.1 The encoding shall terminate with a "Z"
	11.7.2 The fractional-seconds elements, if present, shall omit all trailing
		0's; if the elements correspond to 0, they shall be wholly omitted, and the
		decimal point element also shall be omitted.
	11.7.3 The decimal point element, if present, shall be the point option ".".
	11.7.4 Midnight (GMT) shall be represented in the form: "YYYYMMDD000000Z"
		where "YYYYMMDD" represents the day following the midnight in question.
	11.7.5 Examples of valid representations
		"19920521000000Z"
		"19920622123421Z"
		"19920722132100.3Z"
 */
package com.hx.dlms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.StringUtil;

public class ASN1GeneralizedTime extends ASN1String {
	private static final long serialVersionUID = 7382862055034082336L;
	private static final Logger log = Logger.getLogger(ASN1GeneralizedTime.class);

	public ASN1GeneralizedTime(){
		super(ASN1Constants.TAG_GENERALIZEDTIME,-1);
	}
	
	public ASN1GeneralizedTime(Date initTime){
		this();
		setTime(initTime);
	}
	
	public ASN1GeneralizedTime(String strTime){
		this();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		try {
			setTime(sdf.parse(strTime));
		} catch (ParseException e) {
			log.error(StringUtil.getExceptionDetailInfo(e));
		}
	}
	
	
	/**
	 * The date uses default TimeZone, it is said that this is local time.
	 * @param date
	 */
	public void setTime(Date date){
		Date gmtDate = new Date(date.getTime()+TimeZone.getDefault().getRawOffset());
		Calendar cl = Calendar.getInstance();
		cl.setTime(gmtDate);
		SimpleDateFormat ztimeFormat = new SimpleDateFormat("yyyyMMddHHmmss.S'Z'");
		value = ztimeFormat.format(gmtDate).getBytes();
	}
	
	private int strToInt(int offset,int len){
        int c;
        int result = 0;
        for (int i = offset, end = offset + len; i < end; i++) {
            c = value[i] - 48;
            if (c < 0 || c > 9) {
                throw new RuntimeException("GeneralTime convert exception,format error."); //$NON-NLS-1$
            }
            result = result * 10 + c;
        }
        return result;
	}
	
	public String getStringTime(){
		if( null == value )
			return null;
		return new String(value);
	}
	
	public Date getTime(){
		if( null == value )
			return null;
		if( log.isDebugEnabled() ){
			String strTime = new String(value);
			log.debug("decoded GeneralTime="+strTime);
		}
		//YYYYMMDDhhmmss[.s...]{Z|+hhmm|-hhmm}
		int offset = 0;
		int year = strToInt(offset,4);
		offset += 4;
		int month = strToInt(offset,2);
		offset += 2;
		int day = strToInt(offset,2);
		offset += 2;
		int hour = strToInt(offset,2);
		offset += 2;
		int minute = strToInt(offset,2);
		offset += 2;
		int second = strToInt(offset,2);
		offset += 2;
		int millisecond = 0;
		boolean gmtTime = false;
		while( value.length > offset ){
			if( value[offset] == '.' ){
				//find millisecond
				offset++;
				int maxCnt = 3;
				int n = 0;
				while(offset<value.length && maxCnt-->0 ){
					n = value[offset] - 48;
					if( n<0 || n>9 )
						break;
					millisecond = millisecond*10 + n;
					offset++;
				}
				continue;
			}
			if( value[offset] == 'Z' ){
				gmtTime = true;
				break;
			}
			if( value[offset]=='+' ){
				throw new RuntimeException("not supported yet");
			}
			else if( value[offset] == '-' ){
				throw new RuntimeException("not supported yet");
			}
			break;
		}
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, millisecond);
        long local = c.getTimeInMillis();
		if( gmtTime ){
			local = c.getTimeInMillis() + TimeZone.getDefault().getRawOffset();
		}
		return new Date(local);
	}
}
