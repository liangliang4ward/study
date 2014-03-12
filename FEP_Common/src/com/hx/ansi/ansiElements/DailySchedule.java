package com.hx.ansi.ansiElements;

import java.io.Serializable;

import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-7-12 ÉÏÎç11:24:47
 * @version 1.0 
 */

public class DailySchedule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 12225010001L;


	public int sat;
	public int sun;
	public int week;
	public int sp;
	public int season;
	public DailySchedule(){
		
	}
	public DailySchedule(String s,int i){
		this.sat=Integer.parseInt(s.substring(0, 2), 16);
		this.sun=Integer.parseInt(s.substring(2, 4), 16);
		this.week=Integer.parseInt(s.substring(4, 6), 16);
		this.sp=Integer.parseInt(s.substring(6, 8), 16);
		this.season=i;
	}

	public String encodeDailySch(int sat,int sun,int week,int sp,int season){
		String dailySch=AnsiDataSwitch.parseInt2HexString(sat)+AnsiDataSwitch.parseInt2HexString(sun)+
						AnsiDataSwitch.parseInt2HexString(week)+AnsiDataSwitch.parseInt2HexString(sp);
		return dailySch;
	}
	public static void main(String[] args) {
		String s="00010203";// 01020300 02030001 03000102";
		
		
		
	}
}
