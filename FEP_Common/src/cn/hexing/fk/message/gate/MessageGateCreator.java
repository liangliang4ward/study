package cn.hexing.fk.message.gate;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.IMessageCreator;

public class MessageGateCreator implements IMessageCreator {

	/**
	 * 前置机内部（BP-FE-GATE）之间心跳（同时也用于主动请求报文）
	 */
	public IMessage createHeartBeat(int reqNum) {
		return MessageGate.createHRequest(reqNum);
	}

	public IMessage create() {
		return new MessageGate();
	}

}
