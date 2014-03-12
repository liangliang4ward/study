package com.hx.ansi.ansiElements;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  ANSI费率时段表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-7-12 上午09:07:45
 * @version 1.0 
 */

public class TierSwitch implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 112355444458L;
	int tierSwitch;
	public int Sch_num;
	public int hour;
	public int min;
	public int tier;
	public Date date;
	public String sdate;
	
	public TierSwitch(String s,int i){
		this.tierSwitch=Integer.parseInt(s.substring(0, 4), 16);
		this.Sch_num=Integer.parseInt(s.substring(4, 6), 16);
		decodeTierSwitches(tierSwitch, Sch_num);
	}
	public TierSwitch(){
	}
	public void decodeTierSwitches(int tierSwitch,int Sch_num){
		this.hour=(tierSwitch&63488)>>>11;
		this.min=(tierSwitch&2016)>>>5;
		this.tier=tierSwitch&7;
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		try {
			this.date=sdf.parse(AnsiDataSwitch.parseInt2String(this.hour)+":"+AnsiDataSwitch.parseInt2String(this.min));
			this.sdate=sdf.format(this.date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public String encodeTierSwitches(String sdate,int tier,int sch_num){
//		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
//		this.sdate=sdf.format(date);
		this.hour=Integer.parseInt(sdate.substring(0, 2));
		this.min=Integer.parseInt(sdate.substring(3, 5));
		String tierSwitch=AnsiDataSwitch.parseInt2HexString((hour<<11)+(min<<5)+tier)+AnsiDataSwitch.parseInt2HexString(sch_num);
		return tierSwitch;
	}
	
	public static void main(String[] args) {
		//000000 180100 300200 480300 600000
		TierSwitch t=new TierSwitch("180100",1);
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		try {
			Date d=sdf.parse(AnsiDataSwitch.parseInt2String(1)+":"+AnsiDataSwitch.parseInt2String(8));
			String sd=sdf.format(d);
			System.out.println(sd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
}
