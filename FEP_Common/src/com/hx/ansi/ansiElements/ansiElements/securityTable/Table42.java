package com.hx.ansi.ansiElements.ansiElements.securityTable;

import java.util.HashMap;
import java.util.Map;

import com.hx.ansi.ansiElements.ansiElements.Table;

/** 
 * @Description  密码及密码权限表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:06:28
 * @version 1.0 
 */

public class Table42 extends Table{
	public Map<Integer,securityRCD> securityMap=new HashMap<Integer,securityRCD>();
	
	@Override
	public void decode() {
	}
	
	public void decode(String data,int i,int len){
		securityRCD rcd=new securityRCD(data,len);
		securityMap.put(i, rcd);
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
	class securityRCD{
		public String securityRCD;
		public String passWord;
		public int len;//密码的长度,字节
		public int accessPermission;//该密码对应的权限
		public boolean group7;//Group7权限是否受该密码保护，是则用该密码登录后group7的权限才开通给客户端
		public boolean group6;//Group6权限是否受该密码保护
		public boolean group5;//Group5权限是否受该密码保护
		public boolean group4;//Group4权限是否受该密码保护
		public boolean group3;//Group3权限是否受该密码保护
		public boolean group2;//Group2权限是否受该密码保护
		public boolean group1;//Group1权限是否受该密码保护
		public boolean group0;//Group0权限是否受该密码保护
		public securityRCD(String data,int len){
			this.securityRCD=data;
			this.len=len;
			decode();
		}
		public void decode() {
			passWord=securityRCD.substring(0, len*2);
			accessPermission=Integer.parseInt(securityRCD.substring(len*2, len*2+2), 16);
			group7=((accessPermission&128)>>7)==0?false:true;
			group6=((accessPermission&64)>>6)==0?false:true;
			group5=((accessPermission&32)>>5)==0?false:true;
			group4=((accessPermission&16)>>4)==0?false:true;
			group3=((accessPermission&8)>>3)==0?false:true;
			group2=((accessPermission&4)>>2)==0?false:true;
			group1=((accessPermission&2)>>1)==0?false:true;
			group0=(accessPermission&1)==0?false:true;
		}
	}
    /**
     * 添加参数
     * @param index 索引
     * @param UOM_ENTRY_BFLD   参数
     */
    public void addParamToMap(int index, securityRCD RCD) {
    	securityMap.put(index, RCD);            
    }     
    
    public securityRCD removeParamFromMap(int index){
    	return securityMap.remove(index);
    }

    /**
     * 获得参数
     * @param index 索引
     * @return
     */
    public securityRCD getParamFromMap(int index) {   
    	return securityMap.get(index); 
    }


}
