/**
 * 系统级定时器事件。单次触发模式。
 */
package cn.hexing.fk.common.events.event;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.common.events.GlobalEventHandler;
import cn.hexing.fk.common.spi.IEvent;

import cn.hexing.fk.message.IMessage;

/**
 *
 */
public class SystemTimerEvent implements IEvent {
	//静态属性
	private static final Logger log = Logger.getLogger(SystemTimerEvent.class);
	private static final EventType type = EventType.SYS_TIMER;
	//延迟时间最小的事件排在最前面。
	private static final ArrayList<SystemTimerEvent> events = new ArrayList<SystemTimerEvent>(128);
	static{
		final SysTimerThread timerThread = new SysTimerThread();
		timerThread.start();
	}

	//定时器事件属性。
	protected String name = "";
	protected IMessage message = null;
	protected Object source = null;
	protected int delay = 1;		//延迟delay秒。
	private long beginTime = System.currentTimeMillis();

	public SystemTimerEvent(String name, Object source,IMessage msg, int delay ){
		this.name = name;
		this.source = source;
		this.message = msg;
		this.delay = delay*1000 ;
	}
	
	public IMessage getMessage() {
		return message;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void schedule(SystemTimerEvent event){
		if( null == event )
			return;
		long now = System.currentTimeMillis();
		event.beginTime = now;
		synchronized(events){
			//根据delay来按顺序排序事件。
			for(int i=0; i<events.size(); i++ ){
				SystemTimerEvent e = events.get(i);
				long remain = e.beginTime + e.delay - now;
				if( event.delay < remain ){
					events.add(i, event);
					events.notifyAll();
//					log.debug("timer event:"+event.name+",scheduled,delay="+event.delay);
					return;
				}
			}
			events.add(event);
			events.notifyAll();
//			log.debug("timer event:"+event.name+",scheduled,delay="+event.delay);
		}
	}
	
	/**
	 * 系统定时器线程。
	 * @author hbao
	 *
	 */
	static class SysTimerThread extends Thread{
		public SysTimerThread(){
			super("SysTimerThread");
			this.setDaemon(true);
		}
		
		/**
		 * 根据events，确保定时事件能够发送到GlobalEventHandler。
		 */
		@Override
		public void run() {
			int cnt = 0;		//检测死循环。调试模式使用，生产环境中，也不影响效率。
			long checkTime = 0;
			log.info("系统定时器守护线程开始运行...");
			while(true){
				synchronized(events){
					try{
						if( 0 == checkTime )
							checkTime = System.currentTimeMillis();
						dealList();
						cnt++;
					}
					catch(InterruptedException e){
					}
					catch(Exception e){
						log.warn(e.getLocalizedMessage(),e);
					}
					if( cnt> 1024 ){
						cnt = 0;
						long now = System.currentTimeMillis();
						if( now-checkTime<200 ){
							log.error("系统定时器估计进入死循环。");
						}
						checkTime = now;
					}
				}
			}
		}
		
		private void dealList() throws InterruptedException{
			while( events.size() == 0 )
				events.wait(1000);
			long now = System.currentTimeMillis();
			SystemTimerEvent ev = events.get(0);
			//取第一个事件，如果当前时间 － 事件开始时间，
			long dif = now-ev.beginTime;
			if( dif >= ev.delay ){
				events.remove(0);
				GlobalEventHandler.postEvent(ev);
				return;
			}
			if( dif<0 )
				return;
			if( dif< 100 )
				dif = 100;
			events.wait(dif);
		}
	}
}
