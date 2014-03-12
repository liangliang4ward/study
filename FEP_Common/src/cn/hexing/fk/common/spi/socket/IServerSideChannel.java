/**
 * 针对异步TCP服务器、UDP服务器端的socket client通道对象接口定义。
 */
package cn.hexing.fk.common.spi.socket;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import cn.hexing.fk.message.IMessage;

/**
 */
public interface IServerSideChannel extends IChannel{
	/**
	 * 对于tcp类型client，返回socket channel
	 * @return
	 */
	SocketChannel getChannel();
	
	/**
	 * ClientChannel持续写对象的事件。每次读对象后，持续写对象清0。
	 * 用于读、写消息的优先级控制。
	 * @return
	 */
	int getLastingWrite();
	void setLastingWrite(int writeCount);
	
	/**
	 * 对于UDP类型 client,返回 SocketAddress
	 * @return
	 */
	SocketAddress getSocketAddress();
	
	/**
	 * 用于管理本socket client通道的当前Message读、写。
	 * 被IClientIO接口调用。
	 * @return
	 */
	IMessage getCurReadingMsg();
	void setCurReadingMsg(IMessage curReadingMsg);
	IMessage getCurWritingMsg();
	void setCurWritingMsg(IMessage curWritingMsg);
	//Modified by bhw 2009-1-17 15:29 to avoid message resend. 2 function added.
	//Affected class is AsyncSocketClient and UdpClient.
	/**
	 * 缓冲区是否还有数据需要继续发送
	 * @return
	 */
	boolean bufferHasRemaining();
	void setBufferHasRemaining(boolean hasRemaining);
	/**
	 * 取得需要本client继续发送的新消息对象
	 * @return
	 */
	IMessage getNewSendMessage();
	
	/**
	 * 异步tcp server、UDP server的client channnel读写缓冲区。
	 * @return
	 */
	ByteBuffer getBufRead();
	ByteBuffer getBufWrite();
}
