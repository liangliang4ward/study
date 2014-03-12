package com.hx.ansi.ansiElements.ansiElements.touTable;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;


/** 
 * @Description  TOU实际配置表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:07:24
 * @version 1.0 
 */

public class Table51 extends Table{
	//table 大元素用的都是按照规约中的大写来标示，而对于decode完的数据用小写完成。
	public int TIME_FUNC_FLAG1;
	public boolean anchor=false;
	public boolean sequenceTime=false;
	public boolean seasonChange=false;
	public boolean demandReset=false;
	public boolean freeze=false;
	public boolean TOUfreeze=false;
	public int TIME_FUNC_FLAG2;
	public boolean offsetT53=false;
	public boolean orderByTime=false;
	public boolean rateMeasure=false;
	public boolean weekDay=false;
	public boolean TOUSummer=false;
	public int CALENDAR_FUNC;
	public int specialSchedule;//1
	public int numSeasons;//4
	public int NBR_NON_RECURR_DATES;//非周期性执行表的最大个数0
	public int NBR_RECURR_DATES;//周期性执行表的最大个数40
	public int NBR_TIER_SWITCHES;//支持的最大日时段数为50
	public int CALENDAR_TBL_SIZE;//通信时，table54的最大字节数为286
	
	
	@Override
	public void decode() {
		anchor=((TIME_FUNC_FLAG1&32)>>5)==0?false:true;
		sequenceTime=((TIME_FUNC_FLAG1&16)>>4)==0?false:true;
		seasonChange=((TIME_FUNC_FLAG1&8)>>3)==0?false:true;
		demandReset=((TIME_FUNC_FLAG1&4)>>2)==0?false:true;
		freeze=((TIME_FUNC_FLAG1&2)>>1)==0?false:true;
		TOUfreeze=(TIME_FUNC_FLAG1&1)==0?false:true;
		
		offsetT53=((TIME_FUNC_FLAG2&16)>>4)==0?false:true;
		orderByTime=((TIME_FUNC_FLAG2&8)>>3)==0?false:true;
		rateMeasure=((TIME_FUNC_FLAG2&4)>>2)==0?false:true;
		weekDay=((TIME_FUNC_FLAG2&2)>>1)==0?false:true;
		TOUSummer=(TIME_FUNC_FLAG2&1)==0?false:true;
		
		specialSchedule=(CALENDAR_FUNC&240)>>>4;
		numSeasons=CALENDAR_FUNC&15;
	}
	@Override
	public void decode(String data){
		//表计读上来的数据：00 00 14 00 28 32 00 1E 01
		TIME_FUNC_FLAG1=Integer.parseInt(data.substring(0, 2), 16);
		TIME_FUNC_FLAG2=Integer.parseInt(data.substring(2, 4), 16);
		CALENDAR_FUNC=Integer.parseInt(data.substring(4, 6), 16);
		NBR_NON_RECURR_DATES=Integer.parseInt(data.substring(6, 8), 16);
		NBR_RECURR_DATES=Integer.parseInt(data.substring(8, 10), 16);
		NBR_TIER_SWITCHES=Integer.parseInt(data.substring(10, 14), 16);
		CALENDAR_TBL_SIZE=Integer.parseInt(data.substring(14, 18), 16);
		decode();
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		Table51 t51=new Table51();
		t51.decode("000014002832001E01");
	}


}
