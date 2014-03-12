/**
 * 所有Socket服务器对象公共接口定义，包括异步TCP服务器以及同步UDP服务器。
 */
package cn.hexing.fk.common.spi.socket;

import java.nio.ByteBuffer;

import cn.hexing.fk.common.spi.IModStatistics;
import cn.hexing.fk.common.spi.IModule;
import cn.hexing.fk.message.IMessage;

/**
 *
 */
public interface ISocketServer extends IModStatistics,IModule{
	/**
	 * 服务器侦听端口
	 * @return
	 */
	int getPort();
	
	/**
	 * 为了支持终端连接到的外网实际对应IP地址，需要配置IP:PORT。
	 * 本socket服务器获取的ip地址，是映射到内网的地址。
	 * @return
	 */
	String getServerAddress();
	
	/**
	 * 服务器的IO处理器，用于处理收到数据流或者需要发送数据流。
	 * IOHandler能够从收到数据流中解析出完整的消息，或者把消息对象变成数据流发送出去。
	 * @return
	 */
	IClientIO getIoHandler();
	
	/**
	 * 服务器的IO处理线程池大小。对于终端TCP服务器，建议每100个终端配置1个IO线程。
	 * @return
	 */
	int getIoThreadSize();
	
	/**
	 * 当客户端断开连接时，从服务器所维护的列表中删除。
	 * @param client
	 */
	void removeClient(IServerSideChannel client);
	
	int getClientSize();	//服务器已经连接的socket client数量。
	/**
	 * 返回本服务器所管理的client数组。
	 * @return
	 */
	IServerSideChannel[] getClients();
	
	/**
	 * 本服务器创建自己能够处理的消息对象，以便自己的IOHandler进行读写处理。
	 * @return
	 */
	IMessage createMessage(ByteBuffer buf);
	
	/**
	 * 本服务器设置缺省socket缓冲区长度。
	 * @return
	 */
	int getBufLength();
	boolean useDirectBuffer();
	String getMonitedIPs();
	

	/**
	 * 用于通信流量控制。防止过渡读取数据，导致来不及应答。
	 * @return
	 */
	int getMaxContinueRead();
	int getWriteFirstCount();
	
	/**
	 * 服务器对象对统计信息的处理
	 */
	void setLastReceiveTime(long lastRecv);
	void setLastSendTime(long lastSend);
	void incRecvMessage();
	void incSendMessage();
}
