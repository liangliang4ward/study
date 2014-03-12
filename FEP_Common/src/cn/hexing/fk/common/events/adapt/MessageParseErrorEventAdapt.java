package cn.hexing.fk.common.events.adapt;

import org.apache.log4j.Logger;

import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.common.spi.IEventHandler;

public class MessageParseErrorEventAdapt implements IEventHandler {
	private static final Logger log = Logger.getLogger(MessageParseErrorEventAdapt.class);
//	private IEvent event;
	
	public void handleEvent(IEvent event) {
		log.warn(event);
	}

}
