/**
 * 简单事件处理钩子类。用于事件处理的基类。
 * 基本事件钩子，通过source属性过滤事件。
 * 通过threadpool 来并发执行事件处理（IEventHandler).
 */
package cn.hexing.fk.common.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.common.spi.IEventHandler;
import cn.hexing.fk.common.spi.IEventHook;
import cn.hexing.fk.common.spi.IEventTrace;
import cn.hexing.fk.common.spi.IModule;
import cn.hexing.fk.common.spi.abstra.BaseModule;
import cn.hexing.fk.common.threadpool.ThreadPool;
import cn.hexing.fk.message.gate.MessageGate;
import cn.hexing.fk.tracelog.TraceLog;

/**
 *
 */
public class BasicEventHook extends BaseModule implements IEventHook {
	//静态属性
	private static final Logger log = Logger.getLogger(BasicEventHook.class);
	private static final TraceLog tracer = TraceLog.getTracer();
	//可配置属性
	protected int minSize = 2;		//事件处理的线程池大小
	protected int maxSize = 20;
	protected int timeoutAlarm = 2;	//事件处理超时时间(秒)
	protected String name="evhandler"; //什么服务的事件处理器，定义英文名
	protected Map<EventType,IEventHandler> handlerMap;
	protected Object[] sources = null; //所有被hooked事件源头。
	protected IEventTrace eventTrace = null;

	//可选属性
	protected Map<EventType,IEventHandler> include;
	protected List<EventType> exclude;		//包含或者排除的事件列表。派生类配置使用。
	protected int threadPriority = Math.max(Thread.MAX_PRIORITY-2,Thread.NORM_PRIORITY);
	protected EventQueue queue = new EventQueue();		//如果没有配置，则在start中创建
	
	//内部属性
	protected ThreadPool pool = null;
	protected boolean initialized = false;
	protected IEventHandler[] handlers = new IEventHandler[EventType.getMaxIndex()+1];
	private long lastEventTime = System.currentTimeMillis();
	
	public String getModuleType() {
		return IModule.MODULE_TYPE_EVENT_HOOK;
	}

	public boolean isActive() {
		return null != pool && pool.isActive();
	}

	/**
	 * 事件钩子的profile信息。
	 */
	public String profile(){
		StringBuffer sb = new StringBuffer(256);
		sb.append("\r\n<eventhook-profile type=\"").append(getModuleType()).append("\">");
		sb.append("\r\n    ").append("<name>").append(name).append("</name>");
		sb.append("\r\n    ").append("<state>").append(isActive()).append("</state>");
		sb.append("\r\n    ").append("<queue-size>").append(queue.size()).append("</queue-size>");
		sb.append("\r\n    ").append("<source>").append(getSource()).append("</source>");
		
		sb.append("\r\n    ").append("<tp-minsize>").append(pool.getMinSize()).append("</tp-minsize>");
		sb.append("\r\n    ").append("<tp-maxsize>").append(pool.getMaxSize()).append("</tp-maxsize>");
		sb.append("\r\n    ").append("<tp-size>").append(pool.size()).append("</tp-size>");
		sb.append("\r\n    ").append("<tp-timeout-alarm>").append(pool.getTimeoutAlarm()).append("</tp-timeout-alarm>");
		sb.append("\r\n    ").append("<tp-works>").append(pool.toString()).append("</tp-works>");
		sb.append("\r\n</eventhook-profile>");
		return sb.toString();
	}

	/**
	 * 类加载完毕后，需要调用初始化函数，以便增加或者排除事件类型。
	 */
	public void init(){
		if( initialized )
			return;
		if( null == handlerMap )
			handlerMap = new HashMap<EventType,IEventHandler>();
		if( null != include ){
			Iterator<Map.Entry<EventType,IEventHandler>> it = include.entrySet().iterator();
			while(it.hasNext() ){
				Map.Entry<EventType,IEventHandler> entry = it.next();
				handlerMap.put(entry.getKey(), entry.getValue());
			}
		}
		if( null != exclude ){
			Iterator<EventType> it = exclude.iterator();
			while(it.hasNext())
				handlerMap.remove(it.next());
		}
		initialized = true;
	}

	public boolean canHook( IEvent e ){
		//未控制
		if( null == sources )
			return true;
		if( null != sources ){
			for( Object iter: sources ){
				if( iter == e.getSource() ){
					return true;
				}
			}
		}
		return false;
	}
	
	public void postEvent(IEvent e) {
		if( !canHook(e) )
			return;
		if( e.getType() == EventType.ACCEPTCLIENT || e.getType() == EventType.CLIENTCLOSE ){
			handleEvent(e);
			return;
		}
		if( null != e.getMessage() && e.getMessage().isHeartbeat() ){
			if( e.getMessage() instanceof MessageGate ){
				handleEvent(e);
				return;
			}
		}
		//事件系统来说，事件是不能丢弃的。但是考虑到系统的正常运行，这里提供1秒中的插入尝试时间。
		//事件处理应该是高效的。如果失败，则需要跟踪日志，以便调整事件队列大小。
		
		int cnt = 10;
		while( cnt-- > 0 ){
			try{
				queue.offer(e);
				break;
			}catch(Exception exp){
				//事件插入队列失败，是非常严重的事情。
				String info = "BasicEventHook can not postEvent. reason is "+exp.getLocalizedMessage()+". event is"+e.toString();
				log.fatal(info,exp);
				tracer.trace(info,exp);
				try{
					Thread.sleep(50);
				}catch(Exception te){}
			}
		}
	}
	
	protected void createThreadPool(){
		pool = new ThreadPool(this,queue);
		pool.setThreadPriority(threadPriority);
		pool.setMinSize(minSize);
		pool.setMaxSize(maxSize);
		pool.setTimeoutAlarm(timeoutAlarm);
		pool.setName(name);
	}
	
	public boolean start() {
		init();
		if( null != pool && pool.isActive() )
			return true;
		//需要把handlerMap中所有EventType注册到GlobalEventHandler
		for(EventType type: handlerMap.keySet()){
			if( type.toInt()>= handlers.length ){
				IEventHandler[] temp = new IEventHandler[type.toInt()*2];
				System.arraycopy(handlers, 0, temp, 0, handlers.length);
				handlers = temp;
			}
//			System.out.println("len="+handlers.length+",index="+type.toInt());
			handlers[type.toInt()] = handlerMap.get(type);
			GlobalEventHandler.registerHook(this,type);
		}
		createThreadPool();
		pool.start();
		
		return true;
	}

	public void stop( ) {
		//stop event hook gracefully
		queue.enableOffer(false);
		int cnt = 100;
		while( cnt-- > 0 && queue.size()>0 && queue.enableTake() ){
			//让事件处理完毕
			try{
				Thread.sleep(50);
			}catch(Exception exp){}
		}
		for(EventType type: handlerMap.keySet()){
			GlobalEventHandler.deregisterHook(this,type);
		}
		if( null != pool )
			pool.stop();
		pool = null;
	}

	public void handleEvent(IEvent event) {
		IEventHandler handler = handlers[event.getType().toInt()];
		if( null != handler ){
			handler.handleEvent(event);
			lastEventTime = System.currentTimeMillis();
			
			if( null != eventTrace ){
				try{
					eventTrace.traceEvent(event);
				}catch(Exception e){
					log.warn("事件跟踪器执行异常:"+e.getLocalizedMessage(),e);
				}
			}
		}
	}

	/**
	 * 上次事件处理时间。
	 * @return
	 */
	public long getLastEventTime(){
		return lastEventTime;
	}
	
	public int getMinSize() {
		return minSize;
	}

	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getTimeoutAlarm() {
		return timeoutAlarm;
	}

	public void setTimeoutAlarm(int timeoutAlarm) {
		this.timeoutAlarm = timeoutAlarm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<EventType, IEventHandler> getHandlerMap() {
		return handlerMap;
	}

	public void setHandlerMap(Map<EventType, IEventHandler> handlerMap) {
		this.handlerMap = handlerMap;
	}
	
	public void addHandler(EventType type,IEventHandler handler){
		if( null == handlerMap )
			handlerMap = new HashMap<EventType,IEventHandler>();
		handlerMap.put(type, handler);
	}

	public int getThreadPriority() {
		return threadPriority;
	}

	public void setThreadPriority(int threadPriority) {
		this.threadPriority = threadPriority;
	}

	public Object getSource() {
		if( null != sources && sources.length>0 )
			return sources[0];
		else
			return null;
	}

	public void setSource(Object source) {
		if( null == sources ){
			sources = new Object[1];
			sources[0] = source;
		}
		else{
			for( Object src : sources ){
				if( src == source )
					return;
			}
			Object[] dest = new Object[sources.length+1];
			System.arraycopy(sources, 0, dest, 0, sources.length);
			dest[sources.length] = source;
			sources = null;
			sources = dest;
		}
	}
	
	public void addSource(Object source){
		setSource(source);
	}
	
	public void setSource(List<Object> srcs){
		if( null == srcs || srcs.size()==0 )
			return;
		setSource(srcs.toArray());
	}
	
	public void setSource(Object[] srcs ){
		if( null == sources ){
			sources = srcs;
		}
		else{
			//排除重复对象
			Vector<Object> uniqs = new Vector<Object>();
			for(Object src: srcs ){
				boolean found = false;
				for(Object iter: sources ){
					if( iter == src ){
						found = true;
						break;
					}
				}
				if( !found )
					uniqs.add(src);
			}
			Object[] srcss = uniqs.toArray();
			Object[] dest = new Object[sources.length + srcss.length];
			System.arraycopy(sources, 0, dest, 0, sources.length);
			System.arraycopy(srcss, 0, dest, sources.length, srcss.length);
			sources = null;	srcss = null;
			sources = dest;
		}
	}
	
	public void addSource(Object[] srcs ){
		setSource(srcs);
	}

	public void setExclude(List<EventType> exclude) {
		this.exclude = exclude;
	}

	public void setInclude(Map<EventType, IEventHandler> include) {
		this.include = include;
	}

	public void setEventTrace(IEventTrace eTrace){
		eventTrace = eTrace;
	}

	public void setQueue(EventQueue queue) {
		this.queue = queue;
	}
}
