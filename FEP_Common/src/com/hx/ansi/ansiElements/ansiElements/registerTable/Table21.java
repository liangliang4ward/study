package com.hx.ansi.ansiElements.ansiElements.registerTable;

import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  Table 21 寄存器限制定义表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午03:29:16
 * @version 1.0 
 */

public class Table21 extends Table{
	
	public int REG_FUNC1_FLAGS ;
	public boolean curplusTime=false;
	public boolean continuousCumulative=false;
	public boolean accumulatedDemand=false;
	public boolean lockReset=false;
	public boolean resetTimes=false;
	public boolean dateTime=false;
	public boolean dateSesion=false;
	public int REG_FUNC2_FLAGS ;
	public boolean freezeReset=false;
	public boolean weekFreeze=false;
	public boolean dayFreeze=false;
	public boolean freezeNumber=false;
	public boolean enablseFreeze=false;
	public int NBR_SELF_READS;
	public int NBR_SUMMATIONS ;//每个数据块(data-block)中电量寄存器的个数
	public int NBR_DEMANDS ;//每个数据块(data-block)中需量寄存器的个数
	public int NBR_COIN_VALUES ;
	public int NBR_OCCUR;
	public int NBR_TIERS ;//费率数
	public int NBR_PRESENT_DEMANDS ;
	public int NBR_PRESENT_VALUES ;
	
	
	@Override
	public void decode() {
		curplusTime=((REG_FUNC1_FLAGS&64)>>>6)==0?false:true;
		continuousCumulative=((REG_FUNC1_FLAGS&32)>>>5)==0?false:true;
		accumulatedDemand=((REG_FUNC1_FLAGS&16)>>>4)==0?false:true;
		lockReset=((REG_FUNC1_FLAGS&8)>>>3)==0?false:true;
		resetTimes=((REG_FUNC1_FLAGS&4)>>>2)==0?false:true;
		dateTime=((REG_FUNC1_FLAGS&2)>>>1)==0?false:true;
		dateSesion=(REG_FUNC1_FLAGS&1)==0?false:true;
		freezeReset=((REG_FUNC2_FLAGS&48)>>>4)==0?false:true;
		weekFreeze=((REG_FUNC2_FLAGS&8)>>>3)==0?false:true;
		dayFreeze=((REG_FUNC2_FLAGS&4)>>>2)==0?false:true;
		freezeNumber=((REG_FUNC2_FLAGS&2)>>>1)==0?false:true;
		enablseFreeze=(REG_FUNC2_FLAGS&1)==0?false:true;
		
	}
	public void decode(String data){
		byte []b=new byte[1024];
		b=HexDump.toArray(data);
		REG_FUNC1_FLAGS=AnsiDataSwitch.parseBytetoInt(b[0]);
		REG_FUNC2_FLAGS=AnsiDataSwitch.parseBytetoInt(b[1]);
		NBR_SELF_READS=AnsiDataSwitch.parseBytetoInt(b[2]);
		NBR_SUMMATIONS=AnsiDataSwitch.parseBytetoInt(b[3]);
		NBR_DEMANDS=AnsiDataSwitch.parseBytetoInt(b[4]);
		NBR_COIN_VALUES=AnsiDataSwitch.parseBytetoInt(b[5]);
		NBR_OCCUR=AnsiDataSwitch.parseBytetoInt(b[6]);
		NBR_TIERS=AnsiDataSwitch.parseBytetoInt(b[7]);
		NBR_PRESENT_DEMANDS=AnsiDataSwitch.parseBytetoInt(b[8]);
		NBR_PRESENT_VALUES=AnsiDataSwitch.parseBytetoInt(b[9]);
		decode();
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		String s="0F00010A0A0001040A16";
		Table21 t21=new Table21();
		t21.decode(s);
	}
}
