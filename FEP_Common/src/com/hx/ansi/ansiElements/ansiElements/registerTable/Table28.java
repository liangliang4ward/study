package com.hx.ansi.ansiElements.ansiElements.registerTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.AnsiCommandResult;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-25 下午06:40:18
 * @version 1.0 
 */

public class Table28 extends Table{
    public Map<String,String> summationsMap=new HashMap<String,String>();
	@Override
	public void decode() {
		
	}
	@Override
	public void decode(String data){
	
	}

	public void decode(AnsiContext context,String data){
		int  count =getCount(context);//每一个数据项数据的长度
		for(int i=0;i<data.length()/8;i++){
			String sdata="";
			String value="";
			sdata=data.substring(8*i, 8*i+8);//取出数据
			double dresult=Long.parseLong(sdata, 16)*(context.table12.paramMap.get(context.table27.selectMap.get(i)).multiplier);
			value=String.valueOf(dresult);
			value=AnsiDataSwitch.getDouble(value, 2);
			String code=context.table12.dataItemMap.get(context.table27.selectMap.get(i));
			this.summationsMap.put(code, value);
		}
		
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		if(table instanceof Table28){
		Table28 table28 = (Table28) table;
		List<AnsiCommandResult> rt=new ArrayList<AnsiCommandResult>();
		Iterator itor=table28.summationsMap.entrySet().iterator();
			while(itor.hasNext()){
				Map.Entry<String,String> entry=(Map.Entry<String,String>)itor.next();
				AnsiCommandResult acr=new AnsiCommandResult();
				acr.setCode(entry.getKey());
				acr.setValue(entry.getValue());
				rt.add(acr);
		}
		ansiDataItem.commandResult=rt;	
	}else{
		System.out.println("错误的table参数。。。23");
	}
	return ansiDataItem;
	}
	
	
	
	
	
	
	/**
	 * 获取没一个数据项字节数
	 * @param context
	 * @return
	 */
	public int getCount(AnsiContext context){
		int offSet=0;
		//table0中有一些数据类型还不明确
		switch(context.table0.formatControl_3_NI_FMAT2){
		case 0:
			offSet=8;//FLOAT64
		case 1:
			offSet=4;//FLOAT32
		case 2:
			offSet=4;//FLOAT—CHAR12
		case 3:
			offSet=4;//FLOAT-CHAR6
		case 4:
			offSet=4;//INT32 /10000
		case 5:
			offSet=6;//BCD6
		case 6:
			offSet=4;//BCD4
		case 7:
			offSet=3;//INT24
		case 8:
			offSet=4;//INT32
		case 9:
			offSet=5;//INT40
		case 10:
			offSet=6;//INT48
		case 11:
			offSet=8;//BCD8
		case 12:
			offSet=4;//FLOAT-CHAR21
		default :
			offSet=4;//默认偏移4个字节
		}
		return offSet;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		//00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 49 59 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 8B 13 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		String s="00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 49 59 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 8B 13 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
		s=s.replace(" ", "");
		String ss="";
		for(int i=0;i<s.length()/8;i++){
			ss=s.substring(8*i, 8*i+8);
			System.out.println(ss);
		}
	}
	
}
