package com.hx.ansi.ansiElements.ansiElements.basicTable;

import com.hx.ansi.ansiElements.AnsiDataItem;
import com.hx.ansi.ansiElements.ansiElements.Table;


/** 
 * @Description  Table 3 状态表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午02:29:15
 * @version 1.0 
 */

public class Table3 extends Table{

	public int ED_MODE_BFLD ;
	public boolean isFactory=false;//是否出厂模式
	public boolean isStore=false;//是否仓储
	public boolean isTest=false;//表计是否处于测试模式
	public boolean isWork=false;//表计是否处于计量模式
	public int ED_STD_STATUS1;
	public boolean isReverse=false;//是否检测到反转
	public boolean isSteal=false;	//是否处于窃电状态
	public boolean isPower_down=false;//是否检测到掉电	
	public boolean isOverload=false;	//是否检测到超负载
	public boolean isLowThreshold=false;	//是否检测到某个量低于某个设定阀值
	public boolean isBattery_voltageLow=false;//是否检测到电池欠压	
	public boolean isMeasureError=false;	//是否有计量出错
	public boolean isTimeError=false;//是否有时钟出错
	public boolean isMemoryError=false;//是否有非易失性存储出错
	public boolean isROMError=false;//ROM是否出错
	public boolean isRAMError=false;//RAM是否出错
	public boolean isSelfTestError=false;//自检出错
	public boolean isConfigError=false;//配置错误
	public boolean isProgramError=false;//是否被编程过	0—被编程过；1—未被编程过或出厂状态
	public int ED_STD_STATUS2;//保留
	public int ED_MFG_STATUS;//
	public String relayStatus="00";
	
	@Override
	public void decode() {
		isFactory=((ED_MODE_BFLD&8)>>>3)==0?false:true;
		isStore=((ED_MODE_BFLD&4)>>>2)==0?false:true;
		isTest=((ED_MODE_BFLD&2)>>>1)==0?false:true;
		isWork=(ED_MODE_BFLD&1)==0?false:true;
		isReverse=((ED_STD_STATUS1&8192)>>>13)==0?false:true;
		isSteal=((ED_STD_STATUS1&4096)>>>12)==0?false:true;
		isPower_down=((ED_STD_STATUS1&2048)>>>11)==0?false:true;
		isOverload=((ED_STD_STATUS1&1024)>>>10)==0?false:true;
		isLowThreshold=((ED_STD_STATUS1&512)>>>9)==0?false:true;
		isBattery_voltageLow=((ED_STD_STATUS1&256)>>>8)==0?false:true;
		isMeasureError=((ED_STD_STATUS1&128)>>>7)==0?false:true;
		isTimeError=((ED_STD_STATUS1&64)>>>6)==0?false:true;
		isMemoryError=((ED_STD_STATUS1&32)>>>5)==0?false:true;
		isROMError=((ED_STD_STATUS1&16)>>>4)==0?false:true;
		isRAMError=((ED_STD_STATUS1&8)>>>3)==0?false:true;
		isSelfTestError=((ED_STD_STATUS1&4)>>>2)==0?false:true;
		isConfigError=((ED_STD_STATUS1&2)>>>1)==0?false:true;
		isProgramError=(ED_STD_STATUS1&1)==0?false:true;
		relayStatus=(ED_STD_STATUS2&32)>>>5==1?"00":"01";//true duankai
		
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	public void decode(String data){
		ED_MODE_BFLD=Integer.parseInt(data.substring(0, 2), 16);
		ED_STD_STATUS1=Integer.parseInt(data.substring(2, 6), 16);
		ED_STD_STATUS2=Integer.parseInt(data.substring(8, 10), 16);
		decode();
	}
	@Override
	public AnsiDataItem getResult(AnsiDataItem ansiDataItem,Table table) {
		if(table instanceof Table3){
			Table3 table3 = (Table3) table;
			int icode=Integer.parseInt(ansiDataItem.dataCode);
			switch(icode){
			case 20319://code:00010001
				ansiDataItem.resultData=table3.relayStatus;
				break;			
			}
		}else{
			System.out.println("错误的table参数");
		}
		return ansiDataItem;
	}
	public static void main(String[] args) {
		Table3 t=new Table3();
		t.decode("0100000020");
	}
	
	
}
