package com.hx.ansi.ansiElements.ansiElements.loadTable;

import cn.hexing.fk.utils.HexDump;
import cn.hexing.fk.utils.StringUtil;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.ansiElements.ansiElements.basicTable.Table2049;
import com.hx.ansi.parse.AnsiDataSwitch;



public class Table2060 extends Table {

	private int loadControlStatus;//0:closed 1:open
	private int powerThreshold;//unit:0.01A,corresponding to total active power
	
	public int getLoadControlStatus() {
		return loadControlStatus;
	}
	public void setLoadControlStatus(byte loadControlStatus) {
		this.loadControlStatus = loadControlStatus;
	}
	@Override
	public void decode(String data) {
		data=StringUtil.PadLeftForStr(data, 22, "0");
		this.loadControlStatus=ParseLoadControlStatus(data.substring(0, 2));
		this.powerThreshold= ParseHexStrToInt(data.substring(2, 6));
		this.lagToEnterLoadControl=ParseHexStrToInt(data.substring(6, 10));
		this.lagToExitLoadControl=ParseHexStrToInt(data.substring(10, 14));
		this.TimesOfOverLoad= ParseHexStrToInt(data.substring(14, 18));
		
		this.lagOfContinuousOverLoad=ParseHexStrToInt(data.substring(18, 22));
	}
	
	private int ParseLoadControlStatus(String value){
		int status=-1;
		if(value.equals("01") || value.endsWith("00")){
			status= Integer.parseInt(value);
		}
		
		return status;
	}
	
	//return -1 means invalid value
	private int ParseHexStrToInt(String value){
		int threshold=-1;
		try{
			threshold = Integer.parseInt(value,16); 
		}
		catch(Exception ex){
		}
		
		return threshold;
	}
	
	public String ValidateResult(int value){
		if(value==-1){
			return "null:4";
		}
		else{
			return String.valueOf(value);
		}
	}
	
	
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem, Table table) {
		if(table instanceof Table2060){
			Table2060 table2060 = (Table2060) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode,16);
			switch(icode){
			default:
			case 0x80c01://load control status
				ansiDataItem.resultData=ValidateResult(this.loadControlStatus);
				break;
			case 0x80c02://power threshold
				
				if(this.powerThreshold%100==0){
					ansiDataItem.resultData=String.valueOf((int)((float)this.powerThreshold/100));
				}
				else{
					ansiDataItem.resultData=String.valueOf((float)this.powerThreshold/100);
				}
				break;
			case 0x80c03://lag of enter load control
				ansiDataItem.resultData=ValidateResult(this.lagToEnterLoadControl);
				break;
			case 0x80c04://lag of exit load control
				ansiDataItem.resultData=ValidateResult(this.lagToExitLoadControl);
				break;
			case 0x80c05://threshold times of over load
				ansiDataItem.resultData=ValidateResult(this.TimesOfOverLoad);
				break;
			case 0x80c06://threshold of continuous not entering over load
				ansiDataItem.resultData=ValidateResult(this.lagOfContinuousOverLoad);
				break;
			}
		}
		else{
			System.out.println("invalid table parameter");
		}
		return ansiDataItem;
	}
	@Override
	public AnsiDataItem getIndex(AnsiDataItem ansiDataItem, Table table,
			AnsiContext context) {
		ansiDataItem.offset="000000";
		ansiDataItem.count="000B";
		return ansiDataItem;
	}
	@Override
	public AnsiDataItem encode(AnsiDataItem ansiDataItem, Table table,
			AnsiContext context) {
		int icode=Integer.parseInt(ansiDataItem.dataCode,16);
		switch(icode){
		default:
		case 0x80c01://load control status
			ansiDataItem.data=StringUtil.PadLeftForStr(ansiDataItem.data, 2, "0");
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000000";
			ansiDataItem.count="0001";
			break;
		case 0x80c02://power threshold
			ansiDataItem.data=StringUtil.PadLeftForStr(Integer.toHexString((int)(Float.parseFloat(ansiDataItem.data)*100) ),4,"0");
			
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000001";
			ansiDataItem.count="0002";
			break;
		case 0x80c03://lag of enter load control
			ansiDataItem.data=StringUtil.PadLeftForStr(Integer.toHexString(Integer.parseInt(ansiDataItem.data) ),4,"0");
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000003";
			ansiDataItem.count="0002";
			break;
		case 0x80c04://lag of exit load control
			ansiDataItem.data=StringUtil.PadLeftForStr(Integer.toHexString(Integer.parseInt(ansiDataItem.data) ),4,"0");
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000005";
			ansiDataItem.count="0002";
			break;
		case 0x80c05://threshold times of over load
			ansiDataItem.data=StringUtil.PadLeftForStr(Integer.toHexString(Integer.parseInt(ansiDataItem.data) ),4,"0");
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000007";
			ansiDataItem.count="0002";
			break;
		case 0x80c06://threshold of continuous not entering over load
			ansiDataItem.data=StringUtil.PadLeftForStr(Integer.toHexString(Integer.parseInt(ansiDataItem.data) ),4,"0");
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000009";
			ansiDataItem.count="0002";
			break;
		}
		return ansiDataItem;
	}
	public int getPowerThreshold() {
		return powerThreshold;
	}
	public void setPowerThreshold(int powerThreshold) {
		this.powerThreshold = powerThreshold;
	}
	public int getLagToEnterLoadControl() {
		return lagToEnterLoadControl;
	}
	public void setLagToEnterLoadControl(int lagToEnterLoadControl) {
		this.lagToEnterLoadControl = lagToEnterLoadControl;
	}
	public int getLagToExitLoadControl() {
		return lagToExitLoadControl;
	}
	public void setLagToExitLoadControl(int lagToExitLoadControl) {
		this.lagToExitLoadControl = lagToExitLoadControl;
	}
	public int getTimesOfOverLoad() {
		return TimesOfOverLoad;
	}
	public void setTimesOfOverLoad(int timesOfOverLoad) {
		TimesOfOverLoad = timesOfOverLoad;
	}
	public int getLagOfContinuousOverLoad() {
		return lagOfContinuousOverLoad;
	}
	public void setLagOfContinuousOverLoad(int lagOfContinuousOverLoad) {
		this.lagOfContinuousOverLoad = lagOfContinuousOverLoad;
	}
	private int lagToEnterLoadControl;//unit:s
	private int lagToExitLoadControl;//unit:s
	private int TimesOfOverLoad;
	private int lagOfContinuousOverLoad;//unit
	
	
}
