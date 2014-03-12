/**
 * 通信前置机、网关等，都需要一个消息队列来实现消息的上下行。
 */
package cn.hexing.fk.common.spi;

import cn.hexing.fk.message.IMessage;

/**
 *
 */
public interface IMessageQueue {

	boolean sendMessage(IMessage msg);
	IMessage take()throws InterruptedException;
	IMessage poll();
	void offer(IMessage msg);
	int size();
}
