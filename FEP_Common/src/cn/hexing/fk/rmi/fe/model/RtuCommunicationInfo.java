package cn.hexing.fk.rmi.fe.model;

import java.io.Serializable;

/**
 * 
 * @author gaoll
 *
 * @time 2013-9-6 œ¬ŒÁ04:47:09
 *
 * @info ÷’∂À–≈œ¢
 */
public class RtuCommunicationInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1982797080131481732L;

	private String rtuId;
	
	private RtuCurrentMessage msg;
	
	private RtuCurrentWorkState state;
	
	private long currentTime;
	
	public RtuCurrentMessage getMsg() {
		return msg;
	}

	public void setMsg(RtuCurrentMessage msg) {
		this.msg = msg;
	}

	public String getRtuId() {
		return rtuId;
	}

	public void setRtuId(String rtuId) {
		this.rtuId = rtuId;
	}

	public RtuCurrentWorkState getState() {
		return state;
	}

	public void setState(RtuCurrentWorkState state) {
		this.state = state;
	}
	@Override
	public String toString(){
		String str =this.rtuId;
		if(state!=null){
			str+=" ";
			str+=state.toString();
		}
		str+=" ";
		if(msg !=null){
			str+=msg.toString();
		}
		return str;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	
}
