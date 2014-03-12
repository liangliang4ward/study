/**
 * Message对象在读取缓冲区数据，进行消息解包时错误事件。
 * 用于打印哪些消息内容不能解析。source为client对象，toString函数提供IP地址信息。
 */
package cn.hexing.fk.common.events.event;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.message.IMessage;

/**
 */
public class MessageParseErrorEvent implements IEvent {
	private final EventType type = EventType.MSG_PARSE_ERROR;
	private IMessage message;
	private Object source;		//消息对象对应的来源ClientChannel
	private String info;

	public MessageParseErrorEvent(IMessage msg){
		message = msg;
		source = msg.getSource();
	}

	public MessageParseErrorEvent(IMessage msg,String info){
		message = msg;
		source = msg.getSource();
		this.info = info;
	}
	
	public IMessage getMessage() {
		return message;
	}

	public Object getSource() {
		return source;
	}

	public EventType getType() {
		return type;
	}

	public void setSource(Object src) {
		source = src;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer(512);
		sb.append("MessageParseErrorEvent,source=").append(source);
		sb.append(",packet=").append(message.getRawPacketString());
		if( null != info )
			sb.append(",info=").append(info);
		return sb.toString();
	}
}
