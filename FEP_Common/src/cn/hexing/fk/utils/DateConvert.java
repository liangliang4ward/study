package cn.hexing.fk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 日历转换工具
 * iran <==> gregorian
 * @author Administrator
 *
 */
public class DateConvert {

	public static int[] IranDayTab = {0,0,31,62,93,124,155,186,216,246,276,306,336};
	public static int[] IranYearDay = {
			0, 0, 366, 731, 1096, 1461, 1827, 2192, 2557,
			2922, 3288, 3653, 4018, 4383, 4749, 5114, 5479, 5844, 6210, 6575,
			6940, 7305, 7671, 8036, 8401, 8766, 9132, 9497, 9862, 10227, 10592,
			10958, 11323, 11688, 12053, 12419, 12784, 13149, 13514, 13880,
			14245, 14610, 14975, 15341, 15706, 16071, 16436, 16802, 17167,
			17532, 17897, 18263, 18628, 18993, 19358, 19724, 20089, 20454,
			20819, 21185, 21550, 21915, 22280, 22645, 23011, 23376, 23741,
			24106, 24472, 24837, 25202, 25567, 25933, 26298, 26663, 27028,
			27394, 27759, 28124, 28489, 28855, 29220, 29585, 29950, 30316,
			30681, 31046, 31411, 31777, 32142, 32507, 32872, 33238, 33603,
			33968, 34333, 34698, 35064, 35429, 35794, 36159
	};
	public static int[] DayTab = {
		    0,0,31,59,90,120,151,181,212,243,273,304,334
	};

	public static int[] DayTabR = {
	    0,0,31,60,91,121,152,182,213,244,274,305,335
	};
//	---------------------------------------------------------------------------------------------------------------//
//	函数功能：根据日月年计算出伊朗历的日数
//	入口参数：伊朗日历日起始地址
//	---------------------------------------------------------------------------------------------------------------//
	public static int TOU_GetGmDay_IRAN(String IAddr ){
	    int Day;
	    int Mon,Year;
	    
	    Mon = Integer.parseInt(IAddr.substring(2,4));
		Year= Integer.parseInt(IAddr.substring(0,2));
		if ( Year < 79 ){
	        Year = 100 + Year;
		}
		Day = Integer.parseInt(IAddr.substring(4,6))-1 + IranDayTab[Mon] + IranYearDay[Year-79+1];
	   	return (Day);
	}
//	--------------------------------------------------------------------
//	得到当前的日
//	--------------------------------------------------------------------
	public static int TOU_GetGmDay( String GAddr )
	{
	    int Day,Mon,Year;

	    Mon = Integer.parseInt(GAddr.substring(2,4));
		Year= Integer.parseInt(GAddr.substring(0,2));
		Day = Integer.parseInt(GAddr.substring(4,6))-1 + DayTab[Mon] + Year*365 + (Year>>2) + 1;
	   
	    if ( (Year&0x03)==0 && Mon<=2 ) Day--;
	    return Day;
	}
	/*

//---------------------------------------------------------------------------------------------------------------//
//函数功能：根据日月年计算出伊朗历的小时数
//入口参数：伊朗日历时起始地址,但是需要把中间的周去掉
//---------------------------------------------------------------------------------------------------------------//
	public long TOU_GetGmHour_IRAN(char[] Addr)
	{
		return ( (long)BF_BCD_Byte(*(Addr)) + (unsigned long)TOU_GetGmDay_IRAN(Addr+1)*24);
	} 

//---------------------------------------------------------------------------------------------------------------//
//函数功能：根据日月年时分计算出当前的分数
//入口参数：伊朗日历分起始地址
//---------------------------------------------------------------------------------------------------------------//
	public long TOU_GetGmMin_IRAN(String IAddr)
	{
		return ( (unsigned long)BF_BCD_Byte(*(Addr)) + (unsigned long)BF_BCD_Byte(*(Addr+1))*60 +
			     (unsigned long)TOU_GetGmDay_IRAN(Addr+3)*24*60);
	} 
*/
//	---------------------------------------------------------------------------------------------------------------//
//	函数功能：根据公历计算出来的日数得到伊朗日历的年月日
//	---------------------------------------------------------------------------------------------------------------//
	public static String TOU_Days_Date_IRAN(int days ){
		String IAddr = "";
	    int  i,tmp;
	    //Year
	    for ( i=100; i>0; i-- ) 
	    {
	    	if ( days >= IranYearDay[i] )	 
	            break;	
	    }
	    tmp = 78+i;
	    if (tmp >= 100 )
		{
	        tmp -= 100;
		}
	    String stemp = "" +tmp;
	    stemp = "00".substring(0, 2 - stemp.length()) + stemp;
	    IAddr = stemp;
	    //Month
	    days -= IranYearDay[i];	    
	    for ( i=12; i>0; i-- ) 
	    {
	    	if ( days >= IranDayTab[i] )	   
	            break;
	    }
	    stemp = "00".substring(0, 2 - (""+i).length()) + (""+i);
	    IAddr = IAddr + stemp;
	    //Day
	    days -= IranDayTab[i];
	    stemp = "" + (days + 1);
	    stemp = "00".substring(0, 2 - stemp.length()) + stemp;
	    IAddr = IAddr + stemp;
	    
	    return IAddr;
	}
//	---------------------------------------------------------------------------------------------------------------//
//	函数功能：根据日数得到公历的年月日
//	---------------------------------------------------------------------------------------------------------------//
	public static String TOU_Days_Date_G(int days)
	{
		String GAddr = "";
		int Tmp;
		int Y1,Y2;
		String stemp = "";

	    Tmp = days;
	    
	    Y1 = Tmp / (365*4+1);
	    Y2 = Tmp % (365*4+1);
	    if ( Y2 >= 366 )
	    {
	    	stemp = "" + (Y1*4 + (Y2-1)/365);       // Year
	       // *(Day+2) = Y1*4 + (Y2-1)/365;       // Year	    	
	        Y2 = (Y2-1)%365;
	    }
	    else 
	    	stemp = "" + (Y1*4);
	    GAddr = stemp;
	    
	    if ((Integer.parseInt(stemp)&0x03)!=0x00)
		{
		    for (Y1=12;Y1>0;Y1--)
		        if ( (Y2>=DayTab[Y1]) )
		            break;
			stemp = "" + Y1;                  	// Month			
		    stemp = "00".substring(0, 2 - stemp.length()) + stemp;
		    GAddr = GAddr + stemp;
			stemp = "" + (Y2 +1 - DayTab[Y1]);      // Day		
		    stemp = "00".substring(0, 2 - stemp.length()) + stemp;
			GAddr = GAddr + stemp;
		}
		else
		{
			for (Y1=12;Y1>0;Y1--)
		        if ( (Y2>=DayTabR[Y1]) )
		            break;
			stemp = "" + Y1;                  	// Month		
		    stemp = "00".substring(0, 2 - stemp.length()) + stemp;
		    GAddr = GAddr + stemp;
			stemp = "" + (Y2 +1 - DayTabR[Y1]);     // Day
		    stemp = "00".substring(0, 2 - stemp.length()) + stemp;
			GAddr = GAddr + stemp;
		}   
	    return GAddr;
	}

//	---------------------------------------------------------------------------------------------------------------//
//	函数功能：把公历转换成伊朗历
//	只是把日期转换了一下，时分不需要转换
//	入口参数：GAddr----公历时间地址；带入的地址为秒；
//				IAddr----伊朗历时间地址；带入的地址为秒；
//	---------------------------------------------------------------------------------------------------------------//
	public static String TOU_GregorianToIRAN(String GAddr)
	{
		String IAddr = "";
	    int  Days;	    
	    Days = TOU_GetGmDay(GAddr.substring(0,6));//计算得到当前公历下的日数，前提是当前年总是在2000年之后									

	    if (( Days >=79 )&&(Days<36525))
	    {        
	        Days -= 79;
	         
	        IAddr = TOU_Days_Date_IRAN( Days );				//根据当前日数转换成伊朗历
//	      把当前周时分秒搬到伊朗日历里去
			if(Integer.parseInt(GAddr.substring(6,8)) == 0){
	        	IAddr = IAddr + "07";
	        }else {
	        	IAddr = IAddr + GAddr.substring(6,8);
	        }	        	
	        IAddr = IAddr + GAddr.substring(8,14);
	
	    }
		else								//如果公历的日期出错，则转换之后的伊朗日历的日期为全0
		{
//			把当前时分秒搬到伊朗日历里去
			IAddr = "00000001" +  GAddr.substring(8,14);	    	
		}
	    return IAddr;
	}

//	---------------------------------------------------------------------------------------------------------------//
//	函数功能：把伊朗历转换成公历
//	入口参数：GAddr----公历时间地址；带入的地址为秒；
//				IAddr----伊朗历时间地址；带入的地址为秒；
//	---------------------------------------------------------------------------------------------------------------//
	
	public static String TOU_IRANToGregorian( String IAddr)
	{
		String GAddr = "";	
	    int  Days;	    
	    Days = TOU_GetGmDay_IRAN(IAddr.substring(0,6))+79;
	    if (Days<36525)
	    {
	    	GAddr = TOU_Days_Date_G(Days );
	    	if(Integer.parseInt(IAddr.substring(6,8)) == 0){
	    		GAddr = GAddr + "07";
	        }else {
	        	GAddr = GAddr + IAddr.substring(6,8);
	        }	        	
	    	GAddr = GAddr + IAddr.substring(8,14);	   
	    }
		else									//if have error, then the Gregorian is 0
		{
			GAddr = "00000001" +  IAddr.substring(8,14);	
		}
	    if(GAddr.length()==13){
	    	GAddr="0"+GAddr;
	    }
	return GAddr;
	}
	/**
	 * 伊朗历转换公历
	 * @param gregorianTime  --伊朗历时间
	 * @param formatStr		 --格式化String   yyyy-MM-dd HH:mm:ss ||yyyy-MM-dd  ...
	 * @return
	 */
	@Deprecated
	public static String iranToGregorian(String iranDateTime,String formatStr){
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		
		Date date = null;
		try {
			date=sdf.parse(iranDateTime);
			Date gregorian=iranToGregorian(date);
			return sdf.format(gregorian);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 公历转换伊朗历
	 * @param gregorianTime  --公历时间
	 * @param formatStr		 --格式化String   yyyy-MM-dd HH:mm:ss ||yyyy-MM-dd  ...
	 * @return
	 */
	@Deprecated
	public static String gregorianToIran(String gregorianTime,String formatStr){
		
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		
		Date date = null;
		try {
			date=sdf.parse(gregorianTime);
			Date gregorian=gregorianToIran(date);
			return sdf.format(gregorian);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	/**
	 * 伊朗历转换为公历
	 */
	@Deprecated
	public static Date iranToGregorian(Date iranDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String iranTime=sdf.format(iranDate);
		
		
		iranTime = iranTime.substring(2, 4) + iranTime.substring(5, 7)
				+ iranTime.substring(8, 10) + "01" + iranTime.substring(11, 13)
				+ iranTime.substring(14, 16) + iranTime.substring(17, 19);
		iranTime = TOU_IRANToGregorian(iranTime);
		String greTime = "20" + iranTime.substring(0, 2) + "-"
				+ iranTime.substring(2, 4) + "-" + iranTime.substring(4, 6)
				+ " " + iranTime.substring(8, 10) + ":"
				+ iranTime.substring(10, 12) + ":" + iranTime.substring(12, 14);
		Date greDate = null;
		try {
			greDate = sdf.parse(greTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return greDate;
	}
	@Deprecated
	public static Date gregorianToIran(Date gregorianDate) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sdf.format(gregorianDate);
		time = time.substring(2,4) + time.substring(5,7) + time.substring(8,10)
            	+ "01" + time.substring(11,13)+ time.substring(14,16)+ time.substring(17,19);
	    time = TOU_GregorianToIRAN(time);
	    if (Integer.parseInt(time.substring(0,2)) >= 89){
	    	time = "13" + time;
	    }else{
	    	time = "14" + time;
	    }
	    time = time.substring(0,4) + "-" + time.substring(4,6) + "-" + time.substring(6,8)
            	+ " " + time.substring(10,12) + ":" + time.substring(12,14) + ":" + time.substring(14,16);
	    Date iranDate = null;
		try {
			iranDate = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return iranDate;
	}
	/**
	 * 伊朗历转换为公历
	 * @param iranTime
	 * @return
	 */
	public static String iranToGregorian(String iranTime){
		boolean withoutTime = false;
		if(iranTime.length() == 10){
			withoutTime = true;
			iranTime+=" 00:00:00";
		}
		if(iranTime.substring(0, 10).equals("0000-00-00")) return "0000-00-00 00:00:00";
		iranTime = iranTime.substring(2, 4) + iranTime.substring(5, 7)
				+ iranTime.substring(8, 10) + "01" + iranTime.substring(11, 13)
				+ iranTime.substring(14, 16) + iranTime.substring(17, 19);
		iranTime = DateConvert.TOU_IRANToGregorian(iranTime);
		String time = "20" + iranTime.substring(0, 2) + "-"
				+ iranTime.substring(2, 4) + "-" + iranTime.substring(4, 6)
				+ " " + iranTime.substring(8, 10) + ":"
				+ iranTime.substring(10, 12) + ":" + iranTime.substring(12, 14);
		if(withoutTime){
			time = time.substring(0, 10);
		}
		return time;
	}
	/**
	 * 公历转换为伊朗历
	 * @param time
	 * @return
	 */
	public static String gregorianToIran(String time){
		boolean withoutTime = false;
		if(time.length() == 10){
			withoutTime = true;
			time+=" 00:00:00";
		}
		time = time.substring(2,4) + time.substring(5,7) + time.substring(8,10)
            	+ "01" + time.substring(11,13)+ time.substring(14,16)+ time.substring(17,19);
	    time = DateConvert.TOU_GregorianToIRAN(time);
	    if (Integer.parseInt(time.substring(0,2)) >= 89){
	    	time = "13" + time;
	    }else{
	    	time = "14" + time;
	    }
	    time = time.substring(0,4) + "-" + time.substring(4,6) + "-" + time.substring(6,8)
            	+ " " + time.substring(10,12) + ":" + time.substring(12,14) + ":" + time.substring(14,16);
		if(withoutTime){
			time = time.substring(0, 10);
		}
	    return time;
	}
	
	public static void main(String[] args) {
		
		String str2=iranToGregorian("1387-02-31 00:00:01");
		System.out.println(str2);
		String str3=gregorianToIran("2012-05-20");
		System.out.println(str3);
	}
	
}
