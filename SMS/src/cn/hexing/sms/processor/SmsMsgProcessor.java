package cn.hexing.sms.processor;

import org.smslib.InboundMessage;

public interface SmsMsgProcessor {
	public boolean process(InboundMessage msg);
}
