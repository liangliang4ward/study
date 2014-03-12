/**
 * 全局事件处理器。负责事件的默认处理。默认处理一般就是写日志。
 * 如果某个事件需要特别处理，则需要编写相应的钩子。
 */
package cn.hexing.fk.common.events;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.common.spi.IEventHandler;
import cn.hexing.fk.common.spi.IEventHook;
import cn.hexing.fk.common.spi.IEventPump;
import cn.hexing.fk.tracelog.TraceLog;
import cn.hexing.fk.utils.State;

/**
 */
@SuppressWarnings("unchecked")
public final class GlobalEventHandler implements IEventPump {
	private final EventQueue queue = new EventQueue();
	/**
	 * 全局事件处理钩子。这些钩子的实现类，通过配置方式确定钩住哪个事件源，以及哪些类型事件。
	 */
	private static final Object hookLock = new Object();
	private static ArrayList<IEventHook> [] hooks = (ArrayList<IEventHook>[])Array.newInstance(ArrayList.class,512);

	/**
	 * 通过编程方式添加事件以及处理函数。这种模式只适合于快速处理模式的事件，如系统定时器。
	 */
	private static ArrayList<IEventHandler> [] handlers;
	
	private volatile State state = State.STOPPED;
	private static Logger log = Logger.getLogger(GlobalEventHandler.class);
	private static TraceLog tracer = TraceLog.getTracer();

	private static GlobalEventHandler gHandler = null;
	private EventPumpThread pump = null; 

	static {
		gHandler = getInstance();
	}

	private GlobalEventHandler(){
		pump = new EventPumpThread();
		pump.start();
	}
	
	public static final GlobalEventHandler getInstance(){
		if( null == gHandler )
			gHandler = new GlobalEventHandler();
		return gHandler;
	}
	
	public void destroy(){
		state = State.STOPPING;
		pump.interrupt();
	}
	/**
	 * 全局事件处理器允许注册钩子，把特定EventType事件交给钩子处理
	 * @param hook
	 */
	public static final void registerHook(IEventHook hook,EventType type){
		synchronized(hookLock){
			if( null == hooks ){
				hooks = new ArrayList [128];
				Arrays.fill(hooks,null);
			}
			if( hooks.length < EventType.getMaxIndex() ){
				ArrayList<IEventHook> [] temp = new ArrayList [EventType.getMaxIndex()];
				Arrays.fill(temp, null);
				System.arraycopy(hooks, 0, temp, 0, hooks.length);
				hooks = temp;
			}
			if( null == hooks[type.toInt()]){
				hooks[type.toInt()] = new ArrayList<IEventHook>();
			}
			hooks[type.toInt()].add(hook);
		}
	}

	/**
	 * 编程方式，添加某种事件的处理。
	 * @param handler
	 * @param type
	 */
	public static final void registerHandler(IEventHandler handler,EventType type){
		synchronized(hookLock){
			if( null == handlers ){
				handlers = new ArrayList [128];
				Arrays.fill(handlers,null);
			}
			if( handlers.length < EventType.getMaxIndex() ){
				ArrayList<IEventHandler> [] temp = new ArrayList [EventType.getMaxIndex()];
				Arrays.fill(temp, null);
				System.arraycopy(handlers, 0, temp, 0, handlers.length);
				handlers = temp;
			}
			if( null == handlers[type.toInt()]){
				handlers[type.toInt()] = new ArrayList<IEventHandler>();
			}
			handlers[type.toInt()].add(handler);
		}
	}
	
	/**
	 * 全局事件处理器允许删除注册的钩子
	 * @param hook
	 */
	public static final void deregisterHook(IEventHook hook,EventType type){
		synchronized(hookLock){
			try{
				hooks[type.toInt()].remove(hook);
			}catch(Exception e){
				log.error("deregisterHook: "+e.getLocalizedMessage(),e);
			}
		}
	}

	public static final void deregisterHandler(IEventHandler handler,EventType type){
		synchronized(hookLock){
			try{
				handlers[type.toInt()].remove(handler);
			}catch(Exception e){
				log.error("deregisterHandler: "+e.getLocalizedMessage(),e);
			}
		}
	}
	
	public final void handleEvent(IEvent e) {
		if( EventType.SYS_MEMORY_PROFILE == e.getType() ){
			log.info("profile事件: "+e);
		}
		else
			log.debug("Global Event Handler:"+e );//log.debug("全局事件处理器:"+e );
	}

	public static void postEvent(IEvent e){
		gHandler.post(e);
	}
	
	public final void post(IEvent e) {
		boolean hooked = false;
		ArrayList<IEventHook> list = hooks[e.getType().toInt()];
		if( null != list && list.size()>0 ){
			try{
				for(IEventHook hook: list){
					hook.postEvent(e);
				}
				hooked = true;
			}catch(Exception exp){
				log.error("hook.postEvent:"+exp.getLocalizedMessage(),exp);
			}
		}

		//检测事件是否需要被Handler处理
		if( null != handlers && handlers.length> e.getType().toInt() ){
			ArrayList<IEventHandler> arHandlers = handlers[e.getType().toInt()];
			if( null != arHandlers && arHandlers.size()>0 ){
				hooked = true;
				for( IEventHandler handler: arHandlers ){
					try{
						handler.handleEvent(e);
					}catch(Exception exp){
						log.error("handler.handleEvent: "+exp.getLocalizedMessage(),exp);
					}
				}
			}
		}
		
		//没有注册的钩子，则全局事件处理器自己处理。
		if( ! hooked ){
			try{
				queue.offer(e);
			}catch(Exception exp){
				String info = "全局事件队列插入失败。event="+e.toString();
				tracer.trace(info,exp);
				log.error(info,exp);
			}
		}
	}
	
	private class EventPumpThread extends Thread{
		public EventPumpThread(){
			super("GlobalEventPumpThread");
			this.setDaemon(true);
		}
		public void run(){
			state = cn.hexing.fk.utils.State.RUNNING;
			log.info("Global event handler thread running");
			long pre = System.currentTimeMillis();
			int cnt = 0;
			while( state != cn.hexing.fk.utils.State.STOPPING ){
				try{
					IEvent e = queue.take();
					if( null == e ){
						//死循环检测
						cnt++;
						if( cnt >= 20 ){
							long now = System.currentTimeMillis();
							if( Math.abs(now-pre)<1000 ){
								log.warn("检测到死循环。");
							}
							pre = System.currentTimeMillis();
							cnt = 0;
						}
						continue;
					}
					handleEvent(e);
				}
				catch(Exception e){
					log.warn("Global event handler event pump catch exception:"+e.getLocalizedMessage(),e);
				}
			}
			state = cn.hexing.fk.utils.State.STOPPED;
		}
	}
}
