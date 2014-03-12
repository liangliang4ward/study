package cn.hexing.fk.rmi.fe.model;

import java.io.Serializable;

import cn.hexing.fk.message.MessageConst;

public class RtuCurrentMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2371312875835107357L;

	
	private long time;
	
	private String content;
	
	private String logicAddress;
	
	private int dir; //up -down
	
	private String peerAddr;

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public String getLogicAddress() {
		return logicAddress;
	}

	public void setLogicAddress(String logicAddress) {
		this.logicAddress = logicAddress;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPeerAddr() {
		return peerAddr;
	}

	public void setPeerAddr(String peerAddr) {
		this.peerAddr = peerAddr;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return (dir==MessageConst.DIR_DOWN?"DOWN":"UP"+"content:")+this.content+",peerAddr:"+this.peerAddr;
		
	}

	

	
}
