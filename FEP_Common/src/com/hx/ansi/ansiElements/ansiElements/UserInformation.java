package com.hx.ansi.ansiElements.ansiElements;
/** 
 * @Description  用户信息类
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-15 下午04:19:00
 * @version 1.0 
 */
/**
 * 用户信息类
 */
public class UserInformation {
	/** 用户ID*/
	private String userID;
	/**用户信息 */
	private String userName;
	/**密码 */
	private String passWord;
	/**终端地址：IP+port*/
	private String peerAddr;
	/** 本地访问地址*/
	private String localAddr;
	/** 加密类型*/
	private int securityMode;
	
	public String getLocalAddr() {
		return localAddr;
	}
	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}
	public String getPeerAddr() {
		return peerAddr;
	}
	public void setPeerAddr(String peerAddr) {
		this.peerAddr = peerAddr;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public int getSecurityMode() {
		return securityMode;
	}
	public void setSecurityMode(int securityMode) {
		this.securityMode = securityMode;
	}
	
	
	
}
