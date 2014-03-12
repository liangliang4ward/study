/**
 * 网关消息
 */
package cn.hexing.fk.message.gate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.hexing.fk.common.spi.socket.IChannel;
import cn.hexing.fk.exception.MessageParseException;
import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.MessageType;
import cn.hexing.fk.message.MultiProtoRecognizer;
import cn.hexing.fk.utils.HexDump;

/**
 *
 */
public class MessageGate implements IMessage {
	private static final Logger log = Logger.getLogger(MessageGate.class);
	private static final byte[] zeroPacket = new byte[0];
	private static final ByteBuffer emptyData = ByteBuffer.wrap(zeroPacket);
	protected MessageType type = MessageType.MSG_GATE;
	private long ioTime = System.currentTimeMillis();
	private String peerAddr;	//对方的IP:PORT地址。对于网关来说，没有实际意义。
	private String txfs;
    /** 组帧命令结果状态 */
    private String status;
    /** 下行数据库命令ID */
    private Long cmdId;
    
    /**连接模式  0为普通连接,其他为主动连接终端的模式1为外置模块,2为CSD*/
    public int linkMode = 0;
	
	private IChannel source;
	protected GateHead head = new GateHead();
	protected ByteBuffer data = emptyData;
	private ByteBuffer rawPacket=null;				//网关原始报文
	//对象内部属性
	private int state = IMessage.STATE_INVALID;		//辅助消息对象的完整读取或者发送
	private int priority = IMessage.PRIORITY_LOW;	//low priority
	//网关消息一般包含一个浙江规约消息对象（终端上行、前置机命令下行）
	private IMessage innerMessage = null;
	private String serverAddress;

	/** 内部通信：心跳，数据区为每个心跳携带的允许批量发送报文的数量 */
	public static final short CMD_WRAP = 0x00;	//默认情况下，读取一个浙江规约消息。
	public static final short MASTER_FE_CMD = 0x01;	//主站对FE下达通知(indication)，如短信号码设置，心跳报文保存设置
	public static final short FE_MASTER_REP = 0x02;	//FE对主站命令的执行应答

	public static final short CMD_GATE_HREQ = 0x11;
	/** 内部通信：心跳的应答报文 */
	public static final short CMD_GATE_HREPLY = 0x12;

	/** 前置机(主站)第一次连接网关时的参数传递 */
	public static final short CMD_GATE_PARAMS = 0x20;
	/** 前置机(主站)命令下行 */
	public static final short CMD_GATE_REQUEST = 0x21;
	/** 网关报文上行 */
	public static final short CMD_GATE_REPLY = 0x22;
	
	/**
	 * 收到报文的确认帧。如收到前置机请求，并成功(或者失败)发送给终端;
	 * 前置机收到网关的任务上报的确认。
	 */
	public static final short CMD_GATE_CONFIRM = 0x23;
	
	/**
	 * 网关下行失败，需要把下行报文转发给通信前置机，以便走短信通道再次发送。
	 */
	public static final short CMD_GATE_SENDFAIL = 0x24;
	public static final short CMD_RTU_CLOSE = 0x25;
	
	public static final short REQ_MONITOR_RELAY_PROFILE = 0x31;		//通过网关消息，传递profile监控请求
	public static final short REP_MONITOR_RELAY_PROFILE = 0x32;		//通过网关消息，传递profile监控应答
	
	public long getIoTime() {
		return ioTime;
	}

	public MessageType getMessageType() {
		return MessageType.MSG_GATE;
	}

	public String getPeerAddr() {
		return peerAddr;
	}

	public int getPriority() {
		return priority;
	}

	public byte[] getRawPacket() {
		if( null != rawPacket )
			return rawPacket.array();
		else
			return zeroPacket;
	}

	public String getRawPacketString() {
		if( null != rawPacket )
			return HexDump.hexDumpCompact(rawPacket);
		else
			return "";
	}

	public IChannel getSource() {
		return source;
	}

	public boolean read(ByteBuffer readBuffer) throws MessageParseException{
		synchronized(this){
			return _read(readBuffer);
		}
	}
	
	public boolean _read(ByteBuffer readBuffer) throws MessageParseException{
		if( state == IMessage.STATE_INVALID && readBuffer.remaining()<13 ){
			if( log.isDebugEnabled() )
				log.debug("长度不足读取网关报文头，等会儿继续读取。readBuffer.remaining="+readBuffer.remaining());
			return false;		//长度明显不足，等会儿继续读取
		}
		if( type != MessageType.MSG_GATE &&type != MessageType.MSG_WEB && type != MessageType.MSG_INVAL ){
			//如果网关规约在消息测试中发现不是网关规约，那么innerMessage一定已经创建。
			boolean ret = innerMessage.read(readBuffer);
			if( ret )
				onReadFinished();
			return ret;
		}
		//未知类型消息，则先检测消息类型。如果是网关规约，则进行读取。
		if( IMessage.STATE_INVALID == state || IMessage.STATE_READ_DONE == state ){	//消息头没有读取
			innerMessage = MultiProtoRecognizer.recognize(readBuffer);
			if( null == innerMessage )
				return false;
			if( ! (innerMessage instanceof MessageGate ) ){
				type = innerMessage.getMessageType();
				head.setCommand(CMD_WRAP);
				boolean ret = innerMessage.read(readBuffer);
				if( ret ){
					//纯粹浙江规约或国网规约报文
					onReadFinished();
					rawPacket = HexDump.toByteBuffer(innerMessage.getRawPacketString());
				}
				return ret;
			}
			else
				innerMessage = null;
			if( readBuffer.remaining()<13 ){
				if( log.isDebugEnabled() )
					log.debug("网关对报文进行分析后，长度不足以读取网关报文头。readBuffer.remaining="+readBuffer.remaining());
				return false;
			}
			state = IMessage.STATE_READ_HEAD;
			boolean ret = head.read(readBuffer);
			if( !ret )
				return false;
			state = IMessage.STATE_READ_DATA;		//开始读数据。
			return readDataSection(readBuffer);
		}
		else if( IMessage.STATE_READ_HEAD == state ){
			boolean ret = head.read(readBuffer);
			if( !ret )
				return false;
			state = IMessage.STATE_READ_DATA;		//开始读数据。
			return readDataSection(readBuffer);
		}
		else if( IMessage.STATE_READ_DATA == state )
			return readDataSection(readBuffer);
		else
			return true;
	}
	
	private boolean readDataSection(ByteBuffer buffer) throws MessageParseException{
		if( state == IMessage.STATE_READ_DATA ){
			if( emptyData == data && head.getIntBodylen()>0 ){
				data = ByteBuffer.wrap(new byte[head.getIntBodylen()]);
			}
			if( data.remaining()>= buffer.remaining() )
				data.put(buffer);
			else{
				buffer.get(data.array(),data.position(),data.remaining());
				data.position(data.limit());
			}
			if( data.remaining() == 0 ){
				data.flip();
				rawPacket = ByteBuffer.allocate(data.remaining()+head.getHeadLen());
				rawPacket.put(head.getRawHead()).put(data);
				rawPacket.rewind();
				data.rewind();
				state = IMessage.STATE_READ_DONE;
				ioTime = System.currentTimeMillis();
				onReadFinished();
				return true;
			}
			if( log.isDebugEnabled() )
				log.debug("readDataSection,Length not enough.Gate MessageData Need Length="+data.remaining());
			return false;
		}
		buffer.position(buffer.limit());
		if( log.isInfoEnabled() )
			log.info("readDataSection,非法状态，把数据全部清空。");
		return false;	//非法状态，把数据全部清空
	}

	/**
	 * 当网关消息读取完毕时，触发调用该方法。
	 */
	private void onReadFinished() throws MessageParseException{
		if( type == MessageType.MSG_ZJ || type == MessageType.MSG_GW_10 ){
			//通道上行报文是浙江规约消息。
			if( innerMessage.getIoTime() == 0 )
				innerMessage.setIoTime(System.currentTimeMillis());
			String peer = innerMessage.getPeerAddr();
			if( null == peer )
				peer = this.getPeerAddr();
			if( null == peer )
				peer = "undefine";
				innerMessage.setPeerAddr(peer);
			innerMessage.setSource(this.getSource());
		}
		if( type == MessageType.MSG_GATE ){
			if( head.getCommand() == MessageGate.CMD_GATE_REPLY ){
				//从网关上行到主站前置机的报文
 				innerMessage = MultiProtoRecognizer.recognize(data);
				if( null == innerMessage ){
					log.warn("上行网关报文数据区不能识别:"+HexDump.hexDumpCompact(data));
					return;
				}
				innerMessage.read(data);
				data.rewind();
				if( innerMessage.getIoTime() == 0 ){
					//浙江规约报文未带iotime等属性，是老版本网关格式。
					innerMessage.setIoTime(System.currentTimeMillis());
					String peer = head.getAttributeAsString(GateHead.ATT_DESTADDR); 
					if( peer.length() == 0 )
						if( null != source )
							innerMessage.setPeerAddr(source.toString());
						else
							innerMessage.setPeerAddr("nullSource");
					else
						innerMessage.setPeerAddr(peer);
				}
				String _logicalAddr = head.getAttributeAsString(GateHead.ATT_LOGICALADDR);
				if( null != _logicalAddr && _logicalAddr.length()>0 ){
					innerMessage.setLogicalAddress(_logicalAddr);
				}
				String _peer = head.getAttributeAsString(GateHead.ATT_SRCADDR);
				if( null != _peer && _peer.length()>0 )
					innerMessage.setPeerAddr(_peer);
				String _txfs = head.getAttributeAsString(GateHead.ATT_TXFS);
				if( _txfs.length()!=0 )
					innerMessage.setTxfs(_txfs);
				String serverAddress = head.getAttributeAsString(GateHead.ATT_SERVERADDR);
				if( serverAddress.length()>0 ){
					setServerAddress(serverAddress);
					innerMessage.setServerAddress(serverAddress);
				}
			}
			else if( head.getCommand() == MessageGate.CMD_GATE_REQUEST ){
				//通过网关的下行命令。
				innerMessage = MultiProtoRecognizer.recognize(data);
				if( null == innerMessage ){
					log.warn("下行网关报文数据区不能识别:"+HexDump.hexDumpCompact(data));
					return;
				}
				innerMessage.read(data);
				String _logicalAddr = head.getAttributeAsString(GateHead.ATT_LOGICALADDR);
				if( null != _logicalAddr && _logicalAddr.length()>0 ){
					innerMessage.setLogicalAddress(_logicalAddr);
				}
				String peer = head.getAttributeAsString(GateHead.ATT_DESTADDR);
				if( null != peer && peer.length()>0 )
					innerMessage.setPeerAddr(peer);
				data.rewind();
			}
			if( null != innerMessage )
				innerMessage.setSource(this.getSource());
		}
	}
	
	public void setIoTime(long time) {
		ioTime = time;
	}

	public void setPeerAddr(String peer) {
		peerAddr = peer;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setSource(IChannel src) {
		source = src;
		if( null != this.innerMessage )
			this.innerMessage.setSource(src);
	}

	/**
	 * 把网关消息写入缓冲区。特别注意，在网关消息发送过程中，如果调用write来获取rawPacket，可能错误。
	 */
	public boolean write(ByteBuffer writeBuffer){
		synchronized(this){
			return _write(writeBuffer);
		}
	}
	
	private boolean _write(ByteBuffer writeBuffer) {
		if( IMessage.STATE_SEND_DONE == state )
			return true;
		if( IMessage.STATE_READ_DONE == state )
			state = IMessage.STATE_INVALID;
		if( IMessage.STATE_INVALID == state ){	//先发送报文头
			if( null == data )
				head.setIntBodylen(0);
			else
				head.setIntBodylen(data.remaining());
			state = IMessage.STATE_SEND_HEAD;
			if( ! head.write(writeBuffer) )
				return false;
			state = IMessage.STATE_SEND_DATA;
			return _writeDataSection(writeBuffer);
		}
		else if( IMessage.STATE_SEND_HEAD == state ){
			if( ! head.write(writeBuffer) )
				return false;
			state = IMessage.STATE_SEND_DATA;
			return _writeDataSection(writeBuffer);
		}
		else if( IMessage.STATE_SEND_DATA == state )
			return _writeDataSection(writeBuffer);
		//不应该出现的状态。
		return true;
	}
	
	private boolean _writeDataSection(ByteBuffer buffer){
		if( buffer.remaining()>= data.remaining() ){
			buffer.put(data);
			data.rewind();
			ioTime = System.currentTimeMillis();
			rawPacket = ByteBuffer.allocate(head.getHeadLen()+data.remaining());
			rawPacket.put(head.getRawHead()).put(data);
			data.rewind();	rawPacket.flip();
			state = IMessage.STATE_INVALID;
			return true;
		}
		else{
			//缓冲区可写长度小于数据长度，因此逐个写
			int limit = data.limit();
			data.limit(data.position()+buffer.remaining());
			buffer.put(data);
			data.limit(limit);
			return false;
		}
	}

	public String getTxfs() {
		return txfs;
	}

	public void setTxfs(String txfs) {
		this.txfs = txfs;
	}

	/**
	 * 返回GateMessage所包含的内部消息对象，如网关MessageZj上行消息
	 * @return
	 */
	public IMessage getInnerMessage() {
		return innerMessage;
	}

	/**
	 * 设置要下行的消息，如主站下行命令消息。前置机向网关方向发送时调用。
	 */
	public void setDownInnerMessage(IMessage innerMessage) {
		this.innerMessage = innerMessage;
		this.innerMessage.setSource(this.getSource());
		head.setCommand(MessageGate.CMD_GATE_REQUEST);
		if( null == innerMessage.getPeerAddr() )
			innerMessage.setPeerAddr("");
		head.setAttribute(GateHead.ATT_DESTADDR, innerMessage.getPeerAddr());
		head.setAttribute(GateHead.ATT_LOGICALADDR, innerMessage.getLogicalAddress());	//dlms规约终端地址需要传递，报文不含终端地址
		data = ByteBuffer.wrap(innerMessage.getRawPacket());
		head.setIntBodylen(data.remaining());
		String innerMsg = innerMessage.getRawPacketString();
		rawPacket = ByteBuffer.allocate(head.getHeadLen()+innerMsg.length()/2);
		rawPacket.put(head.getRawHead()).put(HexDump.toByteBuffer(innerMsg));
		rawPacket.flip();
	}

	/**
	 * 设置上行的消息，如终端应答消息或主动上报消息。网关向主站前置机方向发送时调用。
	 */
	public void setUpInnerMessage( IMessage innerMessage) {
		this.innerMessage = innerMessage;
		head.setCommand(MessageGate.CMD_GATE_REPLY);
		//设置终端地址属性
		if( null != innerMessage.getServerAddress() )
			head.setAttribute(GateHead.ATT_SERVERADDR, innerMessage.getServerAddress());	//终端目的地址
		head.setAttribute(GateHead.ATT_LOGICALADDR, innerMessage.getLogicalAddress());	//dlms规约终端地址需要传递，报文不含终端地址
		head.setAttribute(GateHead.ATT_SRCADDR, innerMessage.getPeerAddr()==null?"":innerMessage.getPeerAddr());

		data = ByteBuffer.wrap(innerMessage.getRawPacket());
		head.setIntBodylen(data.remaining());
		String innerMsg = innerMessage.getRawPacketString();
		rawPacket = ByteBuffer.allocate(head.getHeadLen()+innerMsg.length()/2);
		rawPacket.put(head.getRawHead()).put(HexDump.toByteBuffer(innerMsg));
		rawPacket.flip();
	}

	public GateHead getHead() {
		return head;
	}
	
	public ByteBuffer getData() {
		return data;
	}
	
	public void setData(ByteBuffer data){
		this.data = data;
	}
	
	@Override
	public String toString() {
		return getRawPacketString();
	}

	public static MessageGate createHRequest(int numPackets ){
		MessageGate msg = new MessageGate();
		msg.head.setCommand(MessageGate.CMD_GATE_HREQ);
		msg.data = ByteBuffer.allocate(8);
		msg.data.putInt(numPackets).flip();
		return msg;
	}
	
	public static MessageGate createHReply(){
		MessageGate msg = new MessageGate();
		msg.head.setCommand(MessageGate.CMD_GATE_HREPLY);
		return msg;
	}
	
	public static final MessageGate createMoniteProfileRequest(){
		MessageGate msg = new MessageGate();
		msg.head.setCommand(MessageGate.REQ_MONITOR_RELAY_PROFILE);
		return msg;
	}
	
	public static final MessageGate createMoniteProfileReply(String profile){
		MessageGate msg = new MessageGate();
		msg.head.setCommand(MessageGate.REP_MONITOR_RELAY_PROFILE);
		msg.setPriority(IMessage.PRIORITY_VIP);
		if( null != profile && profile.length()>0 ){
			byte[] bts = profile.getBytes();
			msg.data = ByteBuffer.wrap(bts);
		}
		return msg;
	}
	
	/**
	 * 把N个浙江规约报文加载到客户端请求的应答中。
	 * @param carriedMsgs
	 * @return
	 */
	public static MessageGate createHReply(ByteBuffer carriedMsgs ){
		MessageGate msg = new MessageGate();
		msg.head.setCommand(MessageGate.CMD_GATE_HREPLY);
		msg.data = carriedMsgs;
		return msg;
	}

	public Long getCmdId() {
		return this.cmdId;
	}
	
	public void setCmdId(Long id){
		this.cmdId = id;
	}

	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String str ){
		this.status = str;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public boolean isHeartbeat() {
		return head.getCommand() == MessageGate.CMD_GATE_HREPLY || head.getCommand() == MessageGate.CMD_GATE_HREQ;
	}

	public int getRtua(){
		if( null != this.innerMessage )
			return this.innerMessage.getRtua();
		return 0;
	}
	
	public String getLogicalAddress(){
		return null != innerMessage ? innerMessage.getLogicalAddress() : "";
	}
	
	public void setLogicalAddress(String logicAddr){
		
	}
	
	public int length(){
		int len = 0;
		if( getRawPacket().length == 0 ){
			if( null != this.innerMessage )
				len = this.innerMessage.length();
		}
		else
			len = getRawPacket().length;
		return len;
	}

	public boolean isTask() {
		return false;
	}

	public void setTask(boolean isTask) {
	}
	
	public final Object getDataObject(){
		if( data.remaining()>10 ){
			try{
				ByteArrayInputStream in = new ByteArrayInputStream(data.array());
				ObjectInputStream deserializer = new ObjectInputStream(in);
				return deserializer.readObject();
			}catch(Exception exp){
				log.warn("Error decoding gate message to FaalRequest. reason="+exp.getLocalizedMessage());
			}
		}
		return null;
	}
	
	public void setDataObject(final Object obj){
		if( this.head.getCommand() <= 0 )
			this.head.setCommand(MASTER_FE_CMD);
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream serializer = new ObjectOutputStream(out);
			serializer.writeObject(obj);
			this.data = ByteBuffer.wrap(out.toByteArray());
			this.head.setIntBodylen(this.data.remaining());
		}catch(Exception exp){
			throw new RuntimeException("Error encoding request to byte array. reason="+exp.getLocalizedMessage());
		}
	}
}
