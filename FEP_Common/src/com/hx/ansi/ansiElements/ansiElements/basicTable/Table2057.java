package com.hx.ansi.ansiElements.ansiElements.basicTable;

import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;
import com.hx.ansi.parse.ParserASC;
import com.hx.ansi.parse.ParserHTB;
import com.hx.ansi.parse.ParserIP;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-5-20 下午03:05:59
 * @version 1.0 
 */

public class Table2057 extends Table{
	public String masterIP_PORT;
	public String alt_TP_PORT;
	public String APN;
	public int beatHeart;
	public String PDP_user;
	public String PDP_password;
	public String smsNumber;
	

	
	

	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encode() {
		
		
	}
	@Override
	public  AnsiDataItem encode(AnsiDataItem ansiDataItem, Table table, AnsiContext context){
		int icode=Integer.parseInt(ansiDataItem.dataCode);
		switch(icode){
		case 800900:
			ansiDataItem.data=ParserIP.constructor(ansiDataItem.data, 12);
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000000";
			ansiDataItem.count="0006";
			break;
		case 800901:
			ansiDataItem.data=ParserIP.constructor(ansiDataItem.data, 12);
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000006";
			ansiDataItem.count="0006";
			break;
		case 800902:
			ansiDataItem.data=ParserASC.constructor(ansiDataItem.data, 64);
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="00000C";
			ansiDataItem.count="0020";
			break;
		case 800903:
			ansiDataItem.data=ParserASC.constructor(ansiDataItem.data, 64);
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="00002D";
			ansiDataItem.count="0020";
			break;
		case 800904:
			ansiDataItem.data=ParserASC.constructor(ansiDataItem.data, 64);
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="00004D";
			ansiDataItem.count="0020";
			break;
		case 800905:
			ansiDataItem.data=ParserASC.constructor(ansiDataItem.data, 30);
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="00006D";
			ansiDataItem.count="000F";
			break;
		case 800906:
			ansiDataItem.data=ParserHTB.constructor(ansiDataItem.data, 2);
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="00002C";
			ansiDataItem.count="0001";
			break;
		}
		return ansiDataItem;
	}
	
	@Override
	public AnsiDataItem getIndex(AnsiDataItem ansiDataItem, Table table, AnsiContext context) {
		ansiDataItem.offset="000000";
		ansiDataItem.count="007C";
		return ansiDataItem;
	}
	@Override
	public void decode(String data) {
		masterIP_PORT=ParserIP.parseValue(data.substring(0, 12), 12);
		alt_TP_PORT=ParserIP.parseValue(data.substring(12, 24), 12);
		APN=ParserASC.parseValue(data.substring(24, 88), 32);
		beatHeart=Integer.parseInt(data.substring(88, 90), 16);
		PDP_user=ParserASC.parseValue(data.substring(90, 154), 32);
		PDP_password=ParserASC.parseValue(data.substring(154, 218), 32);
		smsNumber=ParserASC.parseValue(data.substring(218, 248), 30);
	}
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		if(table instanceof Table2057){
			Table2057 table2057 = (Table2057) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode);
			switch(icode){
			case 800900://code:00800900
				ansiDataItem.resultData=table2057.masterIP_PORT;
				break;
			case 800901://code:00010002
				ansiDataItem.resultData=table2057.alt_TP_PORT;
				break;
			case 800902://code:00010003
				ansiDataItem.resultData=table2057.APN;
				break;
			case 800906://code:00010004
				ansiDataItem.resultData=table2057.beatHeart+"";
				break;	
			case 800903://code:00010004
				ansiDataItem.resultData=table2057.PDP_user;
				break;
			case 800904://code:00010004
				ansiDataItem.resultData=table2057.PDP_password;
				break;
			case 800905://code:00010004
				ansiDataItem.resultData=table2057.smsNumber;
				break;
			}
		}else{
			System.out.println("错误的table参数");
		}
		return ansiDataItem;
	}

	public static void main(String[] args) {
		Table2057 table2057=new Table2057();
		table2057.decode("73EE24A5120C73EE24A6C81F434D4E455400000000000000000000000000000000000000000000000000000002434152440000000000000000000000000000000000000000000000000000000043415244000000000000000000000000000000000000000000000000000000003836313338303035373135303000003836313332303830323334303200000000000200000000000000000007000200003132333435363738000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
	}
	
	
	
	
}
