package com.hx.ansi.element;

import com.hx.ansi.parse.ParserIP;

/** 
 * @Description  A6--CallingAPTitleElement
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-3-20 ÏÂÎç06:18:50
 * @version 1.0 
 */

public class CallingAPTitleElement implements Element{
	
	private String peerAddr;
	private String callingAPTitleElement;

	@Override
	public void encode() {
		String peerAddress=ParserIP.constructor(peerAddr, 12);//12==ip+port 8=ip
		int len1=peerAddress.length();
		String slen1=parseInt2HexString(len1/2);
		String slen2=parseInt2HexString((len1+2+slen1.length())/2);
		callingAPTitleElement="A6"+slen2+"06"+slen1+peerAddress;
	}
	@Override
	public void decode() {
		String peerdata=this.callingAPTitleElement.substring(8, callingAPTitleElement.length());//»ñÈ¡IP+port
		this.peerAddr=ParserIP.parseValue(peerdata, 12);
		
	}
	
	public String getPeerAddr() {
		return peerAddr;
	}

	public void setPeerAddr(String peerAddr) {
		this.peerAddr = peerAddr;
	}

	public String getCallingAPTitleElement() {
		return callingAPTitleElement;
	}

	public void setCallingAPTitleElement(String callingAPTitleElement) {
		this.callingAPTitleElement = callingAPTitleElement;
	}

	public String  parseInt2HexString(int i){
		String ss=Integer.toHexString(i);
		if(1==(ss.length()%2)){
			ss=0+ss;
		 }
		return ss;
	}
	
	
	
}
