package test_.serial;
import java.util.Date;
import java.text.*;

public class GetTime {
	public String GetTime(){
		Date d = new Date();
		// String str = d.toString();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss ");// 其中yyyy-MM-dd是你要表示的格式
		// 可以任意组合，不限个数和次序；具体表示为：MM-month,dd-day,yyyy-year;kk-hour,mm-minute,ss-second;
		String str=sdf.format(d);
		//System.out.println("读取卡片信息时间 : " +str);
		return str;
	}

}
