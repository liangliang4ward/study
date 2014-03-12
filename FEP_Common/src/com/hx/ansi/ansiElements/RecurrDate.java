package com.hx.ansi.ansiElements;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-7-12 上午11:24:35
 * @version 1.0 
 */

public class RecurrDate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1025478812021L;


	public int rdate;
	public int action;
	public int index;
	public int bit03;
	public int period_in_min;
	public int weekday;
	public int day;
	public int delta;
	public int offset;
	public int date;
	public boolean reset;
	public boolean selfRead;
	public int bitAction;
	public String sdate;
	public Date Date;
	
	public RecurrDate(){
		
	}
	public RecurrDate(String s,int i){
		this.rdate=Integer.parseInt(s.substring(0, 4), 16);
		this.action=Integer.parseInt(s.substring(4, 6), 16);
		if(this.action-96>=0){
			this.action=this.action-96;
		}
//		if(this.action>2&&this.action<18){
//			this.action=this.action-3;//季节-3
//		}
		this.index=i;
		decodeRecurrDates(rdate,action);
	}
	
	public void decodeRecurrDates(int rdate, int action){
		 this.bit03=rdate&15;
		switch (this.bit03) {
		case 0:
			this.period_in_min=(rdate&65520)>>>4;
			break;
		case 14:
			this.weekday=(rdate&1792)>>>8;
			break;
		case 15:
			this.day=(rdate&1008)>>>4;
			this.delta=(rdate&64512)>>>10;
			break;
		default:
			this.offset=(rdate&240)>>>4;
			this.weekday=(rdate&1792)>>>8;
			this.date=(rdate&63488)>>>11;
			break;
		}
		this.reset=((action&64)>>>6)==0?false:true;
		this.selfRead=((action&32)>>>5)==0?false:true;
		this.bitAction=action&31;
		switch (this.bitAction) {
		case 0:
			//无动作
			break;
		case 1:
			//切换到夏令时
			break;
		default:
			break;
		}
		this.sdate=this.bit03+"-"+this.date;
		
	}
	public String encodeRecurrDates(String sdate,int Action){
//		SimpleDateFormat sdf= new SimpleDateFormat("MM-dd");
//		String sdate=sdf.format(date);
		this.bit03=Integer.parseInt(sdate.substring(0, 2));
		this.date=Integer.parseInt(sdate.substring(3, 5));
		String recurrDate=AnsiDataSwitch.parseInt2HexString((this.date<<11)+bit03)+AnsiDataSwitch.parseInt2HexString(96+Action);
		return recurrDate;
	}
	

	
	
}
