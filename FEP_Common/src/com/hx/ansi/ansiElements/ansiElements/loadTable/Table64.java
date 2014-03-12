package com.hx.ansi.ansiElements.ansiElements.loadTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

//import cn.hexing.fas.model.AnsiRequest;
//import cn.hexing.fk.bp.ansi.events.AnsiEvent;
import cn.hexing.fk.utils.CalendarUtil;
import cn.hexing.fk.utils.StringUtil;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  load profile 1
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:09:19
 * @version 1.0 
 */

public class Table64 extends Table{
	
	private static final Logger log = Logger.getLogger(Table64.class);

	
	public Date date;
//	public 	List <AnsiTaskData> tasks;
	public String taskMessageData;
	public Map<Date,Integer> pointMap=new HashMap<Date,Integer>();
	
	@Override
	public void decode() {
		
	}
	@Override
	public void decode(String data){
		String sdate=data.substring(0, 10);
		sdate=AnsiDataSwitch.hexToString(sdate);
		SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmm");
		try {
			date=sdf.parse(sdate);
		} catch (ParseException e) {
			log.error(StringUtil.getExceptionDetailInfo(e));
		}
		taskMessageData=data.substring(10);
	}
	public void decode(AnsiContext context,String data){
//		date=data.substring(0, 10);
//		date=AnsiDataSwitch.hexToString(date);
//		SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmm");
//		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			date=sd.format(sdf.parse(date));
//		} catch (ParseException e) {
//			log.error(StringUtil.getExceptionDetailInfo(e));
//		}
//		taskMessageData=data.substring(10);
//		int  count =getCount(context);//每一个数据项数据的长度
//		int pointLen=4*context.table61.NBR_CHNS_SETx*2;
//		List <AnsiTaskData> tasks=new ArrayList<AnsiTaskData>();
//		for(int i=0;i<data.length()/pointLen;i++){
//			String sdata="";
//			sdata=data.substring(pointLen*i, pointLen+pointLen*i);
//			AnsiTaskData task=new AnsiTaskData();
//			for(int j=0;j<sdata.length()/8;j++){
//				String ssdata="";
//				String code="";
//				String value="";
//				AnsiTaskDataItem item=new AnsiTaskDataItem();
//				code=context.table12.dataItemMap.get(context.table62.LP_SEL_SETx.get(j).index);
//				ssdata=sdata.substring(8*i, 8*i+8);//取出数据
//				double dresult=Long.parseLong(ssdata, 16)*(context.table12.paramMap.get(context.table62.LP_SEL_SETx.get(j).index).multiplier);
//				value=String.valueOf(dresult);
//				value=AnsiDataSwitch.getDouble(value, 2);
//				item.setCode(code);
//				item.setValue(value);
//				task.addDataList(item);
//			}
//			tasks.add(task);
//		}
//		this.tasks=tasks;
	}	
		
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
	public void getResult(){
		
	}
	
	@Override
	public AnsiDataItem getIndex(AnsiDataItem ansiDataItem, Table table, AnsiContext context) {
		
//		AnsiEvent de = (AnsiEvent) context.webReqList.get(0);
//		AnsiRequest dr=(AnsiRequest) de.getRequest();
		
		
		String offset="";
		String scount="";
		
			String string="";
			//length of one record,every channel contains 8 dataitem
			//int oneRecordCount =getCount(context)*8;
			int oneRecordCount =4*8;
			//length of one package,contains all the data in one day
			int packageLen=oneRecordCount*4*24+5;
			
			//read the load profile data	
			if(null!=ansiDataItem.dataCode&&"00005200".equals(ansiDataItem.dataCode)){
				Date startDate=ansiDataItem.startTime;
				Date endDate=ansiDataItem.endTime;
				Date lastestDate=new Date();
				log.info("============================latest date:"+context.flag);
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyMMddHHmm");
				try {
					lastestDate=simpleDateFormat.parse(AnsiDataSwitch.hexToString(context.flag.substring(0,10)));
					
				} catch (Exception e) {
					e.printStackTrace();
					lastestDate=CalendarUtil.getBeginOfToday().getTime();
				}
				
				int startIndex=GetStartIndex(startDate);
				int indexDiff=GetIndexDiff(startDate, endDate);
				int packageCount=GetPackageLength(startDate, lastestDate);
				log.info("Create Load Profile Index ================="+lastestDate.toString()
						+"\r\nStartIndex========="+startIndex
						+"\r\nindexDiff========="+indexDiff
						+"\r\npackageCount========="+packageCount
						);
				offset=Integer.toHexString(5+packageCount*packageLen+oneRecordCount*startIndex);
				scount=Integer.toHexString(indexDiff*oneRecordCount);
				
			}else{//read the  date of the latest block
				offset=Integer.toHexString(0);
				scount=Integer.toHexString(5);
			}
			
			ansiDataItem.offset="000000".substring(offset.length())+offset;
			ansiDataItem.count="0000".substring(scount.length())+scount;
			return ansiDataItem;
	}
	
	public int GetPackageLength(Date startDate,Date endDate) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		try {
			startDate=sdf.parse(sdf.format(startDate));
			endDate=sdf.parse(sdf.format(endDate));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int i=0;
		if(endDate.after(startDate)){
			i=(int) ((endDate.getTime()-startDate.getTime())/(1000*60*60*24));
		}
		return i;
	}
	
	public int GetIndexDiff(Date startDate,Date endDate) {
		int i=0;
		if(endDate.after(startDate)){
			i=(int) ((endDate.getTime()-startDate.getTime())/(1000*60*15));
		}
		return (i+1);
	}
	
	public int GetStartIndex(Date startDate) {
		Date date=new Date();
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd");
		try {
			date=sdf.parse(sdf.format(startDate));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);

		for(int i=0;i<96;i++){
			if(i==0){
				pointMap.put(calendar.getTime(), i);
			}else{
				calendar.add(Calendar.MINUTE, +15);
				pointMap.put(calendar.getTime(), i);	
			}
		}
		int point=pointMap.get(startDate);
		return point;
	}
	
	
//	@Override
//	public AnsiDataItem getIndex(AnsiDataItem ansiDataItem, Table table, AnsiContext context) {
//			int count =32;//32=8个数据项*每个数据项长度4个字节
//			int packageLen=count*4*24+5;
//
//			Date date=ansiDataItem.date;
//			//初始化任务点
//			Calendar calendar=Calendar.getInstance();
//			SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd");
//			try {
//				date=sdf.parse(sdf.format(date));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			calendar.setTime(date);
//			for(int i=0;i<96;i++){
//				if(i==0){
//					pointMap.put(calendar.getTime(), i);
//				}else{
//					calendar.add(Calendar.MINUTE, +15);
//					pointMap.put(calendar.getTime(), i);	
//				}
//			}
//			int point=pointMap.get(ansiDataItem.date);
//			Date now=new Date();
//			int i=0;
//			if(now.after(date)){
//				i=(int) ((now.getTime()-date.getTime())/(1000*60*60*24));
//			}
//			String offset="";
//			String scount="";
//
//			if(null!=ansiDataItem.dataCode&&"00005200".equals(ansiDataItem.dataCode)){
//				offset=Integer.toHexString(packageLen*i);
//				scount=Integer.toHexString(5);
//			}else{
//				offset=Integer.toHexString(5+packageLen*i+count*point);
//				scount=Integer.toHexString(count);
//			}
//			
//			ansiDataItem.offset="000000".substring(offset.length())+offset;
//			ansiDataItem.count="0000".substring(scount.length())+scount;
//			return ansiDataItem;
//	}
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
		//0D060100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
	String s="0D060100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
	Table64 t=new Table64();
	t.decode(s);
	System.out.println(s.length());
	
	
	}


}
