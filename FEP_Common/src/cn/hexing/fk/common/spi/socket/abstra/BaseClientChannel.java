/**
 * ClientChannel接口基础类，用于同步socket client对象的封装，保障一致处理。
 */
package cn.hexing.fk.common.spi.socket.abstra;

import cn.hexing.fk.common.spi.socket.IChannel;
import cn.hexing.fk.common.spi.socket.ISocketServer;
import cn.hexing.fk.message.IMessage;

/**
 *
 */
public abstract class BaseClientChannel implements IChannel {
	private int requestNum = -1;

	public abstract void close();

	public long getLastIoTime() {
		return 0;
	}

	public long getLastReadTime() {
		return 0;
	}

	public int getLastingWrite() {
		return 0;
	}

	public abstract String getPeerAddr();

	public abstract String getPeerIp();

	public abstract int getPeerPort();

	public ISocketServer getServer() {
		return null;
	}
	public void setIoThread(Object threadObj) { }

	public void setLastIoTime() { }

	public void setLastReadTime() { }

	public abstract boolean send(IMessage msg);
	
	public abstract boolean sendMessage(IMessage msg);

	public int sendQueueSize() {
		return 0;
	}
	
	public void setMaxSendQueueSize(int maxSendQueueSize){
		
	}

	public int getRequestNum() {
		return requestNum;
	}

	public void setRequestNum(int requestNum) {
		this.requestNum = requestNum;
	}
	
	public abstract boolean isActive();
}
