package cn.hexing.fk.common.events.event;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.message.IMessage;

public class KillThreadEvent implements IEvent {
	private final EventType type = EventType.SYS_KILLTHREAD;
	
	public Object getSource() {
		return null;
	}

	public EventType getType() {
		return type;
	}

	public void setSource(Object src) {
	}

	public void setType(EventType type) {
	}

	public IMessage getMessage(){
		return null;
	}
}
