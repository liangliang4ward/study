package com.hx.ansi.element;

import com.hx.ansi.parse.ParserIP;


/** 
 * @Description A2-- CalledAPTitle
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-20 下午06:17:03
 * @version 1.0 
 */

public class CalledAPTitleElement implements Element{
	
	private String peerAddr;
	private String calledAPTitleElement;

	@Override
	public void encode() {
		String peerAddress=peerAddr.substring(peerAddr.length()-2, peerAddr.length());
		if(peerAddress.equals(":T")){
			peerAddress=peerAddr.substring(0, peerAddr.length()-2);
		}else{
			System.out.println("未知的IP地址");
		}
		peerAddress=ParserIP.constructor(peerAddress, 12);//12==ip+port 8=ip
		int len1=peerAddress.length();
		String slen1=parseInt2HexString(len1/2);
		String slen2=parseInt2HexString((len1+2+slen1.length())/2);
		calledAPTitleElement="A2"+slen2+"06"+slen1+peerAddress;
	}
	@Override
	public void decode() {
		String peerdata=this.calledAPTitleElement.substring(8, calledAPTitleElement.length());//获取IP+port
		this.peerAddr=ParserIP.parseValue(peerdata, 12);
	}
	
	public String getPeerAddr() {
		return peerAddr;
	}

	public void setPeerAddr(String peerAddr) {
		this.peerAddr = peerAddr;
	}

	public String getCalledAPTitleElement() {
		return calledAPTitleElement;
	}

	public void setCalledAPTitleElement(String calledAPTitleElement) {
		this.calledAPTitleElement = calledAPTitleElement;
	}

	public String  parseInt2HexString(int i){
		String ss=Integer.toHexString(i);
		if(1==(ss.length()%2)){
			ss=0+ss;
		 }
		return ss;
	}
	
	
	
}
