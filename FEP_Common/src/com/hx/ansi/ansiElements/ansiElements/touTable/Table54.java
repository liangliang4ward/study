package com.hx.ansi.ansiElements.ansiElements.touTable;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.DailySchedule;
import com.hx.ansi.ansiElements.NonRecurrDate;
import com.hx.ansi.ansiElements.RecurrDate;
import com.hx.ansi.ansiElements.TierSwitch;
import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  Table 54 日历表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:07:49
 * @version 1.0 
 */

public class Table54 extends Table{
	private static Logger log=Logger.getLogger(Table54.class);
	public String NON_RECURR_DATES;//非周期行执行
	public String RECURR_DATES;//周期性执行
	public String TIER_SWITCHES ;//费率中的日时段
	public String DAILY_SCHEDULE_ID_MATRIX;//五个工作日中日表 
	public Map<Integer,NonRecurrDate> NonRecurrDatesMap=new HashMap<Integer, NonRecurrDate>();
	public Map<Integer,RecurrDate> RecurrDatesMap=new HashMap<Integer,RecurrDate>();
	public Map<Integer,TierSwitch> TierSwitchesMap=new HashMap<Integer,TierSwitch>();
	public Map<Integer,DailySchedule> DailyScheduleMap=new HashMap<Integer,DailySchedule>();
	public String resultNonRecurrDates="";
	public String resultRecurrDates="";//,,,#,,,
	public String resultTierSwitches="";
	public String resultDailySchedule="";
	
	
	@Override
	public void decode() {
		
	}
	@Override
	public void decode(String data){
		RECURR_DATES=data.substring(0, 40*3*2);
//		for(int i=0;i<40;i++){
//			RecurrDate r=new RecurrDate(RECURR_DATES.substring(6*i, 6+6*i),i);
//			RecurrDatesMap.put(i, r);
//		}
		data=data.substring(40*3*2);
		TIER_SWITCHES=data.substring(0, 50*3*2);
		data=data.substring(50*3*2);
		DAILY_SCHEDULE_ID_MATRIX=data.substring(0, 4*4*2);
		data.substring(4*4*2);
	}
	public void decode(AnsiContext context ,String data){
		int NON_RECURR_DATES_LEN=context.table51.NBR_NON_RECURR_DATES*4;// 4=3个字节时间（YYMMDD）+1字节ACTION
		int RECURR_DATES_LEN=context.table51.NBR_RECURR_DATES*3;//3=2个字节REDATE+1个字节ACTION
		int TIER_SWITCHES_LEN=context.table51.NBR_TIER_SWITCHES*3;//3=tier 2个字节+SCH_NUM;
//		int DAILY_SCHEDULE_ID_MATRIX_LEN=context.table51.numSeasons*(context.table51.specialSchedule+7);
		int DAILY_SCHEDULE_ID_MATRIX_LEN=context.table51.numSeasons*(context.table51.specialSchedule+3);
		//non recurring dates
		NON_RECURR_DATES=data.substring(0, NON_RECURR_DATES_LEN*2);
		for(int i=0;i<context.table51.NBR_NON_RECURR_DATES;i++){
			NonRecurrDate r=new NonRecurrDate(NON_RECURR_DATES.substring(8*i, 8+8*i),i);
			NonRecurrDatesMap.put(i, r);
		}
		data=data.substring(NON_RECURR_DATES_LEN*2);
		//recurring dates
		RECURR_DATES=data.substring(0, RECURR_DATES_LEN*2);
		String RECURR_DATE="";
		for(int i=0;i<context.table51.NBR_RECURR_DATES;i++){
			RECURR_DATE=RECURR_DATES.substring(6*i, 6+6*i);
			if(AnsiDataSwitch.isAllFF(RECURR_DATE,RECURR_DATE.length())){
				continue;
			}
			RecurrDate r=new RecurrDate(RECURR_DATE,i);
			RecurrDatesMap.put(i, r);
			resultRecurrDates=resultRecurrDates+r.sdate+","+(r.action)+"#";
		}
		if(resultRecurrDates.length() > 0 )
			resultRecurrDates=resultRecurrDates.substring(0, resultRecurrDates.length()-1);
		data=data.substring(RECURR_DATES_LEN*2);
		//tier switches
		TIER_SWITCHES=data.substring(0, TIER_SWITCHES_LEN*2);
		String TIER_SWITCH="";
		for(int i=0;i<context.table51.NBR_TIER_SWITCHES;i++){
			TIER_SWITCH=TIER_SWITCHES.substring(6*i, 6+6*i);
			if(AnsiDataSwitch.isAllFF(TIER_SWITCH,TIER_SWITCH.length())){
				continue;
			}
			TierSwitch r=new TierSwitch(TIER_SWITCH,i);
			TierSwitchesMap.put(i, r);
			resultTierSwitches=resultTierSwitches+r.sdate+","+r.tier+","+r.Sch_num+"#";
		}
		if(resultTierSwitches.length() > 0)
			resultTierSwitches=resultTierSwitches.substring(0, resultTierSwitches.length()-1);
		data=data.substring(TIER_SWITCHES_LEN*2);
		//daily schedule
		DAILY_SCHEDULE_ID_MATRIX=data.substring(0, DAILY_SCHEDULE_ID_MATRIX_LEN*2);
		String DAILY_SCHEDULE="";
		for(int i=0;i<context.table51.numSeasons;i++){
			DAILY_SCHEDULE=DAILY_SCHEDULE_ID_MATRIX.substring(8*i, 8+8*i);
			if(AnsiDataSwitch.isAllFF(DAILY_SCHEDULE,DAILY_SCHEDULE.length())){
				continue;
			}
			DailySchedule r=new DailySchedule(DAILY_SCHEDULE,i);
			DailyScheduleMap.put(i, r);
			resultDailySchedule=resultDailySchedule+r.season+","+r.sun+","+r.week+","+r.sat+","+r.sp+"#";
		}
		if(resultDailySchedule.length() > 0)
			resultDailySchedule=resultDailySchedule.substring(0, resultDailySchedule.length()-1);
		data.substring(DAILY_SCHEDULE_ID_MATRIX_LEN*2);
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem, Table table){
		Table54 table54=(Table54)table;
		int icode=Integer.parseInt(ansiDataItem.dataCode);
		switch(icode){
		case 75404:
			ansiDataItem.resultData=table54.resultRecurrDates+";"+table54.resultTierSwitches+";"+table54.resultDailySchedule;
		}
		return ansiDataItem;
	}
	
	@Override
	public  AnsiDataItem encode(AnsiDataItem ansiDataItem, Table table, AnsiContext context){
		int icode=Integer.parseInt(ansiDataItem.dataCode);
		int len=0;
		String FF="";
		switch(icode){
		case 75400:
			//目前表计不支持
			break;
		case 75401:
			RecurrDate [] recurrDates=ansiDataItem.RecurrDates;
			String SrecurrDates="";
			int i_len=0;//计算有效的recurrDates长度
			for(int i=0;i<recurrDates.length;i++){
				if(recurrDates[i] != null){
					i_len++;
				}
			}
			for(int i=0;i<i_len;i++){
				SrecurrDates=SrecurrDates+recurrDates[i].encodeRecurrDates(recurrDates[i].sdate, recurrDates[i].action);
			}
			ansiDataItem.data=SrecurrDates.toUpperCase();
			len=ansiDataItem.data.length();//数据的长度
			FF=AnsiDataSwitch.getFF(228-len);
			ansiDataItem.data=ansiDataItem.data+FF;//不足的补全FF
//			ansiDataItem.count="0000".substring(len.length())+len;
			ansiDataItem.count="0072";
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000006";
			break;
		case 75402:
			TierSwitch []TierSwitches=ansiDataItem.TierSwitches;
			String STierSwitches="";
			int ii_len=0;//计算有效的TierSwitches长度
			for(int i=0;i<TierSwitches.length;i++){
				if(TierSwitches[i] != null){
					ii_len++;
				}
			}
			for(int i=0;i<ii_len;i++){
				STierSwitches=STierSwitches+TierSwitches[i].encodeTierSwitches(TierSwitches[i].sdate, TierSwitches[i].tier, TierSwitches[i].Sch_num);
			}
			ansiDataItem.data=STierSwitches.toUpperCase();
			len=ansiDataItem.data.length();//数据的长度
			FF=AnsiDataSwitch.getFF(300-len);
			ansiDataItem.data=ansiDataItem.data+FF;//不足的补全FF
//			ansiDataItem.count="0000".substring(len.length())+len;
			ansiDataItem.count="0096";
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="000078";
			break;
		case 75403:
			DailySchedule []dailySchedules=ansiDataItem.DailySchedules;
			String SdailySches="";
			int iii_len=0;//计算有效的dailySchedules长度
			for(int i=0;i<dailySchedules.length;i++){
				if(dailySchedules[i] != null){
					iii_len++;
				}
			}
			for(int i=0;i<iii_len;i++){
				SdailySches=SdailySches+dailySchedules[i].encodeDailySch(dailySchedules[i].sat, dailySchedules[i].sun, dailySchedules[i].week, dailySchedules[i].sp, dailySchedules[i].season);
			}
			ansiDataItem.data=SdailySches.toUpperCase();
			len=ansiDataItem.data.length();//数据的长度
			FF=AnsiDataSwitch.getFF(32-len);
			ansiDataItem.data=ansiDataItem.data+FF;//不足的补全FF
//			ansiDataItem.count="0000".substring(len.length())+len;
			ansiDataItem.count="0010";
			ansiDataItem.data=ansiDataItem.data+HexDump.toHex(AnsiDataSwitch.calculateCS(HexDump.toArray(ansiDataItem.data), 0, HexDump.toArray(ansiDataItem.data).length));
			ansiDataItem.offset="00010E";
			break;
		default:
			log.error("encode table54 error...");
				break;
		}

		
		return ansiDataItem;	
	}
	
	public String getTierSwitches(String data){
		data="111,222,333,444#555,666,777,888#999,000,111,222";//"tier,hour,min,Schedule#tier,hour,min,Schedule"
		String []TierSwitches=data.split("#");
		for(int i=0;i<TierSwitches.length;i++){
			String [] TierSwitch=TierSwitches[i].split(",");
			for(int j=0;j<TierSwitch.length;j++){
				
				
				
				
			}
		}
		
		
		return data;
	}
	
	
	public static void main(String[] args) {
		//小端：0308610A08620108630408640708650A086601081301101302401304801304D81305081305101306C8130878130AF8130CC8130CD013FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000001180002300003480000600001780002900003A80001000102180103300100480101600102780103900100A80102000203180201300200480202600203780201900200A80203000300180301300302480303600300780301900302A803FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00010203010203000203000103000102
		//大端:080361080A62080163080464080765080A66080113100113400213800413D80413080513100513C80613780813F80A13C80C13D00C13FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF000000180100300200480300600000780100900200A80300000101180201300301480001600101780201900301A80001000202180302300102480002600202780302900102A80002000303180003300103480203600303780003900103A80203FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00010203010203000203000103000102
		
		String s="080361080A62080163080464080765080A66080113100113400213800413D80413080513100513C80613780813F80A13C80C13D00C13FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF000000180100300200480300600000780100900200A80300000101180201300301480001600101780201900301A80001000202180302300102480002600202780302900102A80002000303180003300103480203600303780003900103A80203FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00010203010203000203000103000102";
		System.out.println(s.length());
//		Table54 t=new Table54();
//		t.decode(s);
		
		
	}


}
