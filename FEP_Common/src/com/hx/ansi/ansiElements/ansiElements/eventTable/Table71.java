package com.hx.ansi.ansiElements.ansiElements.eventTable;

import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  	Table 71 事件实际限制表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @version 1.0 
 */

public class Table71 extends Table {

	public int LOG_FLAGS_BFLD;
	public boolean eventInhibit;
	public boolean histInhibit;
	public boolean histSeqNum;
	public boolean histDatetime;
	public boolean eventNumber;
	public int NBR_STD_EVENTS;//用来表达table 72中表计支持的标准事件（EVENTS_SUPPORTED_TBL.STD_EVENTS_SUPPORTED）的字节数
	public int NBR_MFG_EVENTS;//用来表达table 72中表计支持的厂家自定义事件（EVENTS_SUPPORTED_TBL.MFG_EVENTS_SUPPORTED）的字节数
	public int HIST_DATA_LENGTH;//History log中事件记录数据（HISTORY_LOG_DATA_TBL.HISTORY_ARGUMENT）的字节数
	public int EVENT_DATA_LENGTH;//Event log中事件记录数据（EVENT_LOG_DATA_TBL.EVENT_ARGUMENT）的字节数
	public int NBR_HISTORY_ENTRIES;//History log记录条数的最大容量
	public int NBR_EVENT_ENTRIES ;//Event log记录条数的最大容量
	public int EXT_LOG_FLAGS;
	public int NBR_PROGRAM_TABLES;
	
	
	@Override
	public void decode() {
		eventInhibit=((LOG_FLAGS_BFLD&16)>>4)==0?false:true;
		histInhibit=((LOG_FLAGS_BFLD&8)>>3)==0?false:true;
		histSeqNum=((LOG_FLAGS_BFLD&4)>>2)==0?false:true;
		histDatetime=((LOG_FLAGS_BFLD&2)>>1)==0?false:true;
		eventNumber=(LOG_FLAGS_BFLD&1)==0?false:true;
	}

	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decode(String data) {
		//06 07 01 02 02 0A 00 0A 00
		//标准长度是12个字节，但是从表计里面读出来的是9个字节，也就是不满足STD_VERSION_NO > 1条件，后面3个字节没有。但是table0里面海兴采用的是STD_VERSION_NO=2
		//所以这里我认为应该是12个字节，如果是9个字节 那么也要按照9个字节处理
		byte []b=new byte[1024];
		b=HexDump.toArray(data);
		LOG_FLAGS_BFLD=AnsiDataSwitch.parseBytetoInt(b[0]);
		NBR_STD_EVENTS=AnsiDataSwitch.parseBytetoInt(b[1]);
		NBR_MFG_EVENTS=AnsiDataSwitch.parseBytetoInt(b[2]);
		HIST_DATA_LENGTH=AnsiDataSwitch.parseBytetoInt(b[3]);
		EVENT_DATA_LENGTH=AnsiDataSwitch.parseBytetoInt(b[4]);
		NBR_HISTORY_ENTRIES=Integer.parseInt(data.substring(10, 14), 16);
		NBR_EVENT_ENTRIES=Integer.parseInt(data.substring(14, 18), 16);
//		EXT_LOG_FLAGS=AnsiDataSwitch.parseBytetoInt(b[9]);
//		NBR_PROGRAM_TABLES=Integer.parseInt(data.substring(20, 24), 16);
		decode();
	}
	public static void main(String[] args) {
		Table71 t71=new Table71();
		t71.decode("06070102020A000A00");
		
		
	}
}