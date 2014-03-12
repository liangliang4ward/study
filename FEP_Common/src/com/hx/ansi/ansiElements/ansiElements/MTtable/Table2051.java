package com.hx.ansi.ansiElements.ansiElements.MTtable;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.ParserASC;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-8-10 ÏÂÎç02:26:37
 * @version 1.0 
 */

public class Table2051 extends  Table{
	
	public String meterSoftVersion;
	
	
	public void decode(String userData){
		this.meterSoftVersion=ParserASC.parseValue(userData,userData.length());
	}
	@Override
	public AnsiDataItem getIndex(AnsiDataItem ansiDataItem, Table table, AnsiContext context) {
		/*ansiDataItem.offset="000000";
		ansiDataItem.count="000D";*/
		ansiDataItem.offset="00000D";
		ansiDataItem.count="0014";
		return ansiDataItem;
	}
	
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		Table2051 table2051=(Table2051)table;
		int icode=Integer.parseInt(ansiDataItem.dataCode);
		switch(icode){
		case 80300:
			ansiDataItem.resultData=table2051.meterSoftVersion;
			break;
		}
		return ansiDataItem;
	}
}
