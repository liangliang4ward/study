package com.hx.ansi.element;


/** 
 * @Description A8-- CallingAPInvocationIdElement
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-20 下午06:19:08
 * @version 1.0 
 */

public class CallingAPInvocationIdElement implements Element{
	//A8 固定为帧序号，主站下行请求带A8 03 02 01 xx 模块回复与主站对应，模块请求带A8 主站回复和模块对应 
	private  int callingAPInvocationId;
	private String callingAPInvocationIdElement;



	@Override
	public void encode() {
		String scallingAPInvocationId=parseInt2HexString(callingAPInvocationId);
		callingAPInvocationIdElement="A8030201"+"00".substring(scallingAPInvocationId.length())+scallingAPInvocationId;
	}

	@Override
	public void decode() {
		this.callingAPInvocationId=Integer.parseInt(this.callingAPInvocationIdElement.substring(0, 2), 16);
	}
	
	
	
	public int getCallingAPInvocationId() {
		return callingAPInvocationId;
	}

	public void setCallingAPInvocationId(int callingAPInvocationId) {
		this.callingAPInvocationId = callingAPInvocationId;
	}

	public String getCallingAPInvocationIdElement() {
		return callingAPInvocationIdElement;
	}

	public void setCallingAPInvocationIdElement(String callingAPInvocationIdElement) {
		this.callingAPInvocationIdElement = callingAPInvocationIdElement;
	}

	public String  parseInt2HexString(int i){
		String ss=Integer.toHexString(i);
		if(1==(ss.length()%2)){
			ss=0+ss;
		 }
		return ss;
	}
	
	
}
