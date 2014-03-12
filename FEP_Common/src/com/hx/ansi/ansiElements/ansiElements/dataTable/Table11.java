package com.hx.ansi.ansiElements.ansiElements.dataTable;

import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  	Table 11 数据源限制表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午03:28:02
 * @version 1.0 
 */
/**
 * Table 11 数据源限制表
 * 数据源限制表提供计量相关的配置或者限制信息。只读。
 */
public class Table11 extends Table{
	
	public int SOURCE_FLAGS;
	public boolean isCTPT=false;//是否有CT或PT
	public boolean isThermal=false;//是否支持thermal式需量计算
	public boolean isSlip=false;//是否支持滑差式需量计算
	public boolean isInterval=false;//是否支持区间式需量计算
	public boolean isReset=false;//需量复位限制是指需量复位后一段时间内不允许再次复位。
								//0—	表计不具备上述功能
								//1—	表计具备上述功能
	public boolean isPower_down=false;//计算最大需量时，某些表可以做到掉电后持续一段时间不将需量纳入最大需量的计算。
									//0-表计不具备上述功能
									//1-表计具备上述功能
	public int NBR_UOM_ENTRIES;//Table12 UOM_ENTRY_TBL内定义元素的个数
	public int NBR_DEMAND_CTRL_ENTRIES=1;//Table13 DEMAND_CONTROL_TBL内定义元素的个数，海兴表为1个
	public int DATA_CTRL_LENGTH;//Table14 DATA_CONTROL_TBL内每个元素表达的字节长度
	public int NBR_CTRL_CTRL_ENTRIES;//Table14 DATA_CONTROL_TBL内元素的个数
	public int NBR_CONSTANTS_ENTRIES;//Table15 CONSTANTS_TBL内元素的个数
	public int CONSTANTS_SELECTOR=2;//常数选择器，此处选择ELECTRIC_CONSTANTS_RCD=2。
	public int NBR_SOURCES;//Table16 SOURCES_TBL内元素的个数
	
	@Override
	public void decode() {
		isCTPT=((SOURCE_FLAGS&32)>>>5)==0?false:true;
		isThermal=((SOURCE_FLAGS&16)>>>4)==0?false:true;
		isSlip=((SOURCE_FLAGS&8)>>>3)==0?false:true;
		isInterval=((SOURCE_FLAGS&4)>>>2)==0?false:true;
		isReset=((SOURCE_FLAGS&2)>>>1)==0?false:true;
		isPower_down=(SOURCE_FLAGS&1)==0?false:true;
	}
	public void decode(String data){
		byte []b=new byte[1024];
		b=HexDump.toArray(data);
		SOURCE_FLAGS=AnsiDataSwitch.parseBytetoInt(b[0]);
		NBR_UOM_ENTRIES=AnsiDataSwitch.parseBytetoInt(b[1]);
		DATA_CTRL_LENGTH=AnsiDataSwitch.parseBytetoInt(b[3]);
		NBR_CTRL_CTRL_ENTRIES=AnsiDataSwitch.parseBytetoInt(b[4]);
		NBR_CONSTANTS_ENTRIES=AnsiDataSwitch.parseBytetoInt(b[5]);
		NBR_SOURCES=AnsiDataSwitch.parseBytetoInt(b[7]);
		decode();
	}
	
	
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		Table11 t=new Table11();
		t.decode("2A2A0A000001022A");
	}
}
