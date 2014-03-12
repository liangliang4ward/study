package cn.hexing.fk.common.spi;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.message.IMessage;

public interface IEvent {
	EventType getType();
	
	/**
	 * 返回消息产生的源，例如AsyncSocketClient对象。
	 * @return
	 */
	Object getSource();
	
	void setSource(Object src);
	IMessage getMessage();
}
