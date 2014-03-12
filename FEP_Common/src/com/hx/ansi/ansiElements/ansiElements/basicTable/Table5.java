package com.hx.ansi.ansiElements.ansiElements.basicTable;

import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;
import com.hx.ansi.parse.ParserASC;

/** 
 * @Description  	Table 5 表计标识表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午02:29:20
 * @version 1.0 
 */

public class Table5 extends Table{
	
	public String  meterAddr;//len=10字节
	
	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	public void decode(String data){
		meterAddr=ParserASC.parseValue(data.substring(0,40), 40);
	}
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		if(table instanceof Table5){
			Table5 table5 = (Table5) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode, 16);
			switch(icode){
			case 66817: //code:00010501
				ansiDataItem.resultData=table5.meterAddr;
				break;
	
			}
		}else{
			System.out.println("错误的table参数");
		}
		return ansiDataItem;
	}
	
	public static void main(String[] args) {
		Table5 t=new Table5();
		t.decode("3030303030303030303030303030303030303030");
		
		
		
		
	}
}
