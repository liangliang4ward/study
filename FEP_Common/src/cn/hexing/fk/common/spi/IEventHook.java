/**
 * 事件处理钩子。全局事件处理器捕获的特定类型事件将被钩子处理。
 */
package cn.hexing.fk.common.spi;


/**
 *
 */
public interface IEventHook extends IEventHandler{
	/**
	 * 设置消息源，只有source与Event中的source相同的事件才被执行。
	 * @param source
	 */
	void setSource(Object source);
	/**
	 * 异步事件处理 通知
	 * @param e
	 */
	void postEvent(IEvent e);
	
	/**
	 * 启动EventHook对象，以便进行事件处理
	 */
	boolean start();
	
	/**
	 * 停止EventHook对象的事件处理。
	 */
	void stop();
	
	/**
	 * 事件处理器的profile信息，如事件队列大小等。
	 * 格式：<profile>
	 * 			<queue size="123"/>
	 * 			<threads size="4"/>
	 * 		</profile>
	 * @return
	 */
	String profile();
	
	/**
	 * 上次事件处理时间。
	 * @return
	 */
	long getLastEventTime();
	
	void setEventTrace(IEventTrace eTrace);
}
