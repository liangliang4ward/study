package com.hx.ansi.ansiElements.ansiElements.registerTable;

import java.util.HashMap;
import java.util.Map;

import com.hx.ansi.ansiElements.ansiElements.Table;

/** 
 * @Description Table22-- 电量、最大需量数据选择表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午03:29:34
 * @version 1.0 
 */

public class Table22 extends Table{
	
	public String SUMMATION_SELECT;
	public int index;//数据在23表中的索引
	public String DEMAND_SELECT ;
	public int MIN_OR_MAX_FLAGS =0xF0;//海兴采用0xF0
	public Map<Integer,Integer> selectMap=new HashMap<Integer,Integer>();
	
	@Override
	public void decode() {
		
	}
	public void decode(String data,int NBR_SUMMATIONS,int NBR_DEMANDS){
		SUMMATION_SELECT=data.substring(0, 2*NBR_SUMMATIONS);
		DEMAND_SELECT=data.substring(2*NBR_SUMMATIONS, 2*NBR_SUMMATIONS+2*NBR_DEMANDS);
		for(int i =0;i<(NBR_SUMMATIONS+NBR_DEMANDS);i++){
			selectMap.put( i,Integer.parseInt(data.substring(2*i, 2*i+2), 16));//12(16)表中的索引对应到23表中的索引
		}
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		//00 01 02 03 04 05 06 07 08 09 20 21 22 23 24 25 26 27 28 29 FF FF 
	}
}
