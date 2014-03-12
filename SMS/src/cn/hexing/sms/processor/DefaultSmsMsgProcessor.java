package cn.hexing.sms.processor;

import org.smslib.InboundMessage;

public class DefaultSmsMsgProcessor implements SmsMsgProcessor{

	@Override
	public boolean process(InboundMessage msg) {
		System.out.println(msg.getText());
		return false;
	}

}
