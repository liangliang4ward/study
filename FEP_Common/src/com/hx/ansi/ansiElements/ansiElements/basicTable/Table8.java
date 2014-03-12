package com.hx.ansi.ansiElements.ansiElements.basicTable;

import com.hx.ansi.ansiElements.ansiElements.Table;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午03:27:49
 * @version 1.0 
 */

public class Table8 extends Table{
	
	public String  TABLE_IDB_BFLD ;
	public  boolean isStandard=true;//默认都按标准过程处理
	public int actionName;
	public  int commandId=0;//初始化为0，用来个Table8中配对
	public String paramRCD;
	public int RESULT_CODE;
	
	
	@Override
	public void decode(String data){
		TABLE_IDB_BFLD=data.substring(0, 4);
		int iTABLE_IDB_BFLD=Integer.parseInt(TABLE_IDB_BFLD, 16);
		actionName=iTABLE_IDB_BFLD&2047;//取后面的bit10-bit0
		isStandard=(iTABLE_IDB_BFLD&2048)==1?true:false;
		commandId=Integer.parseInt(data.substring(4, 6), 16);
		RESULT_CODE=Integer.parseInt(data.substring(6, 8), 16);//执行结果
		paramRCD=data.substring(8);
	}
	
	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		int t=8;
		int i=4095&2047;
		System.out.println(i);
		
		
	}
	
}
