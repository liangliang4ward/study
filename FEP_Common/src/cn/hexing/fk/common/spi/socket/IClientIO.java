/**
 * 异步TCP通讯的客户端socket读、写接口
 */
package cn.hexing.fk.common.spi.socket;

import cn.hexing.fk.exception.SocketClientCloseException;

/**
 */
public interface IClientIO {

	/**
	 * 当SocketIoThread检测到读数据事件，则调用该接口。
	 * @param client
	 * @return true if all data received, false socket缓冲区还有数据没有读取。
	 */
	boolean onReceive(IServerSideChannel client) throws SocketClientCloseException;
	
	/**
	 * 当SocketIoThread检测到可以发送数据事件，调用该接口。
	 * @param client
	 * @return true，如果全部数据发送完毕；false，还有数据没有发送完毕。
	 */
	boolean onSend(IServerSideChannel client)throws SocketClientCloseException;
}
