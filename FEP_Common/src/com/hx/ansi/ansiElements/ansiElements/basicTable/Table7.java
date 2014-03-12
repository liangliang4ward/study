package com.hx.ansi.ansiElements.ansiElements.basicTable;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.HexDump;
import cn.hexing.fk.utils.StringUtil;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  	Table 7 发起过程执行表(只写)
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午03:27:39
 * @version 1.0 
 */

public class Table7 extends Table{
	private static final Logger log = Logger.getLogger(Table7.class);

	public String  TABLE_IDB_BFLD ;
	private boolean isStandard=true;//默认都按标准过程处理
	private int actionName;
	private int commandId=0;//初始化为0，用来个Table8中配对
	private String paramRCD="";
	
	
	
	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void encode() {
		actionName=isStandard?(actionName):(2048|actionName);
		String sActionName=HexDump.toHex(actionName).substring(4);
		sActionName =sActionName;
//		String actionBit=AnsiDataSwitch.parseStringToBit(sActionName);
		String sCommandId=HexDump.toHex(commandId).substring(6);
		TABLE_IDB_BFLD=sActionName+sCommandId+paramRCD;
	}
	
	@Override
	public  AnsiDataItem encode(AnsiDataItem ansiDataItem, Table table, AnsiContext context){
		Table7 table7=(Table7)table;
		int icode=Integer.parseInt(ansiDataItem.dataCode);
		switch(icode){
		case 100700:
			actionName=0;
			break;
		case 100701:
			actionName=1;
			break;
		case 100702:
			actionName=2;
			break;
		case 100703:
			actionName=3;
			break;
		case 100704:
			actionName=4;
			break;
		case 5200:
			actionName=10;
			SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmss");
			try{
				Date date=sd.parse(ansiDataItem.data);
				ansiDataItem.data=sdf.format(date);
				ansiDataItem.data=AnsiDataSwitch.toHexString(ansiDataItem.data);
			}catch(Exception e){
				log.error(StringUtil.getExceptionDetailInfo(e));
			}
			paramRCD="03"+ansiDataItem.data+"00";
			ansiDataItem.count="000B";
			break;
		case 1100700:
			actionName=2;
			isStandard=false;
			ansiDataItem.count="0004";
			paramRCD="00";
			break;
		case 1100701:
			actionName=2;
			isStandard=false;
			ansiDataItem.count="0004";
			paramRCD="01";
			break;
		case 1100704://enter load control mode
			actionName=12;
			isStandard=false;
			ansiDataItem.count="0004";
			paramRCD="01";
			break;
		case 1100705://exit load control mode
			actionName=12;
			isStandard=false;
			ansiDataItem.count="0004";
			paramRCD="00";
			break;
		case 100716:
			actionName=16;
			break;
		case 100717:
			actionName=17;
			break;
		case 11801006:
			actionName=2056;
			paramRCD=ansiDataItem.data;
		}
		commandId=table7.getNextCommandId();
		encode();
		ansiDataItem.data=this.TABLE_IDB_BFLD;
//		ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
		return ansiDataItem;
	} 
	public boolean isStandard() {
		return isStandard;
	}
	public void setStandard(boolean isStandard) {
		this.isStandard = isStandard;
	}
	public int getActionName() {
		return actionName;
	}
	public void setActionName(int actionName) {
		this.actionName = actionName;
	}
	public int getCommandId() {
		return commandId;
	}
	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}
	public String getParamRCD() {
		return paramRCD;
	}
	public void setParamRCD(String paramRCD) {
		this.paramRCD = paramRCD;
	}
	
	public final int getNextCommandId(){
		commandId++;
		if( commandId> 0xFF )
			commandId = 1;
		return commandId;
	}
	
	public static void main(String[] args) {
		Table7 t=new Table7();
		t.setActionName(1);
		t.setCommandId(240);
		t.setParamRCD("");
		t.encode();
		System.out.println(t.TABLE_IDB_BFLD);
		
		
//		String sActionName=HexDump.toHex(3|2048).substring(4);
//		String actionBit=AnsiDataSwitch.parseStringToBit(sActionName);
//		System.out.println(actionBit);
	}
	
	
}
