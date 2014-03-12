package cn.hexing.fk.message.bengal;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.IMessageCreator;

public class BengalMessageCreator implements IMessageCreator {

	public IMessage create() {
		return new BengalMessage();
	}

	public IMessage createHeartBeat(int reqNum) {
		BengalMessage msg = new BengalMessage();
		msg.setSeq(reqNum);
		msg.setFuncCode(2);
		return msg;
	}

}
