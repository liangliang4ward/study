package com.hx.ansi.ansiElements.ansiElements.basicTable;

import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;
import com.hx.ansi.parse.ParserASC;




/** 
 * @Description  Table 0  基本配置表,包含有所有table的个数、过程的个数、数据格式的定义等。该配置信息为只读模式。
 * 				 同时记录table0每一个元素的索引	
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 上午11:32:06
 * @version 1.0 
 */
public class Table0 extends Table{
	
	public int formatControl_1=03;//字符采用ASCII码，传输采用高字节在前
	public int formatControl_2;
	public int formatControl_2_dataFormat;
	public int formatControl_2_numberFormat=1;//BCD码
	public int formatControl_2_operationMode;
	public int formatControl_2_time=2;//16进制表示年月日
	public int formatControl_3;
	public int formatControl_3_NI_FMAT2;
	public int formatControl_3_NI_FMAT1;
	public String device_class="48584520";// 0x48 0x58 0x45 0x20---HXE
	public String ASCdevice_class="";
	public String  nameplate_type;//装置类型:0—气 1—水 2—电 其他--保留
	public int default_set_used;
	public int MAX_PROC_PARM_LENGTH;//调用table 7执行过程时，过程所带的参数的字节最大值
	public int MAX_RESP_DATA_LENGTH;//调用table 8执行过程响应时，过程所带的参数的字节最大值
	public String STD_VERSION_NO;
	public String STD_REVISION_NO;
	public int DIM_STD_TBLS_USED;//能表达表计中实现的标准table的最少字节数
	public String STD_TBLS_USED;//表中实现标准table的实际情况
	public int DIM_MFG_TBLS_USED;//能表达表计中实现的厂家定义table的最少字节数
	public String MFG_TBLS_USED;//表中实现厂家定义table的实际情况
	public int DIM_STD_PROC_USED;//能表达表计中实现的标准过程的最少字节数
	public String STD_PROC_USED;//表中实现标准过程的实际情况
	public int DIM_MFG_PROC_USED;//能表达表计中实现的厂家定义过程的最少字节数
	public String MFG_PROC_USED;//表中实现厂家自定义过程的实际情况
	public int DIM_MFG_STATUS_USED;//能表达表计中实现的厂家自定义状态标志的最少字节数，如果厂家没有自定义状态标志，那么此字节为0
	public int NBR_PENDING;//Pending状态集的个数，table4中用到
	public String STD_TBLS_WRITE;//表中实现的标准table是否可写的实际情况
	public String MFG_TBLS_WRITE;//表中实现的厂家定义table是否可写的实际情况
	public String STD_TBLS_USED_Bit;
	public String MFG_TBLS_USED_Bit;
	public String STD_PROC_USED_Bit;
	public String MFG_PROC_USED_Bit;
	public String STD_TBLS_WRITE_Bit;
	public String MFG_TBLS_WRITE_Bit;
	
	@Override
	public void decode(){
		 formatControl_2_dataFormat=((formatControl_2&192)>>>6);
		 formatControl_2_operationMode=((formatControl_2&24)>>>3);
		 formatControl_3_NI_FMAT2=((formatControl_3&240)>>>4);
		 formatControl_3_NI_FMAT1=(formatControl_3&15);
		 STD_TBLS_USED_Bit=AnsiDataSwitch.parseStringToBit(STD_TBLS_USED);
		 MFG_TBLS_USED_Bit=AnsiDataSwitch.parseStringToBit(MFG_TBLS_USED);
		 STD_PROC_USED_Bit=AnsiDataSwitch.parseStringToBit(STD_PROC_USED);
		 MFG_PROC_USED_Bit=AnsiDataSwitch.parseStringToBit(MFG_PROC_USED);
		 STD_TBLS_WRITE_Bit=AnsiDataSwitch.parseStringToBit(STD_TBLS_WRITE);
		 MFG_TBLS_WRITE_Bit=AnsiDataSwitch.parseStringToBit(MFG_TBLS_WRITE);
		 ASCdevice_class=ParserASC.parseValue(device_class, 8);
	}
	@Override
	public void decode(String table0String){
		try{
			byte []b=new byte[1024];
			b=HexDump.toArray(table0String);
			//第一个字节是formatControl_1
			formatControl_1=AnsiDataSwitch.parseBytetoInt(b[0]);
			formatControl_2=AnsiDataSwitch.parseBytetoInt(b[1]);
			formatControl_3=AnsiDataSwitch.parseBytetoInt(b[2]);
			device_class=table0String.substring(6, 14);
			nameplate_type=table0String.substring(14, 16);
			STD_VERSION_NO=table0String.substring(22, 24);
			STD_REVISION_NO=table0String.substring(24, 26);
			default_set_used=AnsiDataSwitch.parseBytetoInt(b[8]);
			MAX_PROC_PARM_LENGTH=AnsiDataSwitch.parseBytetoInt(b[9]);
			MAX_RESP_DATA_LENGTH=AnsiDataSwitch.parseBytetoInt(b[10]);
			DIM_STD_TBLS_USED=AnsiDataSwitch.parseBytetoInt(b[13]);
			DIM_MFG_TBLS_USED=AnsiDataSwitch.parseBytetoInt(b[14]);
			DIM_STD_PROC_USED=AnsiDataSwitch.parseBytetoInt(b[15]);
			DIM_MFG_PROC_USED=AnsiDataSwitch.parseBytetoInt(b[16]);
			DIM_MFG_STATUS_USED=AnsiDataSwitch.parseBytetoInt(b[17]);
			NBR_PENDING=AnsiDataSwitch.parseBytetoInt(b[18]);
			//实现的table详细情况
			STD_TBLS_USED=HexDump.hexDumpCompact(b, 18, DIM_STD_TBLS_USED);
			MFG_TBLS_USED=HexDump.hexDumpCompact(b, 18+DIM_STD_TBLS_USED, DIM_MFG_TBLS_USED);
			STD_PROC_USED=HexDump.hexDumpCompact(b, 18+DIM_MFG_TBLS_USED, DIM_STD_PROC_USED);
			MFG_PROC_USED=HexDump.hexDumpCompact(b, 18+DIM_STD_PROC_USED, DIM_MFG_PROC_USED);
			STD_TBLS_WRITE=HexDump.hexDumpCompact(b, 18+DIM_MFG_PROC_USED, DIM_STD_TBLS_USED);
			MFG_TBLS_WRITE=HexDump.hexDumpCompact(b, 18+DIM_STD_TBLS_USED, DIM_MFG_TBLS_USED);
			decode();
		}catch(Exception e){
			
		}
	}
	@Override
	public void encode() {
		//下行读取消息，先在这里根据数据项编码找到规约数据项。再找到索引。组帧下发读取
		
	}
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		if(table instanceof Table0){
			Table0 table0 = (Table0) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode, 16);
			switch(icode){
			case 65537://code:00010001
				ansiDataItem.resultData=table0.ASCdevice_class;
				break;
			case 65538://code:00010002
				ansiDataItem.resultData=table0.nameplate_type;
				break;
			case 65539://code:00010003
				ansiDataItem.resultData=table0.STD_VERSION_NO;
				break;
			case 65540://code:00010004
				ansiDataItem.resultData=table0.STD_REVISION_NO;
				break;			
			}
		}else{
			System.out.println("错误的table参数");
		}
		return ansiDataItem;
	}
	public static void main(String[] args) {
		Table0 t=new Table0();
		String s="0A884845434F0200191901000A0202030100EFBDF1DF031EFCF0C11FC203C807000000E0A0400002244000000AC003";
				//0035000030020A884845434F0200191901000A0202030100EFBDF1DF031EFCF0C11FC203C807000000E0A0400002244000000AC0031630
		t.decode(s);
		String ss="45434F02";
		ss=ParserASC.parseValue(ss, 8);
	}
	
}
