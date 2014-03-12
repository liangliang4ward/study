package com.hx.ansi.ansiElements.ansiElements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-6-5 下午03:21:05
 * @version 1.0 
 */

public class AnsiTaskData {
	private static final Logger log = Logger.getLogger(AnsiTaskData.class);
	/** 单位代码 用于任务保存*/
    //private String deptCode;
	/** 数据保存ID */
    //private String dataSaveID;
    /** 终端任务号 */
    private String taskNum;
    /** 终端任务数据属性 */
    //private String taskProperty;
    /** 终端逻辑地址（HEX） */
    private String logicAddress;
    /** 测量点号 */
    private String tn;
    /** 数据时间 */
    private Date time;
    
    /** 数据列表 */
    private List<AnsiTaskDataItem> dataList=new ArrayList<AnsiTaskDataItem>();


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[logicAddress=").append(getLogicAddress())
        	.append(", taskNum=").append(getTaskNum())
            .append(", time=").append(getTime()).append("]");
        return sb.toString();
    }
    /**
     * 添加告警参数
     * @param arg 告警参数
     */
    public void addDataList(AnsiTaskDataItem AnsiTaskDataItem) {
    	dataList.add(AnsiTaskDataItem);
    }

	public String getLogicAddress() {
		return logicAddress;
	}
	public void setLogicAddress(String logicAddress) {
		this.logicAddress = logicAddress;
	}
	public String getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public void setTime(String dt) {
		try{
			SimpleDateFormat df = null ;
			if (dt.trim().length()==16){
				df= new SimpleDateFormat("yyyy-MM-dd HH:mm");
			}
			else if (dt.trim().length()==10){
				df = new SimpleDateFormat("yyyy-MM-dd");
			}else if (dt.trim().length()==7){
				df = new SimpleDateFormat("yyyy-MM");
			}else if(dt.trim().length()==19){
				df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			this.time = df.parse(dt);
		}
		catch(Exception e){		
			log.error("setTime time="+dt+" error:"+e);
		}
	}
	public String getTimeStr() {
		String sDate="";
		try{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			sDate=df.format(time);
		}catch(Exception ex){			
		}
		return sDate;
	}
	public List<AnsiTaskDataItem> getDataList() {
		return dataList;
	}
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}
	//取特殊处理过的任务时间，当前+1天
	public Date getNextday() {
		Calendar cl = Calendar.getInstance();
		cl.setTime(time);
		cl.add(Calendar.DATE, +1);
		return cl.getTime();
	}

}
