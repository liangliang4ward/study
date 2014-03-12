package com.hx.ansi.ansiElements.ansiElements.dataTable;

import java.util.HashMap;
import java.util.Map;

import com.hx.ansi.ansiElements.ansiElements.Table;

/** 
 * @Description 	Table 16 数据源定义表
 * 			Table 16用来类2中所有寄存器数据的溯源
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午03:28:50
 * @version 1.0 
 */

public class Table16 extends Table{
    /** 索引和元素值对应表 */
    public  Map<Integer,SourceLinkBfld> paramMap=new HashMap<Integer,SourceLinkBfld>();

	
	@Override
	public void decode() {

	}
//19 19 19 19 19 19 19 19 19 19 19 19 19 19 19 19 19 19 19 19 19 19 11 11 11 11 11 11 11 11 11 11 12 12 12 12 12 12 12 12 12 12
	public void decode(String data,int i){
		SourceLinkBfld slb=new SourceLinkBfld(data,i);
		paramMap.put(i, slb);
	}
	
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	class SourceLinkBfld{
		
		public int    SOURCE_LINK_BFLD;
		public boolean CONSTANT_TO_BE_APPLIED=false;
		public boolean PULSE_ENGR_FLAG=false;
		public boolean CONSTANTS_FLAG=false;
		public boolean DATA_CTRL_FLAG=false;
		public boolean DEMAND_CTRL_FLAG=false;
		public boolean UOM_ENTRY_FLAG=false;
		public int Index;//与table12的index关联，一一对应
		
		public SourceLinkBfld(String data ,int i){
			this.SOURCE_LINK_BFLD=Integer.parseInt(data, 16);
			this.Index=i;
			decode();
		}
		public void decode() {
			CONSTANT_TO_BE_APPLIED=((SOURCE_LINK_BFLD&32)>>>5)==0?false:true;
			PULSE_ENGR_FLAG=((SOURCE_LINK_BFLD&16)>>>4)==0?false:true;
			CONSTANTS_FLAG=((SOURCE_LINK_BFLD&8)>>>3)==0?false:true;
			DATA_CTRL_FLAG=((SOURCE_LINK_BFLD&4)>>>2)==0?false:true;
			DEMAND_CTRL_FLAG=((SOURCE_LINK_BFLD&2)>>>1)==0?false:true;
			UOM_ENTRY_FLAG=(SOURCE_LINK_BFLD&1)==0?false:true;
		}
	}
    /**
     * 添加参数
     * @param index 索引
     * @param UOM_ENTRY_BFLD   参数
     */
    public void addParamToMap(int index, SourceLinkBfld param) {
    	paramMap.put(index, param);            
    }     
    
    public SourceLinkBfld removeParamFromMap(int index){
    	return paramMap.remove(index);
    }

    /**
     * 获得参数
     * @param index 索引
     * @return
     */
    public SourceLinkBfld getParamFromMap(int index) {   
    	return paramMap.get(index); 
    }
	
    
    
}
