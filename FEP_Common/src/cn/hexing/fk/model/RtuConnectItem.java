package cn.hexing.fk.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author gaoll
 *
 * @time 2012-12-25 上午9:28:11
 *
 * @info 终端连接对象
 */
public class RtuConnectItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logicAddress;
	
	/**
	 * 状态0表示连接，1表示断开 
	 */
	private int status;
	
	/**
	 * 当前IP
	 */
	private String peerAddress;
	
	/**
	 * 发生时间
	 */
	private Date occurTime;

	public final String getLogicAddress() {
		return logicAddress;
	}

	public final void setLogicAddress(String logicAddress) {
		this.logicAddress = logicAddress;
	}

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public final String getPeerAddress() {
		return peerAddress;
	}

	public final void setPeerAddress(String peerAddress) {
		this.peerAddress = peerAddress;
	}

	public final Date getOccurTime() {
		return occurTime;
	}

	public final void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}
}
