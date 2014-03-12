/**
 * 事件处理器超时 事件适配器
 */
package cn.hexing.fk.common.events.adapt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.hexing.fk.common.events.event.EventHandleTimeoutAlarm;
import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.common.spi.IEventHandler;

/**
 *
 */
public class EventHandleTimeoutAdapt implements IEventHandler {
	private static final Logger log = Logger.getLogger(EventHandleTimeoutAdapt.class);
	private EventHandleTimeoutAlarm event;

	public void handleEvent(IEvent event) {
		this.event = (EventHandleTimeoutAlarm)event;
		process();
	}

	protected void process(){
		if(log.isInfoEnabled()){
			StringBuffer sb = new StringBuffer(1024);
			sb.append("Event process TimeOut.EventType：").append(event.getTimeoutEvent().getType());//sb.append("事件处理超时。事件类型：").append(event.getTimeoutEvent().getType());
			sb.append(",begin=");
			Date date = new Date(event.getBeginTime());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
			sb.append(format.format(date)).append(",end=");
			sb.append(format.format(new Date(event.getEndTime()))).append(".Event Content：");//sb.append(format.format(new Date(event.getEndTime()))).append("。事件内容：");
			sb.append(event.getTimeoutEvent().toString());
			log.info(sb.toString());
		}
	}
}
