/**
 * 事件处理接口
 */
package cn.hexing.fk.common.spi;

/**
 *
 */
public interface IEventPump {
	/**
	 * 异步方式把事件放入到事件队列。由事件调度线程进行事件分发，交给事件钩子（多线程方式）进行处理。
	 * @param e
	 */
	void post(IEvent e);
}
