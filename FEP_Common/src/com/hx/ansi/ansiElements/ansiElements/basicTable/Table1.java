package com.hx.ansi.ansiElements.ansiElements.basicTable;

import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.ParserASC;

/** 
 * @Description  Table 1 厂商标识表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午02:28:43
 * @version 1.0 
 */
/**
 * 	Table 1 厂商标识表
 */
public class Table1 extends Table{
	
	public String  MANUFACTURER;//厂家缩写,4字节，0x48 0x58 0x45 0x20
	public String ED_MODEL;//表计模型标示,8个字节。 如HXE34—0x48 0x58 0x45 0x33 0x34 0x20 0x20 0x20
	public String HW_VERSION_NUMBER ;//硬件版本号
	public String HW_REVISION_NUMBER ;//硬件版本修订号
	public String FW_VERSION_NUMBER ;//软件版本号
	public String FW_REVISION_NUMBER ;//软件版本修订号
	public String MFG_SERIAL_NUMBER;// 表的序列号，根据Table0中FORMAT_CONTROL_2的值，序列号用BCD码表示，如表序列号为12345678，
									//	该数据项的表达为0x00 0x00 0x00 0x00 0x12 0x34 0x56 0x78
									//8字节或16字节
	
	@Override
	public void decode(String data) {
		//48 58 43 4F 48 58 53 33 30 30 20 20 01 00 01 00 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 
		MANUFACTURER=ParserASC.parseValue(data.substring(0, 8), 8);
		ED_MODEL=ParserASC.parseValue(data.substring(8, 24), 16);
		HW_VERSION_NUMBER=data.substring(24, 26);
		HW_REVISION_NUMBER=data.substring(26, 28);
		FW_VERSION_NUMBER=data.substring(28, 30);
		FW_REVISION_NUMBER=data.substring(30, 32);
		MFG_SERIAL_NUMBER=ParserASC.parseValue(data.substring(32, 64), 32);
	}
	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		if(table instanceof Table1){
			Table1 table1 = (Table1) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode, 16);
			switch(icode){
			case 65793://code:00010101
				ansiDataItem.resultData=table1.MANUFACTURER;
				break;
			case 65794://code:00010102
				ansiDataItem.resultData=table1.ED_MODEL;
				break;
			case 65795://code:00010103
				ansiDataItem.resultData=table1.HW_VERSION_NUMBER;
				break;
			case 65796://code:00010104
				ansiDataItem.resultData=table1.FW_VERSION_NUMBER;
				break;	
			case 65797://code:00010105
				ansiDataItem.resultData=table1.MFG_SERIAL_NUMBER;
				break;	
			}
		}else{
			System.out.println("错误的table参数");
		}
		return ansiDataItem;
	}
	public static void main(String[] args) {
//		Table1 t=new Table1();
//		t.decode("4858434F48585333303020200100010030303030303030303030303030303030");
//		int icode=Integer.parseInt("00010103", 16);
//		System.out.println(icode);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Integer.toHexString(2147483647));//7f ff ff ff
															//FF FF FF FF
	}
	
	
}
