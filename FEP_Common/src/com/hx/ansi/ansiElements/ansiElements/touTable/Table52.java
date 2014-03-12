package com.hx.ansi.ansiElements.ansiElements.touTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.StringUtil;

import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.ParserHex;

/** 
 * @Description  时钟,Table52提供表计实时时钟的信息，该表只读。设置时钟通过过程10实现。
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:07:30
 * @version 1.0 
 */

public class Table52 extends Table{
	
	private static final Logger log = Logger.getLogger(Table52.class);

	
	public Date CLOCK_CALENDAR;
	public String meterDate;
	public int TIME_DATE_QUAL ;
	public boolean daylight=false;
	public boolean correction=false;
	public boolean timeZone=false;
	public boolean timeZoneOffset=false;
	public boolean isDaylight=false;
	public int week;
	
	@Override
	public void decode() {
		//由于海兴的表计STD_VERSION_NO=2所以此处bit7有含义。
		daylight=((TIME_DATE_QUAL&128)>>7)==0?false:true;
		correction=((TIME_DATE_QUAL&64)>>6)==0?false:true;
		timeZone=((TIME_DATE_QUAL&32)>>5)==0?false:true;
		timeZoneOffset=((TIME_DATE_QUAL&16)>>4)==0?false:true;
		isDaylight=((TIME_DATE_QUAL&8)>>3)==0?false:true;
		week=TIME_DATE_QUAL&7;
	}
	@Override
	public void decode(String data){
		//从表计读上来的数据是：0D 05 03 14 38 2B 04 
//		CLOCK_CALENDAR=new Date();
		SimpleDateFormat sdf= new SimpleDateFormat("yyMMddHHmmss");
		SimpleDateFormat sd= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			CLOCK_CALENDAR=sdf.parse(ParserHex.parseValue(data, 12));
			meterDate=sd.format(CLOCK_CALENDAR);
		} catch (ParseException e) {
			log.error(StringUtil.getExceptionDetailInfo(e));
		}
		TIME_DATE_QUAL=Integer.parseInt(data.substring(12, 14), 16);
		decode();
	}
	@Override
	public void encode() {
		
	}
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		if(table instanceof Table52){
			Table52 table52 = (Table52) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode);
			switch(icode){
			case 5200:
				ansiDataItem.resultData=table52.meterDate;
				break;
			
			}
		}else{
			System.out.println("错误的table参数");
		}
		return ansiDataItem;
	}
	
	
	public static void main(String[] args) {
		Table52 t52=new Table52();
		t52.decode("0D050314382B04");
	}
}
