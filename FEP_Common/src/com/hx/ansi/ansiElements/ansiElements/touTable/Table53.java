package com.hx.ansi.ansiElements.ansiElements.touTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.StringUtil;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  Table 53 时间偏移
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:07:37
 * @version 1.0 
 */

public class Table53 extends Table{
	
	private static final Logger log = Logger.getLogger(Table53.class);

	
	public Date DST_TIME_EFF;
	public int DST_TIME_AMT;
	public int TIME_ZONE_OFFSET;
	public Date STD_TIME_EFF;
	
	@Override
	public void decode() {
		
	}
	@Override
	public void decode(String data){
	   SimpleDateFormat sdf= new SimpleDateFormat("HHmmss");
	   try{
		   DST_TIME_EFF=sdf.parse(AnsiDataSwitch.toHexString(data.substring(0, 6)));
		   DST_TIME_AMT=Integer.parseInt(data.substring(6, 8), 16);
		   TIME_ZONE_OFFSET=Integer.parseInt(data.substring(8, 12), 16);
		   DST_TIME_EFF=sdf.parse(data.substring(12, 18));
	   }catch(ParseException e){
		   log.error(StringUtil.getExceptionDetailInfo(e));
	   }
	
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}

	
public static void main(String[] args) {
	//表计读上来的数据是：00 00 00 00
}

}
