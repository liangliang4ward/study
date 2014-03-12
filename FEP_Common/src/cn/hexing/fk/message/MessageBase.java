package cn.hexing.fk.message;

import cn.hexing.fk.common.spi.socket.IChannel;

public abstract class MessageBase implements IMessage {
	protected IChannel source = null;
	protected String peerAddr = null;
	protected String serverAddress = null;
	protected String status = "";
	protected long ioTime = 0;
	private static final MessageType type = MessageType.MSG_INVAL;
	
	public Long getCmdId() {
		return new Long(0);
	}

	public long getIoTime() {
		return ioTime;
	}

	public MessageType getMessageType() {
		return type;
	}

	public String getPeerAddr() {
		return peerAddr;
	}

	public int getPriority() {
		return 0;
	}

	public int getRtua() {
		return 0;
	}
	
	public String getLogicalAddress(){
		return null;
	}
	
	public void setLogicalAddress(String logicAddr){
		
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public IChannel getSource() {
		return source;
	}

	public String getStatus() {
		return status;
	}

	public String getTxfs() {
		return "";
	}

	public boolean isHeartbeat() {
		return false;
	}

	public boolean isTask() {
		return false;
	}

	public void setIoTime(long time) {
		ioTime = time;
	}

	public void setPeerAddr(String peer) {
		peerAddr = peer;
	}

	public void setPriority(int priority) {
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setSource(IChannel src) {
		source = src;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTask(boolean isTask) {
	}

	public void setTxfs(String fs) {
	}
}
