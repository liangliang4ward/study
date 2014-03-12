package com.hx.ansi.ansiElements.ansiElements.registerTable;

import java.util.HashMap;
import java.util.Map;

import com.hx.ansi.ansiElements.ansiElements.Table;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-25 下午06:39:25
 * @version 1.0 
 */

public class Table27 extends Table{
	public String PRESENT_DEMANDS_SELECT;
	public String PRESENT_VALUE_SELECT;//实时量数据在数据源定义表（table 16）的选择索引 ex：PRESENT_VALUE_SELECT=0x04 0x05
	public int ACT_REGS_TBL_NBR_PRESENT_DEMANDS;//当前需量寄存器的个数
	public int ACT_REGS_TBL_NBR_PRESENT_VALUES;//实时量寄存器的个数
	public Map<Integer,Integer> selectMap=new HashMap<Integer,Integer>();
	@Override
	public void decode() {
		
	}
	
	public void decode(String data,int NBR_PRESENT_DEMANDS,int NBR_PRESENT_VALUES){
		PRESENT_DEMANDS_SELECT=data.substring(0, 2*NBR_PRESENT_DEMANDS);
		PRESENT_VALUE_SELECT=data.substring(2*NBR_PRESENT_DEMANDS, 2*NBR_PRESENT_DEMANDS+2*NBR_PRESENT_VALUES);
		for(int i =0;i<(NBR_PRESENT_DEMANDS+NBR_PRESENT_VALUES);i++){
			selectMap.put( i,Integer.parseInt(data.substring(2*i, 2*i+2), 16));//12(16)表中的索引对应到23表中的索引
		}
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
public static void main(String[] args) {
	//20 21 22 23 24 25 26 27 28 29 0A 0B 0C 0D 0E 0F 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F
	String s="202122232425262728290A0B0C0D0E0F101112131415161718191A1B1C1D1E1F";
	String ss="";
	for(int i=0;i<s.length()/2;i++){
		ss=s.substring(2*i, 2*i+2);
		System.out.println(ss);
	}
}
	
}
