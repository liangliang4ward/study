package com.hx.ansi.ansiElements.ansiElements.touTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.StringUtil;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;
import com.hx.ansi.parse.ParserHex;

/** 
 * @Description  时钟状态
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:07:59
 * @version 1.0 
 */

public class Table55 extends Table{
	
	private static final Logger log = Logger.getLogger(Table55.class);

	
	public Date CLOCK_CALENDAR;
	public int TIME_DATE_QUAL ;
	public boolean daylight=false;
	public boolean correction=false;
	public boolean timeZone=false;
	public boolean timeZoneOffset=false;
	public boolean isDaylight=false;
	public int week;
	public int status;//包含有一些特殊信息
	
	
	@Override
	public void decode() {
		//由于海兴的表计STD_VERSION_NO=2所以此处bit7有含义。
		daylight=((TIME_DATE_QUAL&128)>>7)==0?false:true;
		correction=((TIME_DATE_QUAL&64)>>6)==0?false:true;
		timeZone=((TIME_DATE_QUAL&32)>>5)==0?false:true;
		timeZoneOffset=((TIME_DATE_QUAL&16)>>4)==0?false:true;
		isDaylight=((TIME_DATE_QUAL&8)>>3)==0?false:true;
		week=TIME_DATE_QUAL&7;
		//....
	}
	@Override
	public void decode(String data){
		//从表计读上来的数据是：0D 05 04 00 09 34 05 00 1E  
//		CLOCK_CALENDAR=new Date();
		SimpleDateFormat sdf= new SimpleDateFormat("yyMMddHHmmss");
		try {
			CLOCK_CALENDAR=sdf.parse(ParserHex.parseValue(data, 12));
		} catch (ParseException e) {
			log.error(StringUtil.getExceptionDetailInfo(e));
		}
		TIME_DATE_QUAL=Integer.parseInt(data.substring(12, 14), 16);
		status=Integer.parseInt(data.substring(14, 18), 16);
		decode();
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
			//从表计读上来的数据：0D 05 04 00 09 34 05 00 1E 
	}


}
