package com.hx.ansi.ansiElements.ansiElements.MTtable;


import java.util.Date;

import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-7-24 下午03:06:07
 * @version 1.0 
 */

public class Table2064 extends Table {
	
	public  int maxSize;
	public  String upgradeInf;
	public  String transferStatus;
	public  String activeInf;
	public  Date activeTime;
	public  String bitMap;

	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table,String userData) {
		if(table instanceof Table2064){
			Table2064 table2064 = (Table2064) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode);
			switch(icode){
			case 11801000:
				ansiDataItem.resultData=userData;
				break;
			case 11801001:
				ansiDataItem.resultData=userData;
				break;
			case 11801002:
				ansiDataItem.resultData=userData;
				break;
			case 11801003:
				ansiDataItem.resultData=userData;
				break;
			case 11801004:
				ansiDataItem.resultData=userData;
				break;
			case 11801005:
				ansiDataItem.resultData=userData.substring(4);
				break;
			
			}
			}
		else{
		   System.out.println("错误的table参数");
		}
			
		return ansiDataItem;
	}
	
	

	
	
	
}

