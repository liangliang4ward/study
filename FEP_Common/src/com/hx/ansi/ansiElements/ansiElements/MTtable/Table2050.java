package com.hx.ansi.ansiElements.ansiElements.MTtable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.StringUtil;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-7-25 下午04:27:49
 * @version 1.0 
 */

public class Table2050 extends Table{
	
	private static final Logger log = Logger.getLogger(Table2050.class);

	
	
	public int LIST_STATUS;
	public boolean enablseFreeze=false;
	public int rangeType=1;
	public boolean isOverflow=false;
	public int rangeTransfer=0;
	public int NBR_VALID_ENTRIES;
	public int LAST_ENTRY_ELEMENT;
	public int LAST_ENTRY_SEQ_NBR;
	public int NBR_UNREAD_ENTRIES;
	public int SELF_READS_ENTRIES;
	public Date date;
	public String season;
	public String resettimes;
	 public Map<String,String> taskDataMap=new HashMap<String,String>();
	
	@Override
	public void decode() {
		enablseFreeze=((LIST_STATUS&8)>>3)==0?false:true;
		isOverflow=((LIST_STATUS&2)>>1)==0?false:true;
		rangeTransfer=LIST_STATUS&1;//0—升序排列（第N条发生在第N+1条前）,1—降序排列
	}
	public void decode(AnsiContext context,String data){
//		LIST_STATUS=Integer.parseInt(data.substring(0, 2), 16);
//		NBR_VALID_ENTRIES=Integer.parseInt(data.substring(2, 4), 16);
//		LAST_ENTRY_ELEMENT=0;
//		LAST_ENTRY_SEQ_NBR=0;
//		NBR_UNREAD_ENTRIES=0;
		String ssdate=data.substring(0, 10);
		ssdate=AnsiDataSwitch.hexToString(ssdate);
		SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmm");
		try {
			date=sdf.parse(ssdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		season=data.substring(10, 12);
		resettimes=data.substring(12, 14);
		data=data.substring(14);
		int  count =getCount(context);//每一个数据项数据的长度
		int  countDL=count;//电量数据不带时间，等于数据长度
		int  countXL=count+5+4;//需量数据带有5个字节时间
		int PackageDL=(context.table21.NBR_SUMMATIONS)*countDL;//电量包的长度
		int PackageXL=(context.table21.NBR_DEMANDS)*countXL;//需量包的长度
		for(int i=0;i<=4;i++){
			String sdata="";
			sdata=data.substring(2*i*(PackageDL+PackageXL),2*i*(PackageDL+PackageXL)+ 2*(PackageDL+PackageXL));
			String summationsData=sdata.substring(0, PackageDL*2);//电量数据
			String demandsData=sdata.substring(PackageDL*2);
			for(int k=0;k<summationsData.length()/8;k++){
				String ssdata="";
				String value="";
				ssdata=summationsData.substring(8*k, 8*k+8);//取出数据
				if(AnsiDataSwitch.isAllFF(ssdata, ssdata.length())){ssdata=ssdata.replace("F", "0");}//全ff处理为00?
				double dresult=Long.parseLong(ssdata, 16)/1000.0;//数据除以量纲
				value=String.valueOf(dresult);
				value=AnsiDataSwitch.getDouble(value, 2);
				String code=context.table12.dataItemMap.get(context.table22.selectMap.get(k));
				this.taskDataMap.put(code.substring(0, 7)+i, value);
			}
			for(int k=0;k<demandsData.length()/26;k++){
				String ssdata="";
				String value="";
				String sdate="";
				ssdata=demandsData.substring(26*k, 26*k+26);//取出数据,包含有5个字节时间+数据
				if(AnsiDataSwitch.isAllFF(ssdata, ssdata.length())){ssdata=ssdata.replace("F", "0");}
				SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmm");
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date=new Date();
				try{
					date=df.parse(AnsiDataSwitch.hexToString(ssdata.substring(0,10)));
					sdate=sf.format(date);
				}catch(ParseException e){
					log.error(StringUtil.getExceptionDetailInfo(e));
				}
				double dresult=Long.parseLong(ssdata.substring(18,26), 16)/1000.0;//数据除以量纲
				value=String.valueOf(dresult);
				value=AnsiDataSwitch.getDouble(value, 2);
				String code=context.table12.dataItemMap.get(context.table22.selectMap.get(context.table21.NBR_SUMMATIONS+k));
				if(sdate.equals("1999-11-30 00:00:00")){
					sdate="";
					value="";
				}
				this.taskDataMap.put(code.substring(0, 7)+i+"00", value);
				this.taskDataMap.put(code.substring(0, 7)+i+"01", sdate);
			}
		}
	
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AnsiDataItem getIndex(AnsiDataItem ansiDataItem, Table table, AnsiContext context) {
		Date date=ansiDataItem.date;
		Date now=new Date();
		int i=0;
		if(now.after(date)){
			i=(int) ((now.getTime()-date.getTime())/(1000*60*60*24*30));
		}
		//针对数据长度为467
		int offset=6+467*i;
		String soffset=AnsiDataSwitch.parseInt2HexString(offset);
		ansiDataItem.offset="000000".substring(soffset.length())+soffset;
		ansiDataItem.count="01D3";//387=5+1+(1+460)
		return ansiDataItem;
	}
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
		
		
	}
}
	
