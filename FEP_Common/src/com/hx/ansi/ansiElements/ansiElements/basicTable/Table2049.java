package com.hx.ansi.ansiElements.ansiElements.basicTable;

import cn.hexing.fk.utils.HexDump;
import cn.hexing.fk.utils.StringUtil;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

public class Table2049 extends Table{

	private int energyDecimalPoint;
	private int energyDecimalPointOnTestMode;
	private int powerDecimalPoint;
	private int voltageDecimalPoint;
	private int currentDecimalPoint;
	private int demandDecimalPoint;
	private int accumulatedDemandDecimalPoint;
	private int pulseWidthDecimalPoint;
	private int com0Baudrate;
	private int com1Baudrate;
	private int activeEnergyMeasureType;
	private int reactiveEnergyMeasureType;
	private int timeToCloaseLedAfterWakeup;
	private int pulseConstant;
	private int pulseConstantOnFactoryMode;
	private int pulseConstantOnTestMode;
	private String monthlyFrozenTime;
	
	@Override
	public AnsiDataItem getIndex(AnsiDataItem item, Table table, AnsiContext context){
		int icode=Integer.parseInt(item.dataCode);
		
		switch(icode){
		case 80117://monthly frozen time
			item.offset="00001E";
			item.count="2";
			break;
		default:
			item.offset="00001E";
			item.count="0002";
			break;
		}
		return item;
	}
	
	@Override
	public void decode(String data) {
		if(data.toUpperCase().equals("FFFF")){
			
			monthlyFrozenTime="0100";
		}
		else{
			data=StringUtil.PadLeftForStr(data, 4, "0");
			monthlyFrozenTime=String.valueOf(Integer.parseInt(data.substring(0,2),16))
					+String.valueOf(Integer.parseInt(data.substring(2,4),16));
		}
	}
	
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		if(table instanceof Table2049){
			Table2049 table2049 = (Table2049) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode);
			switch(icode){
			default:
			case 80117://monthly frozen time
				ansiDataItem.resultData=table2049.monthlyFrozenTime;
				break;
			}
		}
		else{
			System.out.println("´íÎóµÄtable²ÎÊý");
		}
		return ansiDataItem;
	}
	
	@Override
	public  AnsiDataItem encode(AnsiDataItem ansiDataItem, Table table, AnsiContext context){
		int icode=Integer.parseInt(ansiDataItem.dataCode);
		switch(icode){
		default:
		case 80117://monthly frozen time
			//ansiDataItem.data=ParserIP.constructor(ansiDataItem.data, 2);
			ansiDataItem.data=StringUtil.PadLeftForStr(ansiDataItem.data,4,"0");
			ansiDataItem.data=StringUtil.PadLeftForStr(Integer.toHexString(Integer.parseInt(ansiDataItem.data.substring(0,2))),2,"0")
					+StringUtil.PadLeftForStr(Integer.toHexString(Integer.parseInt(ansiDataItem.data.substring(2,4))),2,"0");
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="00001E";
			ansiDataItem.count="0002";
			break;
		}
		return ansiDataItem;
	}
	
	
	
	public int getPowerDecimalPoint() {
		return powerDecimalPoint;
	}

	public void setPowerDecimalPoint(int powerDecimalPoint) {
		this.powerDecimalPoint = powerDecimalPoint;
	}

	public int getVoltageDecimalPoint() {
		return voltageDecimalPoint;
	}

	public void setVoltageDecimalPoint(int voltageDecimalPoint) {
		this.voltageDecimalPoint = voltageDecimalPoint;
	}

	public int getCurrentDecimalPoint() {
		return currentDecimalPoint;
	}

	public void setCurrentDecimalPoint(int currentDecimalPoint) {
		this.currentDecimalPoint = currentDecimalPoint;
	}

	public int getDemandDecimalPoint() {
		return demandDecimalPoint;
	}

	public void setDemandDecimalPoint(int demandDecimalPoint) {
		this.demandDecimalPoint = demandDecimalPoint;
	}

	public int getAccumulatedDemandDecimalPoint() {
		return accumulatedDemandDecimalPoint;
	}

	public void setAccumulatedDemandDecimalPoint(int accumulatedDemandDecimalPoint) {
		this.accumulatedDemandDecimalPoint = accumulatedDemandDecimalPoint;
	}

	public int getPulseWidthDecimalPoint() {
		return pulseWidthDecimalPoint;
	}

	public void setPulseWidthDecimalPoint(int pulseWidthDecimalPoint) {
		this.pulseWidthDecimalPoint = pulseWidthDecimalPoint;
	}

	public int getCom0Baudrate() {
		return com0Baudrate;
	}

	public void setCom0Baudrate(int com0Baudrate) {
		this.com0Baudrate = com0Baudrate;
	}

	public int getCom1Baudrate() {
		return com1Baudrate;
	}

	public void setCom1Baudrate(int com1Baudrate) {
		this.com1Baudrate = com1Baudrate;
	}

	public int getActiveEnergyMeasureType() {
		return activeEnergyMeasureType;
	}

	public void setActiveEnergyMeasureType(int activeEnergyMeasureType) {
		this.activeEnergyMeasureType = activeEnergyMeasureType;
	}

	public int getReactiveEnergyMeasureType() {
		return reactiveEnergyMeasureType;
	}

	public void setReactiveEnergyMeasureType(int reactiveEnergyMeasureType) {
		this.reactiveEnergyMeasureType = reactiveEnergyMeasureType;
	}

	public int getTimeToCloaseLedAfterWakeup() {
		return timeToCloaseLedAfterWakeup;
	}

	public void setTimeToCloaseLedAfterWakeup(int timeToCloaseLedAfterWakeup) {
		this.timeToCloaseLedAfterWakeup = timeToCloaseLedAfterWakeup;
	}

	public int getPulseConstant() {
		return pulseConstant;
	}

	public void setPulseConstant(int pulseConstant) {
		this.pulseConstant = pulseConstant;
	}

	public int getPulseConstantOnFactoryMode() {
		return pulseConstantOnFactoryMode;
	}

	public void setPulseConstantOnFactoryMode(int pulseConstantOnFactoryMode) {
		this.pulseConstantOnFactoryMode = pulseConstantOnFactoryMode;
	}

	public int getPulseConstantOnTestMode() {
		return pulseConstantOnTestMode;
	}

	public void setPulseConstantOnTestMode(int pulseConstantOnTestMode) {
		this.pulseConstantOnTestMode = pulseConstantOnTestMode;
	}

	public String getMonthlyFrozenTime() {
		return monthlyFrozenTime;
	}

	public void setMonthlyFrozenTime(String monthlyFrozenData) {
		this.monthlyFrozenTime = monthlyFrozenData;
	}



	public int getEnergyDecimalPoint() {
		return energyDecimalPoint;
	}

	public void setEnergyDecimalPoint(int energyDecimalPoint) {
		this.energyDecimalPoint = energyDecimalPoint;
	}

	public int getEnergyDecimalPointOnTestMode() {
		return energyDecimalPointOnTestMode;
	}

	public void setEnergyDecimalPointOnTestMode(int energyDecimalPointOnTestMode) {
		this.energyDecimalPointOnTestMode = energyDecimalPointOnTestMode;
	}
}
