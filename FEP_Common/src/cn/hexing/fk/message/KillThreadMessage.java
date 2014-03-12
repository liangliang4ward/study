package cn.hexing.fk.message;

import java.nio.ByteBuffer;

import cn.hexing.fk.common.spi.socket.IChannel;
import cn.hexing.fk.exception.MessageParseException;

public class KillThreadMessage implements IMessage {
	private static final MessageType type = MessageType.MSG_KILLTHREAD;
	
	public long getIoTime() {
		return 0;
	}

	public MessageType getMessageType() {
		return type;
	}

	public String getPeerAddr() {
		return null;
	}

	public int getPriority() {
		return IMessage.PRIORITY_MAX;
	}

	public byte[] getRawPacket() {
		return null;
	}

	public String getRawPacketString() {
		return null;
	}

	public IChannel getSource() {
		return null;
	}

	public String getTxfs() {
		return null;
	}

	public boolean read(ByteBuffer readBuffer) throws MessageParseException {
		return false;
	}

	public void setIoTime(long time) {

	}

	public void setPeerAddr(String peer) {
	}

	public void setPriority(int priority) {
	}

	public void setSource(IChannel src) {
	}

	public void setTxfs(String fs) {
	}

	public boolean write(ByteBuffer writeBuffer) {
		return false;
	}

	public Long getCmdId() {
		return null;
	}

	public String getStatus() {
		return null;
	}

	public String getServerAddress() {
		return null;
	}

	public void setServerAddress(String serverAddress) {
	}

	public boolean isHeartbeat() {
		return false;
	}

	public int getRtua() {
		return 0;
	}
	
	public String getLogicalAddress(){
		return null;
	}
	
	public void setLogicalAddress(String logicAddr){
		
	}

	public void setStatus(String status) {
	}
	
	public int length(){
		return 0;
	}

	public boolean isTask() {
		return false;
	}

	public void setTask(boolean isTask) {
	}
}
