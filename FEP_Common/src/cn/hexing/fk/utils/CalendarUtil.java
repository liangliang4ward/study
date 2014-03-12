package cn.hexing.fk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class CalendarUtil {

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat shortDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat shortTimeFormat = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
	private static final SimpleDateFormat milliFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    /**
     * 取得当天的最早时间
     * @return 当前日期
     */
    public static Calendar getBeginOfToday() {
        Calendar date = Calendar.getInstance();
        clearTimePart(date);
        return date;
    }
    
    /**
     * 取得当天的最晚时间
     * @return 当前日期
     */
    public static Calendar getEndOfToday() {
        Calendar date = Calendar.getInstance();
        setLastTimeOfDay(date);
        return date;
    }
    
    /**
     * 清除日期的时间部分，即把时、分、秒、毫秒部分清零
     * @param date 需要清除时间的日期
     * @return 清除了时间部分后的日期
     */
    public static Calendar clearTimePart(Calendar date) {
        //date.clear(Calendar.HOUR);		//clear方法有问题，只是掩盖，并没有实际清零。by yangdh---2007/03/22
        date.set(Calendar.HOUR_OF_DAY,0);
        date.set(Calendar.MINUTE,0);
        date.set(Calendar.SECOND,0);
        date.set(Calendar.MILLISECOND,0);
        //date.clear(Calendar.AM_PM);
        return date;
    }
    
    /**
     * 设置日期的时间部分为当天的最后时间，即 23:59:59.999
     * @param date 需要设置时间的日期
     * @return 设置了时间后的日期
     */
    public static Calendar setLastTimeOfDay(Calendar date) {
        date.set(Calendar.AM_PM, Calendar.PM);
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 999);
        return date;
    }
    
    /**
     * 取得某一年的第一天
     * @param year 年度
     * @return 某一年的第一天。如 2000-1-1 00:00:00.000
     */
    public static Calendar getFirstDayOfYear(int year) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DATE, 1);
        clearTimePart(date);
        
        return date;
    }
    
    /**
     * 取得某一年的最后一天
     * @param year 年度
     * @return 某一年的最后一天。如 2000-12-31 23:59:59.999
     */
    public static Calendar getLastDayOfYear(int year) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, 11);
        date.set(Calendar.DATE, 31);
        date.set(Calendar.HOUR, 11);
        setLastTimeOfDay(date);
        
        return date;
    }
    
    /**
     * 解析日期字符串。日期格式可以是 yyyy-MM-dd HH:mm:ss、yyyy-MM-dd HH:mm、yyyy-MM-dd、
     * HH:mm:ss、HH:mm 中的任何一种
     * @param val 日期字符串
     * @return Calendar 对象
     */
    public static Calendar parse(String val) {
        if (val == null) {
            return null;
        }
        
        try {
            Date date = null;
            String s = val.trim();
            int indexOfDateDelim = s.indexOf("-");
            int indexOfTimeDelim = s.indexOf(":");
            int indexOfTimeDelim2 = s.indexOf(":", indexOfTimeDelim + 1);
            if (indexOfDateDelim < 0 && indexOfTimeDelim > 0) {
                if (indexOfTimeDelim2 > 0) {
                    date = timeFormat.parse(s);
                }
                else {
                    date = shortTimeFormat.parse(s);
                }
            }
            else if (indexOfDateDelim > 0 && indexOfTimeDelim < 0) {
                date = dateFormat.parse(s);
            }
            else {
                if (indexOfTimeDelim2 > 0) {
                    date = dateTimeFormat.parse(s);
                }
                else {
                    date = shortDateTimeFormat.parse(s);
                }
            }
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException(val + " is invalid date format");
        }
    }
    
    /**
     * 计算一个日期的时间部分的毫秒数
     * @param time 时间
     * @return 时间的毫秒数
     */
    public static long getTimeMillis(Calendar time) {
        if (time == null) {
            return 0L;
        }	
        //by yangdh  2007-11-19  显示转换int to long
        return ((long)time.get(Calendar.HOUR_OF_DAY) * 3600000L
                + (long)time.get(Calendar.MINUTE) * 60000L
                + (long)time.get(Calendar.SECOND) * 1000L
                + (long)time.get(Calendar.MILLISECOND));
    }
    
    /**
     * 比较两个日程时间部分的大小
     * @param time1 时间1
     * @param time2 时间2
     * @return -1, 0 或 1，如果 time1 < time2, time1 == time2 或 time1 > time2
     */
    public static int compareTime(Calendar time1, Calendar time2) {
        long ms1 = getTimeMillis(time1);
        long ms2 = getTimeMillis(time2);
        if (ms1 == ms2) {
            return 0;
        }
        else if (ms1 > ms2){
            return 1;
        }
        else {
            return -1;
        }
    }
    
    /**
     * 解析日期字符串。日期格式可以是 yyyy-MM-dd HH:mm:ss、yyyy-MM-dd HH:mm、yyyy-MM-dd、
     * HH:mm:ss、HH:mm 中的任何一种
     * @param val 日期字符串
     * @param defaultValue 缺省值。如果 val 非法，则使用该值
     * @return Calendar 对象
     */
    public static Calendar parse(String val, Calendar defaultValue) {
        try {
            return parse(val);
        }
        catch (Exception ex) {
            return defaultValue;
        }
    }
    
    public static String getTimeString(long milliseconds){
    	Date date = new Date(milliseconds);
    	return timeFormat.format(date);
    }
    
    public static String getDateTimeString(long milliseconds ){
    	Date date = new Date(milliseconds);
    	SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return dtf.format(date);
    }

    public static String getDateString(long milliseconds){
    	Date date = new Date(milliseconds);
    	return dateFormat.format(date);
    }
    
    public static String getFullDateTimeString(long milliseconds ){
    	Date date = new Date(milliseconds);
    	return fullFormat.format(date);
    }

    public static String getMilliDateTimeString(long milliseconds ){
    	Date date = new Date(milliseconds);
    	return milliFormat.format(date);
    }
}
