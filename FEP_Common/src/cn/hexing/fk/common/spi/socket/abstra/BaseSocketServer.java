package cn.hexing.fk.common.spi.socket.abstra;

import java.nio.ByteBuffer;

import cn.hexing.fk.common.spi.IModule;
import cn.hexing.fk.common.spi.socket.IClientIO;
import cn.hexing.fk.common.spi.socket.IServerSideChannel;
import cn.hexing.fk.common.spi.socket.ISocketServer;
import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.IMessageCreator;
import cn.hexing.fk.message.MultiProtoRecognizer;
import cn.hexing.fk.message.gate.MessageGateCreator;
import cn.hexing.fk.utils.CalendarUtil;

/**
 * 异步TCP服务器、UDP服务器的基础抽象类。
 *
 */
public abstract class BaseSocketServer implements ISocketServer {
	//可通过spring配置的属性
	protected String name = "async tcp server";
	protected String ip = null;		//支持侦听某个具体IP地址。
	protected int port = -1;		 //侦听端口
	protected int bufLength = 256;	 //socket客户端缓冲区大小
	protected boolean useDirectBuffer = true;
	protected int ioThreadSize = 2;	 //socket IO 线程池大小
	protected IMessageCreator messageCreator = new MessageGateCreator();
	//SocketChannel读写接口实现类路径。
	protected IClientIO ioHandler = null;	//SocketChannel读、写操作接口
	protected String monitedIPs = "";		//被监控的IP列表,以,分割
	/**
	 * 通讯方式定义：
	 * 01:短信; 02:GPRS;  03:DTMF;  04:Ethernet;
	 * 05:红外; 06:RS232; 07:CSD;   08:Radio; 	09:CDMA;
	 */
	protected String txfs = "02";			//默认GPRS通信方式
	protected int timeout = 30*60;		//默认30 分钟超时
	//允许增加配置属性：发送优先控制属性
	private int writeFirstCount = 100;		//发送次数之后才允许读操作。0表示发送不优先。1，表示发送一次之后才允许读
	private int maxContinueRead = 100;		//最大连续读取消息个数，以免来不及应答。
	private String serverAddress=null;

	//统计属性,初始化自动为0
	protected long lastReceiveTime=0,lastSendTime=0;			//最新收、发时间
	protected long totalRecvMessages=0,totalSendMessages=0;	//总共收、发消息总数
	protected int msgRecvPerMinute=0,msgSendPerMinute=0;		//每分钟收、发报文个数
	protected Object statisticsRecv = new Object() ,statisticsSend = new Object();
	protected String moduleType = IModule.MODULE_TYPE_SOCKET_SERVER;
	
	public IMessage createMessage(ByteBuffer buf) {
		IMessage msg = messageCreator.create();
		if( null == msg )
			msg = MultiProtoRecognizer.recognize(buf);
		return msg;
	}

	public int getBufLength() {
		return bufLength;
	}
	
	public void setBufLength(int bufLen ){
		bufLength = bufLen;
	}
	
	public void setUseDirectBuffer( boolean useDirect ){
		useDirectBuffer = useDirect;
	}
	
	public boolean useDirectBuffer(){
		return useDirectBuffer;
	}

	public abstract int getClientSize();

	public abstract IServerSideChannel[] getClients();

	public IClientIO getIoHandler() {
		return ioHandler;
	}
	
	public void setIoHandler(IClientIO ioh) {
		ioHandler = ioh;
	}

	public int getIoThreadSize() {
		return this.ioThreadSize;
	}
	
	public void setIoThreadSize(int iotSize ){
		ioThreadSize = iotSize;
	}

	public int getMaxContinueRead() {
		return this.maxContinueRead;
	}
	
	public void setMaxContinueRead(int mcRead){
		this.maxContinueRead = mcRead;
	}

	public int getPort() {
		return port;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public void setIp(String ip){
		this.ip = ip;
	}

	public void setServerAddress(String serverAddress){
		this.serverAddress = serverAddress;
	}
	
	public String getServerAddress(){
		if( null != serverAddress )
			return serverAddress;
		else
			return "127.0.0.1:"+port;
	}
	
	public int getWriteFirstCount() {
		return this.writeFirstCount;
	}
	
	public void setWriteFirstCount(int fcount){
		this.writeFirstCount = fcount;
	}

	public void incRecvMessage() {
		synchronized(statisticsRecv){
			msgRecvPerMinute++;
			totalRecvMessages++;
		}
	}

	public void incSendMessage() {
		synchronized(statisticsSend){
			msgSendPerMinute++;
			totalSendMessages++;
		}
	}

	public void removeClient(IServerSideChannel client) {
		if( this.getClientSize() == 0 ){
			synchronized(statisticsRecv){
				this.totalRecvMessages = 0;
				this.msgRecvPerMinute = 0;
			}
			synchronized(statisticsSend){
				this.totalSendMessages = 0;
				this.msgSendPerMinute = 0;
			}
		}
	}

	public void setLastReceiveTime(long lastRecv) {
		this.lastReceiveTime = lastRecv;
	}

	public void setLastSendTime(long lastSend) {
		this.lastSendTime = lastSend;
	}

	public long getLastReceiveTime() {
		return this.lastReceiveTime;
	}

	public long getLastSendTime() {
		return this.lastSendTime;
	}

	public int getMsgRecvPerMinute() {
		return this.msgRecvPerMinute;
	}

	public int getMsgSendPerMinute() {
		return this.msgSendPerMinute;
	}

	public long getTotalRecvMessages() {
		return this.totalRecvMessages;
	}

	public long getTotalSendMessages() {
		return this.totalSendMessages;
	}

	public String getModuleType() {
		return null;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getTxfs() {
		return txfs;
	}
	
	public void setTxfs(String txfs){
		this.txfs = txfs;
	}

	public abstract boolean isActive();

	public abstract boolean start();

	public abstract void stop();

	public String profile() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("\r\n<sockserver-profile type=\"").append(getModuleType()).append("\">");
		sb.append("\r\n    ").append("<name>").append(name).append("</name>");
		sb.append("\r\n    ").append("<state>").append(isActive()).append("</state>");
		sb.append("\r\n    ").append("<port>").append(port).append("</port>");

		sb.append("\r\n    ").append("<ioThreadSize>").append(ioThreadSize).append("</ioThreadSize>");
		sb.append("\r\n    ").append("<clientSize>").append(getClientSize()).append("</clientSize>");
		sb.append("\r\n    ").append("<timeout>").append(timeout).append("</timeout>");

		sb.append("\r\n    ").append("<txfs>").append(this.txfs).append("</txfs>");
		sb.append("\r\n    ").append("<totalRecv>").append(totalRecvMessages).append("</totalRecv>");
		sb.append("\r\n    ").append("<totalSend>").append(totalSendMessages).append("</totalSend>");
		sb.append("\r\n    ").append("<perMinuteRecv>").append(msgRecvPerMinute).append("</perMinuteRecv>");
		sb.append("\r\n    ").append("<perMinuteSend>").append(msgSendPerMinute).append("</perMinuteSend>");

		String stime = CalendarUtil.getTimeString(lastReceiveTime);
		sb.append("\r\n    ").append("<lastRecv>").append(stime).append("</lastRecv>");
		stime = CalendarUtil.getTimeString(lastSendTime);
		sb.append("\r\n    ").append("<lastSend>").append(stime).append("</lastSend>");
		sb.append("\r\n</sockserver-profile>");
		return sb.toString();
	}

	public void setMessageCreator(IMessageCreator messageCreator) {
		this.messageCreator = messageCreator;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getMonitedIPs() {
		return monitedIPs;
	}

	public void setMonitedIPs(String monitedIPs) {
		this.monitedIPs = monitedIPs;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

}
