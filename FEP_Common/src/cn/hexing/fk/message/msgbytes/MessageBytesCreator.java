package cn.hexing.fk.message.msgbytes;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.IMessageCreator;

public class MessageBytesCreator implements IMessageCreator {

	public IMessage create() {
		return new MessageBytes();
	}

	public IMessage createHeartBeat(int reqNum) {
		return null;
	}

}
