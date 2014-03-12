package cn.hexing.fk.common.events;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import cn.hexing.fk.common.EventType;
import cn.hexing.fk.common.spi.IEvent;
import cn.hexing.fk.exception.EventQueueFullException;
import cn.hexing.fk.exception.EventQueueLockedException;
import cn.hexing.fk.tracelog.TraceLog;

/**
 * <p>Title: Java Socket Server with NIO support </p>
 * <p>Description:实现事件队列 </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 */

public class EventQueue	implements Serializable 
{
	private static final long serialVersionUID = 200603141443L;
	private static final TraceLog tracer = TraceLog.getTracer();
	
	private static final int DEFAULT_QUEUE_SIZE = 1024;
	private int capacity = 1024*100;
	private final Object lock = new Object();

	private IEvent[] events ;
	private int first = 0;
	private int last = 0;
	private int size = 0;
	private int waiting = 0;
	
	private boolean writable = true;
	private boolean readable = true; 

	/**
	 * Construct a new, empty <code>Queue</code> with the specified initial
	 * capacity.
	 */
	public EventQueue(int initialCapacity) {
		events = new IEvent[initialCapacity];
	}

	/**
	 * construct a new queue with default initial capacity
	 */

	public EventQueue() {
		events = new IEvent[DEFAULT_QUEUE_SIZE];
	}

	/**
	 * 从队列彻底移除所有元素.
	 */
	public void clear() {
		synchronized(lock){
			Arrays.fill(events, null);
			first = 0;
			last = 0;
			size = 0;
			lock.notifyAll();
		}
	}
	
	/**
	 * 检索并移除此队列的头部，如果此队列不存在任何元素，则一直等待。
	 * 注意take 与poll的差异。
	 * @return
	 */
	public IEvent take() throws InterruptedException{
		synchronized(lock){
			IEvent e=null;
			waiting++;
			while( null == (e=poll()) ){
				lock.wait();
			}
			waiting--;
			return e;
		}
	}
	
	/**
	 * 将指定的元素添加到队列的尾部，如有必要，则等待空间变得可用。
	 * 注意put与offer的差异。
	 * @param evt
	 * 抛出：
	 * 		InterruptedException - 如果在等待时中断。
	 * 		NullPointerException - 如果指定的元素为 null。
	 */
	public void put( IEvent evt) throws InterruptedException{
		if( null == evt )
			throw new NullPointerException();
		throw new RuntimeException("暂未实现该功能。");
	}
	
	/**
	 * Inserts the given element at the beginning of this queue
	 * @param evt
	 * @return
	 */
	public boolean addFirst(IEvent evt) throws EventQueueLockedException,EventQueueFullException{
		if( null == evt )
			return false;
		synchronized(lock){
			if( evt.getType() != EventType.SYS_KILLTHREAD  && !writable )
				throw new EventQueueLockedException("Invalid offer while eventQueue disable put into.");
			if (size == events.length) {
				// expand queue
				if( size>= capacity ){
					if( evt.getType() == EventType.ACCEPTCLIENT ){
						//必须插入到队列
						events[first] = evt;
						return true;
					}
					//超过队列允许的最大值，不能插入到队列中.
					String info = "超过队列允许的最大值，不能插入到队列中。size="+size;
					tracer.trace(info);
					throw new EventQueueFullException(info);
				}
				final int oldLen = events.length;
				IEvent[] newEvents = new IEvent[oldLen * 2];

				if (first < last) {
					System.arraycopy(events, first, newEvents, 0, last - first);
				} else {
					System.arraycopy(events, first, newEvents, 0, oldLen - first);
					System.arraycopy(events, 0, newEvents, oldLen - first, last);
				}

				first = 0;
				last = oldLen;
				events = newEvents;
			}

			//插入头部
			if( --first < 0 ){
				first = events.length-1;
			}
			events[first] = evt;
			size++;

			if (waiting > 0) {
				lock.notifyAll();
			}
			return true;
		}
	}

	/**
	 * 检索并移除此队列的头，如果此队列为空，则返回 null。
	 * @return
	 */
	public IEvent poll() {
		synchronized(lock){
			if (size == 0 ) {
				return null;
			}
			if( !readable ){
				//如果头部事件是killThread，允许返回
				if( events[first].getType() != EventType.SYS_KILLTHREAD )
					return null;
			}
			IEvent event = events[first];
			events[first] = null;
			first++;

			if (first == events.length) {
				first = 0;
			}

			size--;
			return event;
		}
	}
	
	/**
	 * Removes at most the given number of available elements from this queue and adds them 
	 * into the given collection。
	 * 需要等待timeout milliseconds时间
	 * @return the number of elements transferred.
	 */
	public int drainTo(Collection<IEvent> c,int maxElements,long timeout) {
		if( timeout<0 )
			timeout = 0;
		synchronized(lock){
			long mark = System.currentTimeMillis();
			if( maxElements<=0 )
				maxElements = size;
			for(int i=0;i<maxElements; i++){
				while (size == 0) {
					if( System.currentTimeMillis()-mark >= timeout )
						return i;
					try{
						if( timeout>0 )
							lock.wait(timeout);
					}
					catch(Exception e){}
				}
				c.add(events[first]);
				events[first] = null;
				first++;
				if (first == events.length) {
					first = 0;
				}
				size--;
			}
			return maxElements;
		}
	}
	
	/**
	 * 如果可能，在队列尾部插入指定的元素，如果队列已满则立即返回。
	 * @param o 要添加的元素
	 * @return 如果可以向此队列添加元素，则返回 true；否则返回 false。
	 * 抛出： NullPointerException - 如果指定的元素为 null。
	 */
	public boolean offer(IEvent evt) throws EventQueueLockedException,EventQueueFullException,NullPointerException
	{
		if( null == evt )
			throw new NullPointerException();
		synchronized(lock){
			if( evt.getType() != EventType.SYS_KILLTHREAD  && ! writable )
				throw new EventQueueLockedException("Invalid offer while eventQueue disable put into.");

			if( null != evt.getMessage() && evt.getMessage().isHeartbeat())
				return this.addFirst(evt);
			if( evt.getType() == EventType.ACCEPTCLIENT || evt.getType() == EventType.CLIENTCLOSE )
				return this.addFirst(evt);

			int delta = Math.max(50, capacity/100);
			if( size+delta >= capacity ){
				//超过队列允许的最大值，不能插入到队列中.
				String info = "超过队列允许的最大值，不能插入到队列中。size="+size;
				tracer.trace(info);
				throw new EventQueueFullException(info);
			}
			if (size == events.length) {
				// expand queue
				final int oldLen = events.length;
				IEvent[] newEvents = new IEvent[oldLen * 2];

				if (first < last) {
					System.arraycopy(events, first, newEvents, 0, last - first);
				} else {
					System.arraycopy(events, first, newEvents, 0, oldLen - first);
					System.arraycopy(events, 0, newEvents, oldLen - first, last);
				}

				first = 0;
				last = oldLen;
				events = newEvents;
			}

			events[last++] = evt;

			if (last == events.length) {
				last = 0;
			}

			size++;

			if (waiting > 0) {
				lock.notifyAll();
			}
			return true;
		}
	}


	/**
	 * Returns <code>true</code> if the queue is empty.
	 */
	public boolean isEmpty() {
		return (size == 0);
	}

	/**
	 * Returns the number of elements in the queue.
	 */
	public int size() {
		return size;
	}
	
	public void setCapacity(int capacity){
		this.capacity = capacity;
	}
	
	public int capacity(){
		return capacity;
	}
	
	/**
	 * false,不允许从插入元素到队列（除了SYS_KILLTHREAD事件）
	 * true,允许插入元素到队列
	 */
	public void enableOffer(boolean putable){
		synchronized(lock){
			this.writable = putable;
		}
	}
	
	public boolean enableOffer(){
		return writable;
	}
	
	/**
	 * 
	 */
	public void enableTake(boolean takable){
		synchronized(lock){
			readable = takable;
			if( takable && size>0 )
				lock.notifyAll();
		}
	}
	
	public boolean enableTake(){
		return readable;
	}
	
	public void lockQueue(){
		enableOffer(false);
		enableTake(false);
	}
	
	public void unlockQueue(){
		enableTake(true);
		enableOffer(true);
	}
	
}