package cn.hexing.fk.message.gw;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.IMessageCreator;
import cn.hexing.fk.message.MessageConst;

public class MessageGwCreator implements IMessageCreator {

	public IMessage create() {
		return new MessageGw();
	}

	public IMessage createHeartBeat(int reqNum) {
		MessageGw heart = new MessageGw();
		heart.head.app_func = MessageConst.GW_FUNC_HEART;
		heart.head.c_func = MessageConst.GW_FN_HEART;
		heart.head.rtua = reqNum;
		return heart;
	}

}
