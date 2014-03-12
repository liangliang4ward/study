package com.hx.ansi.ansiElements.ansiElements.MTtable;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.ParserASC;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-8-10 ÏÂÎç02:26:47
 * @version 1.0 
 */

public class Table2053 extends Table{

	public String moduleSoftVersion;
	
	public void decode(String userData){
		this.moduleSoftVersion=ParserASC.parseValue(userData, userData.length());
	}
	@Override
	public AnsiDataItem getIndex(AnsiDataItem ansiDataItem, Table table, AnsiContext context) {
		ansiDataItem.offset="000008";
		ansiDataItem.count="0011";
		return ansiDataItem;
	}
	
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table){
		Table2053 table2053=(Table2053)table;
		int icode=Integer.parseInt(ansiDataItem.dataCode);
		switch(icode){
		case 80500:
			ansiDataItem.resultData=table2053.moduleSoftVersion;
			break;
		}
		return ansiDataItem;
	}
}
