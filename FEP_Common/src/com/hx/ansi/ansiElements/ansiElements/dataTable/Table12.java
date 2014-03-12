package com.hx.ansi.ansiElements.ansiElements.dataTable;

import java.util.HashMap;
import java.util.Map;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  	Table 12 计量值单元表
 * 		Table12提供计量值的类型、单位、单位量纲等属性。此表作为所有寄存器的数据标识源，其数据结构为数组，
 * 		数组元素为4字节，数组元素的个数由表计计量内容的多少有关。Table12为只读信息。
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-19 下午03:28:26
 * @version 1.0 
 */

public class Table12 extends Table{
	
    /** 索引和元素值对应表 */
    public  Map<Integer,UOMENTRYBFLD> paramMap=new HashMap<Integer,UOMENTRYBFLD>();
    /** code和索引表 */
    public  Map<Integer,String> dataItemMap=new HashMap<Integer,String>();
	

	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}
	public void decode(String data,int i){
		UOMENTRYBFLD unb=new UOMENTRYBFLD(AnsiDataSwitch.ReverseStringByByte(data),i);
		paramMap.put(i, unb);
		dataItemMap.put(i,data );
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
	public class UOMENTRYBFLD{
		public int UOM_ENTRY_BFLD ;
		public boolean isHARMONIC=false;//是否为谐波分量 0-否；1—是
		public byte SEGMENTATION;//是否为相角相关的量
		public byte NET_FLOW_ACCOUNTABILITY;//计量方向
		public boolean Q4_ACCOUNTABILITY=false;//计量值是否为第4象限相关 0—否；1—是
		public boolean Q3_ACCOUNTABILITY=false;//计量值是否为第3象限相关 0—否；1—是
		public boolean Q2_ACCOUNTABILITY=false;//计量值是否为第2象限相关 0—否；1—是
		public boolean Q1_ACCOUNTABILITY=false;//计量值是否为第1象限相关 0—否；1—是
		public int MULTIPLIER;//单位的量纲
		public double multiplier;
		public byte TIME_BASE;//基于时间的计算方式
		public int ID_CODE;//计量单元ID
		public int Index=0;//与table16的index关联，一一对应
		public  Map<Integer,Double> multiplierMap=new HashMap<Integer,Double>();
		public UOMENTRYBFLD(String data,int i){
			this.UOM_ENTRY_BFLD=Integer.parseInt(data, 16);
			this.Index=i;//索引是按照数据的顺序
			multiplierMap.put(0, 1.0);
			multiplierMap.put(1, 100.0);
			multiplierMap.put(2, 1000.0);
			multiplierMap.put(3, 1000000.0);
			multiplierMap.put(4, 1000000000.0);
			multiplierMap.put(5, 0.01);
			multiplierMap.put(6, 0.001);
			multiplierMap.put(7, 0.000001);
			decodeUOM_ENTRY_BFLD();
		}
		//对每一个Item进行解码
		public void  decodeUOM_ENTRY_BFLD(){
			isHARMONIC=((UOM_ENTRY_BFLD&4194304)>>>22)==0?false:true;
			SEGMENTATION=(byte) ((UOM_ENTRY_BFLD&3670016)>>>19);
			NET_FLOW_ACCOUNTABILITY=(byte) ((UOM_ENTRY_BFLD&262144)>>>18);
			Q4_ACCOUNTABILITY=((UOM_ENTRY_BFLD&131072)>>>17)==0?false:true;
			Q3_ACCOUNTABILITY=((UOM_ENTRY_BFLD&65536)>>>16)==0?false:true;
			Q2_ACCOUNTABILITY=((UOM_ENTRY_BFLD&32768)>>>15)==0?false:true;
			Q1_ACCOUNTABILITY=((UOM_ENTRY_BFLD&16384)>>>14)==0?false:true;
			MULTIPLIER=((UOM_ENTRY_BFLD&14336)>>>11);
			multiplier=multiplierMap.get(MULTIPLIER);
			TIME_BASE=(byte) ((UOM_ENTRY_BFLD&1792)>>>8);
			ID_CODE=(int) (UOM_ENTRY_BFLD&255);
		}
	}
	
    /**
     * 添加参数
     * @param index 索引
     * @param UOM_ENTRY_BFLD   参数
     */
    public void addParamToMap(int index, UOMENTRYBFLD UOM_ENTRY_BFLD) {
    	paramMap.put(index, UOM_ENTRY_BFLD);            
    }     
    
    public UOMENTRYBFLD removeParamFromMap(int index){
    	return paramMap.remove(index);
    }

    /**
     * 获得参数
     * @param index 索引
     * @return
     */
    public UOMENTRYBFLD getParamFromMap(int index) {   
    	return paramMap.get(index); 
    }

    
    public static void main(String[] args) {
    			//004002000080010000C0030000C0070001C0000001000300014020000180200001002100010022000002280000023000000238000002000001020000020200000C2A28000C2A30000C2A3800082A2800082A3000082A380019320000193228001932300019323800212A20001602280016023000160238001502080015021800004402000084010000C4030000C4070001C400000104030001442000018420000104210001042200
//		String s="004002000080010000C0030000C0070001C0000001000300014020000180200001002100010022000002280000023000000238000002000001020000020200000C2A28000C2A30000C2A3800082A2800082A3000082A380019320000193228001932300019323800212A20001602280016023000160238001502080015021800004402000084010000C4030000C4070001C400000104030001442000018420000104210001042200";
//		String ss="";
//		for(int i=0;i<s.length()/8;i++){
//			ss=s.substring(8*i, 8*i+8);
//			System.out.println(ss);
//		}
//		
		Table12 table12=new Table12();
		//table12中每一个数据元素的长度为4个字节。len计算表计计量的元素个数
		for(int i=0;i<1;i++){
			table12.decode("00024000",i);
		}
		
		
    }
}
