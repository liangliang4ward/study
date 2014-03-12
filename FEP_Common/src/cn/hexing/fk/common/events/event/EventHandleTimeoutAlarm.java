/**
 * 事件处理超时告警事件
 */
package cn.hexing.fk.common.events.event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.message.IMessage;

/**
 *
 */
public class EventHandleTimeoutAlarm implements IEvent {
	private final EventType type = EventType.SYS_EVENT_PROCESS_TIMEOUT;
	private Object source;
	private IEvent event;
	private String threadName;
	private long beginTime,endTime;
	private List<StackTraceElement> stackTraces = new ArrayList<StackTraceElement>();
	private boolean isFinished =false;
	
	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public EventHandleTimeoutAlarm(IEvent ev){
		event = ev;
		threadName = Thread.currentThread().getName();
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

	public void setType(EventType type) {
	}

	/**
	 * 获取处理超时的事件
	 * @return
	 */
	public IEvent getTimeoutEvent(){
		return event;
	}
	
	public IMessage getMessage(){
		return null;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer(1024);
		sb.append("Event Process TimeOut.EventType:").append(event.getType());//sb.append("事件处理超时。类型：").append(event.getType());
		sb.append(",isFinished:"+isFinished);
		sb.append(",thread=").append(threadName);
		sb.append(",begin=");
		Date date = new Date(this.getBeginTime());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
		sb.append(format.format(date)).append(",end=");
		sb.append(format.format(new Date(this.getEndTime()))).append(".EventContent：");//sb.append(format.format(new Date(this.getEndTime()))).append("。事件内容：");
		String eventStr="";
		if (event!=null){
			if(event.toString().length()>=1024)
				eventStr=event.toString().substring(0,1024)+"...";
			else
				eventStr=event.toString();
			sb.append(eventStr);
		}		
		sb.append(".StackTraceElement:");
		for(StackTraceElement st: stackTraces ){
			sb.append("\r\n\t").append(st.toString());
		}
		return sb.toString();
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	public void setStackTraceElement(StackTraceElement [] trace){
		if( null == trace || trace.length == 0 )
			return;
		for(StackTraceElement st: trace){
			stackTraces.add(st);
		}
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
}
