/**
 * 通信链路抽象接口：包含
 * 1）所有TCP、UDP server端的客户对象通信链路；
 * 2）TCP、UDP client端通信链路；
 * 3）串口通信的链路；
 */
package cn.hexing.fk.common.spi.socket;

import cn.hexing.fk.message.IMessage;

/**
 */
public interface IChannel {
	void close();

	/**
	 * 通过client对象发送消息。无论异步或者同步发送。
	 * @param msg
	 */
	boolean send(IMessage msg);
	
	/**
	 * 针对异步tcp socket client对象，返回发送队列大小。
	 * @return
	 */
	int sendQueueSize();
	void setMaxSendQueueSize(int maxSendQueueSize);
	
	/**
	 * 返回异步TCP socket server或者UDP socket server对象;
	 * 对于异步socket client连接池，返回连接池对象（必须实现ISocketServer)
	 * @return
	 */
	ISocketServer getServer();
	
	/**
	 * 如果ClientChannel是异步通信方式，一定归属于某个IOThread对象。
	 * @param threadObj
	 */
	void setIoThread(Object threadObj);

	String getPeerIp();
	int getPeerPort();
	/**
	 * 返回 peerIp:peerPort字符串
	 * @return
	 */
	String getPeerAddr();
	
	/**
	 * 通道的通信时间管理。 ioTime，读操作时间 readTime。
	 * @return
	 */
	long getLastIoTime();
	void setLastIoTime();	//把当前时间设置为本次IO发生时间
	long getLastReadTime();
	void setLastReadTime(); //把当前时间作为读消息的时间。
	
	/**
	 * 支持客户端请求服务器发送的报文数量。如果
	 * requestNum递减到0，则服务器不再发送报文，等到客户端请求发送。
	 * @param reqNum
	 */
	void setRequestNum(int reqNum);
	int  getRequestNum();

}
