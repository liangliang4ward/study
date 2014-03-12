package com.hx.ansi.ansiElements.ansiElements.displayTable;

import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:04:43
 * @version 1.0 
 */

public class Table31 extends Table{
	public  int DISPLAY_CTRL;//是否可以编程
	public 	boolean hold=false;
	public  boolean blank=false;
	public  boolean dipaly=false;
	public  int NBR_DISP_SOURCES;//可供选择的显示项数
	public  int WIDTH_DISP_SOURCES=2;//显示项ID的字节数
	public  int NBR_PRI_DISP_LIST_ITEMS;//一次侧可供选择的显示项数，海兴表计无一次侧显示的设置，NBR_PRI_DISP_LIST_ITEMS=0
	public  int NBR_PRI_DISP_LISTS;//一次侧显示模式的种类，由于海兴表计无一次侧显示的设置，NBR_PRI_DISP_LISTS =0
	public  int NBR_SEC_DISP_LIST_ITEMS;//二次侧可供选择的显示项数，海兴表计中NBR_SEC_DISP_LIST_ITEMS = NBR_DISP_SOURCES
	public  int NBR_SEC_DISP_LISTS;//二次侧显示模式的种类
	
	
	
	@Override
	public void decode() {
		hold=((DISPLAY_CTRL&4)>>2)==0?false:true;
		blank=((DISPLAY_CTRL&2)>>1)==0?false:true;
		dipaly=(DISPLAY_CTRL&1)==0?false:true;
	}
	@Override
	public void decode(String data){
		byte []b=new byte[1024];
		b=HexDump.toArray(data);
		DISPLAY_CTRL=AnsiDataSwitch.parseBytetoInt(b[0]);
		NBR_DISP_SOURCES=Integer.parseInt(data.substring(2, 6), 16);
		WIDTH_DISP_SOURCES=AnsiDataSwitch.parseBytetoInt(b[3]);
		NBR_PRI_DISP_LIST_ITEMS=Integer.parseInt(data.substring(8, 12), 16);
		NBR_PRI_DISP_LISTS=AnsiDataSwitch.parseBytetoInt(b[6]);
		NBR_SEC_DISP_LIST_ITEMS=Integer.parseInt(data.substring(14, 18), 16);
		NBR_SEC_DISP_LISTS=AnsiDataSwitch.parseBytetoInt(b[9]);
		decode();
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		Table31 t31=new Table31();
		//01800002C00003000000
		t31.decode("01800002C00003000000");
		
		
	}

	
	
}
