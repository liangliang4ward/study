package com.hx.ansi.element;

import com.hx.ansi.ansiElements.AnsiContext;


/** 
 * @Description  BE--UserInformationElement
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-20 下午06:19:47
 * @version 1.0 
 */

public class UserInformationElement implements Element{
	private String user_information_external;
	private String user_information_octetstring;
	public  static enum EPSEM_CONTROL{ NO_SECURITY,SECURITY_MODE_1,SECURITY_MODE_2,UNKNOW};
	/*
	public static  int NO_SECURITY_READ=0x80;
	public static  int NO_SECURITY_WRITE=0x92;
	public static  int SECURITY_MODE_1_READ=0x84;
	public static  int SECURITY_MODE_1_WRITE=0x96;
	public static  int SECURITY_MODE_2_READ=0x88;
	public static  int SECURITY_MODE_2_WRITE=0x9A;
	*/
	private String userInformationElement;
	private String epsem_control;
	private int dataLength;
	private String result="";
	private String userData;
	private String serverTag="";
	private String meterAddr;
	
	@Override
	public void encode() {
		
	}
	@Override
	public void decode() {
		
		
	}
	/**
	 *  encode userInf
	 * @param context
	 * @param serviceTag
	 */
	public void encode(String data,AnsiContext context,String serviceTag){
		userInformationElement="";
		String sUserInformation="";
		String sdata=encodeData(data, context, serviceTag);
		sUserInformation="81"+parseInt2HexString(sdata.length()/2)+sdata;
		sUserInformation="28"+parseInt2HexString(sUserInformation.length()/2)+sUserInformation;
		userInformationElement="BE"+parseInt2HexString(sUserInformation.length()/2)+sUserInformation;
	}
	
	/**
	 * decode userInf
	 * @param context
	 * @param data
	 */
	public String  decode(AnsiContext context,String data){
		String 	securityMode=data.substring(12, 14);
		data.substring(14);
		int iSecurityMode=Integer.parseInt(securityMode, 16);
		switch(iSecurityMode){
		case 128:
			context.epsem_control=EPSEM_CONTROL.NO_SECURITY;
			break;
		case 146:
			context.epsem_control=EPSEM_CONTROL.NO_SECURITY;
			break;
		case 132:
			context.epsem_control=EPSEM_CONTROL.SECURITY_MODE_1;
			break;
		case 150:
			context.epsem_control=EPSEM_CONTROL.SECURITY_MODE_1;
			break;
		case 136:
			context.epsem_control=EPSEM_CONTROL.SECURITY_MODE_2;
			break;
		case 154:
			context.epsem_control=EPSEM_CONTROL.SECURITY_MODE_2;
			break;
		}
		return data;
	}
	/**
	 * 解析用户数据区
	 * @param data
	 */
	public void decode(String data){
		//心跳：BE LL 28 LL 81 LL 80 LL TAG 01 LL AA AA AA AA
		//回复：BE LL 28 LL 81 LL 80 LL OK DATA 
		this.epsem_control=data.substring(12,14);
		this.dataLength=Integer.parseInt(data.substring(14, 16), 16);
		String tagOrok=data.substring(16, 18);//这里有可能是tag也有可能是result
		if(tagOrok.equalsIgnoreCase("FF")||tagOrok.equalsIgnoreCase("FE")){
			this.serverTag=tagOrok;
			this.meterAddr=data.substring(22, data.length());
		}else{
			this.result=tagOrok;
			this.userData=data.substring(18, data.length());
		}
	}
	
	/**
	 * 
	 * @param data
	 * @param context
	 * @param serviceTag
	 * @return
	 */
	private String encodeData(String data,AnsiContext context,String serviceTag){
		String userData="";
		switch(context.epsem_control){
		case NO_SECURITY:
//			if(null!=serviceTag&&serviceTag.startsWith("4")){
//			userData="92"+context.table0.device_class+parseInt2HexString(data.length()/2)+data;
//			}else{
			userData="80"+parseInt2HexString(data.length()/2)+data;
//			}
			break;
		case SECURITY_MODE_1:
//			if(null!=serviceTag&&serviceTag.startsWith("4")){
//				userData="96"+context.table0.device_class+parseInt2HexString(data.length()/2)+data;
//				}else{
				userData="84"+parseInt2HexString(data.length()/2)+data;
//				}
				break;
		case SECURITY_MODE_2:
//		if(null!=serviceTag&&serviceTag.startsWith("4")){
//			userData="9A"+context.table0.device_class+parseInt2HexString(data.length()/2)+data;
//			}else{
			userData="88"+parseInt2HexString(data.length()/2)+data;
//			}
			break;
		case UNKNOW:
			
		}
		return userData;
	} 
	

	
	public String getUser_information_external() {
		return user_information_external;
	}

	public void setUser_information_external(String user_information_external) {
		this.user_information_external = user_information_external;
	}

	public String getUser_information_octetstring() {
		return user_information_octetstring;
	}

	public void setUser_information_octetstring(String user_information_octetstring) {
		this.user_information_octetstring = user_information_octetstring;
	}

	public String getUserInformationElement() {
		return userInformationElement;
	}

	public void setUserInformationElement(String userInformationElement) {
		this.userInformationElement = userInformationElement;
	}

	public String getEpsem_control() {
		return epsem_control;
	}
	public void setEpsem_control(String epsem_control) {
		this.epsem_control = epsem_control;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
	public String getServerTag() {
		return serverTag;
	}
	public void setServerTag(String serverTag) {
		this.serverTag = serverTag;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public String getMeterAddr() {
		return meterAddr;
	}
	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}
	public String  parseInt2HexString(int i){
		String ss=Integer.toHexString(i);
		if(1==(ss.length()%2)){
			ss=0+ss;
		 }
		return ss;
	}
	
}
