/**
 * 线程池所管理的工作单元接口。
 */
package cn.hexing.fk.common.spi;

/**
 */
public interface IEventHandler{
	/**
	 * 提供事件处理函数
	 */
	void handleEvent(IEvent event);
	
}
