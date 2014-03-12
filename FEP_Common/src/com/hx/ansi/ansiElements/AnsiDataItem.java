package com.hx.ansi.ansiElements;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hx.ansi.ansiElements.ansiElements.AnsiCommandResult;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-19 下午02:12:07
 * @version 1.0 
 */

public class AnsiDataItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1123654L;

	public String dataCode;//数据项
	public int dataType;//数据类型    0 -基本数据  
						//	    1-电量数据   需量数据  
 						//		2-实时量
	public int tiers;//总 -0   费率1-1 。。。。
	public long cmdId = -1L; //主站下行带命令id，上行数据解析完根据数据id入库
	public String data;//下行带参数
	public TierSwitch[] TierSwitches=null;
	public NonRecurrDate[] NonRecurrDates=null;
	public RecurrDate[]RecurrDates=null;
	public DailySchedule[]DailySchedules=null;
	public String resultData;//上行终端返回数据
	public String offset;//偏移读取的时候带偏移量
	public String count;//偏移字节数
	public Date date=null;
	public int index;//在对应块中的位置
	public int length; //数据长度
	public List<AnsiCommandResult> commandResult;
	public AnsiTaskInf taskInf;
	
	//for load profile
	public Date startTime=null;
	public Date endTime=null;
	public boolean readLatestDate=false;
	public boolean readData=false;
}
