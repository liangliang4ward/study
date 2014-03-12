/**
 * 浙江电力负控规约 报文定义。
 * 浙江规约报文至少13个字节。报文头部分至少11个。如果数据不够，则不能读取。
 * 考虑到少数终端存在前导字符串，因此读取时，先把不符合规约部分读取进来。
 */
package cn.hexing.fk.message.zj;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cn.hexing.fk.common.spi.socket.IChannel;
import cn.hexing.fk.exception.MessageParseException;
import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.MessageConst;
import cn.hexing.fk.message.MessageType;
import cn.hexing.fk.utils.HexDump;

/**
 */
public class MessageZj implements IMessage {
	private static final Logger log = Logger.getLogger(MessageZj.class);
	private static final int MAX_LEN = 1024;		//浙江规约，一个帧最长长度
	private static final MessageType type = MessageType.MSG_ZJ;
	private IChannel source;
	public int rtua = 0;
	public MessageZjHead head = new MessageZjHead();
	public ByteBuffer data;
	private StringBuffer rawPacket = new StringBuffer(256);
	private byte[] prefix=null;		//前导字符串
	private int priority = IMessage.PRIORITY_LOW;	//low priority
	private long ioTime;		//完整收到消息或者发送完毕时间
	private String peerAddr;	//对方的IP:PORT地址
	private String serverAddress;	//终端实际连接的外网IP：PORT。用于接收到消息时与终端资产表比对。
	private String txfs="";
	//对象内部属性
	private int state = IMessage.STATE_INVALID;
	private byte _cs = 0;		//发送时候，用于生成校验码
	private static final String charset = "ISO-8859-1";
	
	//by yangjie 为规约解析增加属性
	/**每个消息的唯一key */
	public long key = 0;	//key = rtua | fseq<<32
    /** 组帧命令结果状态 */
    private String status;
    /** 下行数据库命令ID */
    private Long cmdId;
    private int msgCount;

	public MessageType getMessageType() {
		return type;
	}
	
	/**
	 * 浙江规约帧定位
	 * @param readBuffer
	 * @return
	 */
	public int locatePacket(ByteBuffer readBuffer)  throws MessageParseException{
		int pos = -1;
		int pos0 = readBuffer.position();
		boolean located = false;
		int posMark = -1;
		for( pos=pos0; pos+13 <= readBuffer.limit(); pos++){
			if( 0x68 != readBuffer.get(pos) )
				continue;
			if( 0x68 == readBuffer.get(pos+7) ){
				//貌似找到浙江规约帧，需要把刚刚定位的之前部分作为prefix保存起来。
				//即68xxxxxxxxxx68之前部分作为prefix。如果还是定位失败，则68xxxxxxxxxx部分作为丢弃，从下68开始找。
				//第1步：检测数据长度，看看是否非法。
				head.dlen = (short)(readBuffer.get(pos+9) & 0xFF);
				head.dlen |= ( 0xFF & readBuffer.get(pos+10)) << 8;
				if( head.dlen<0 ){
					//肯定错误。丢弃68以及之后6字节。提示continue之后会执行pos++
					pos += 6;
					continue;
				}
				head.c_func = (byte) (readBuffer.get(pos+8) & 0x3f);
				//第2步检测：除非用户自定义数据，否则帧长度不能大于MAX_LEN
				if( head.c_func != 0x0F && head.dlen>= MAX_LEN ){
					pos += 6;
					continue;
				}
				//第3步检测:dlen>数据长度，而数据长度<buffer capacity，继续进行68定位
				//这种情况表明：刚才帧格式错误，或者由于网络原因帧数据没有发送完整。
				//如果是帧格式错误，则需要往后定位，看看现有数据能否组成完整的帧，否则作为数据不完整，继续等待数据
				if( (head.dlen+13)>(readBuffer.limit()-pos0) && readBuffer.limit()<readBuffer.capacity() ){
					posMark = pos;		//可能是数据不完整，需要记录本次定位。
					located = true;
					//往后继续检测。
					pos += 6;
					continue;
				}
				
				//第4步：可能是合法帧直接到这里，也可能是第3步继续检测到这里。
				if( pos>pos0 ){
					prefix = new byte[pos-pos0];
					//先定位到最后一个'|'
					int lastDelimiter = -1;
					for(int i=0;i<prefix.length;i++){
						prefix[i] = readBuffer.get();
						if( '|' == prefix[i] )
							lastDelimiter = i;
					}
					//从新版网关上行浙江规约消息，格式如下
					//iotime=xxxx|peeraddr=xxxxx|txfs=xxx|浙江规约原始帧
					if( prefix.length> 16 ){
						byte[] iot = "iotime=".getBytes();
						boolean isAttr = true;
						for(int j=0; j<iot.length; j++){
							if( iot[j] != prefix[j] ){
								isAttr = false;
								break;
							}
						}
						if( isAttr ){
							String attrs;
							try{
								attrs = new String(prefix,0,lastDelimiter,charset);
							}catch(UnsupportedEncodingException e){
								attrs = new String(prefix);
							}
							StringTokenizer st = new StringTokenizer(attrs,"|");
							String token = st.nextToken().substring(7);
							this.ioTime = Long.parseLong(token);
							this.peerAddr = st.nextToken().substring(9);
							if( st.hasMoreTokens() )
								this.txfs = st.nextToken().substring(5);
							byte[]p = new byte[prefix.length-lastDelimiter-1];
							for(int i=0;i<p.length; i++)
								p[i] = prefix[lastDelimiter+1+i];
							prefix = p;
						}
					}
					rawPacket.append(HexDump.hexDumpCompact(prefix,0, prefix.length));
				}
				located = true;
				break;
			}
		}
		if( !located ){
			//后面的数据可能全部都是非法数据。需要过滤
			for(; pos<readBuffer.limit(); pos++ ){
				if( 0x68 != readBuffer.get(pos) )
					continue;
			}
			int posEnd = Math.max(readBuffer.limit()-13, pos);
			byte[] bts = new byte[posEnd-pos0];
			readBuffer.get(bts);
			String expInfo = "exp:unmatched ZJ Protocol,Message will be discard:"+ HexDump.hexDumpCompact(bts, 0, bts.length);
			if (expInfo.length()>1000)//字符串超长不完全打印
				expInfo=expInfo.substring(0,1000);
			log.warn(expInfo);
			throw new MessageParseException(expInfo);
		}
		//从后面的地方开始读报文，过滤前面内容。
		return posMark<0 ? pos : posMark;
	}
	
	
	/**
	 * 设计上必须考虑到GPRS通讯慢的问题，TCP流可能不是一次读取完整一帧报文。
	 */
	public boolean read(ByteBuffer readBuffer) throws MessageParseException{
		if( state == IMessage.STATE_INVALID && readBuffer.remaining()<13 ){
			if( log.isDebugEnabled() )
				log.debug("长度不足以读取浙江规约报文头，等会儿继续读取。readBuffer.remaining="+readBuffer.remaining());
			return false;		//长度明显不足，等会儿继续读取
		}
		if( state == IMessage.STATE_INVALID ){	//消息头没有读取
			//检测前导字符串，定位浙江规约帧头
			int pos= locatePacket(readBuffer);
			if( readBuffer.limit()-pos < 13 ){
				if( log.isDebugEnabled() )
					log.debug("经过浙江规约报文定位与过滤后，长度不足以读取报文头。readBuffer.remaining="+readBuffer.remaining());
				return false;
			}
			
			//依次读取 报文头、数据体、报文尾
			readBuffer.position(pos);
			byte [] bts = new byte[11];
			_cs = 0;
			for( int i = 0; i< 11 ; i++ ){
				bts[i] = readBuffer.get(i+pos);		//绝对位置读不影响position()
				_cs += bts[i];
			}
			rawPacket.append(HexDump.hexDumpCompact(bts, 0, bts.length));
			
			head.flag1 = readBuffer.get();
			head.rtua_a1 = readBuffer.get();
			head.rtua_a2 = readBuffer.get();
			short iTemp = 0;
			byte c1 = readBuffer.get();
			iTemp = (short) ((0xff & c1) << 8);
			head.rtua_b1b2 = (short)(0xFF & c1);
			c1 = readBuffer.get();
			iTemp |= (short) (0xff & c1);
			head.rtua_b1b2 |= (short)((0xFF & c1 )<<8);
			
			head.rtua = (head.rtua_a1 & 0xFF) << 24;
			head.rtua |= (0xFF & head.rtua_a2) << 16;
			head.rtua |= 0xFFFF & head.rtua_b1b2;
			head.rtua_b1b2 = iTemp;
			rtua = head.rtua;
			
			iTemp = (short) (0xFF & readBuffer.get());
			iTemp |= (0xFF & readBuffer.get()) << 8;
			head.msta = (byte) (0x003f & iTemp);
			head.fseq = (byte) ((0x1fc0 & iTemp) >> 6);
			head.iseq = (byte) ((0xe000 & iTemp) >> 13);
	
			head.flag2 = readBuffer.get();
			
			c1 = readBuffer.get();
			head.c_func = (byte) (c1 & 0x3f);
			//对于终端应答报文，优先级最高。但是，如果是召测任务，则优先级定位为normal。
			if( head.msta != 0 ){
				if( head.c_func != MessageConst.ZJ_FUNC_READ_TASK )
					priority = IMessage.PRIORITY_VIP;
				else
					priority = IMessage.PRIORITY_NORMAL;
			}
			else{
				if( head.c_func == MessageConst.ZJ_FUNC_EXP_ALARM )		//消息高优先级
					priority = IMessage.PRIORITY_HIGH;
				else if( head.c_func == MessageConst.ZJ_FUNC_READ_TASK )
					priority = IMessage.PRIORITY_LOW;
				else
					priority = IMessage.PRIORITY_NORMAL;
			}
			
			head.c_expflag = (byte) ((c1 & 0x40) >> 6);
			head.c_dir = (byte) ((c1 & 0x80) >> 7);
	
			head.dlen = (short)(readBuffer.get() & 0xFF);
			head.dlen |= ( 0xFF & readBuffer.get()) << 8;
			
			state = IMessage.STATE_READ_DATA;
			if( head.dlen+2 >readBuffer.remaining() ){	//需要继续等待数据
				if( log.isDebugEnabled() )
					log.debug("现有缓冲区内容长度[buflen="+readBuffer.remaining()+"]<浙江规约数据区长度["+head.dlen+2+"]");
				return false;
			}
			//消息能够完整读取
			return readDataSection(readBuffer);
		}
		else if( state == IMessage.STATE_READ_DATA || state == IMessage.STATE_READ_TAIL ){
			return readDataSection(readBuffer);
		}
		else{
			//状态非法
			log.error("消息读取状态非法,state="+state);
		}
		return true;
	}
	
	private boolean readDataSection(ByteBuffer readBuffer) throws MessageParseException{
		//第一次读取数据体部分
		if( null == data ){
			data = ByteBuffer.allocate(head.dlen);
		}
		if( state == IMessage.STATE_READ_DATA ){
			while( data.hasRemaining() ){		//报文体没有读取完毕
				if( readBuffer.hasRemaining() )
					data.put(readBuffer.get());
				else
				{//缓冲区没有数据了，但是报文体还没有读取完毕
					return false;
				}
			}
			data.flip();		//ready for read.
			byte[] d = data.array();
			for(int i=0; i<d.length; i++){
				_cs += d[i];
			}
			state = IMessage.STATE_READ_TAIL;
			rawPacket.append(HexDump.hexDumpCompact(data));
		}
		if( readBuffer.remaining()>=2 ){
			head.cs = readBuffer.get();
			rawPacket.append(HexDump.toHex(head.cs));
			head.flag3 = readBuffer.get();
			rawPacket.append(HexDump.toHex(head.flag3));
			if( _cs != head.cs ){
				data = null;
				String packet = rawPacket.toString();
				rawPacket.delete(0, packet.length());
				state = IMessage.STATE_INVALID;		//重新开始读消息的状态。
				throw new MessageParseException("exp校验码不正确:"+packet);
			}
			if( 0x16 != head.flag3 ){
				//帧明显存在错误。最后必须为0x16
				data = null;
				String packet = rawPacket.toString();
				rawPacket.delete(0, packet.length());
				state = IMessage.STATE_INVALID;		//重新开始读消息的状态。
				throw new MessageParseException("exp最后不是16标志符，帧格式错误。packet="+packet);
			}
			state = IMessage.STATE_READ_DONE;
			return true;
		}
		//还有报文尾没有读取。
		return false;
	}

	public boolean write(ByteBuffer writeBuffer) {
		synchronized(rawPacket){
			return _write(writeBuffer);
		}
	}

	/**
	 * 写消息，也必须考虑部分可写的情况。消息可能不能一次完整发送出去。
	 */
	private boolean _write(ByteBuffer writeBuffer) {
		int prefixLen = null==prefix ? 0 : prefix.length ;
		if( state == IMessage.STATE_INVALID && writeBuffer.remaining()<13+prefixLen ){
			log.info("写缓冲长度不足，等会儿继续写。");
			return false;		//长度明显不足，等会儿继续读取
		}
		if( state == IMessage.STATE_INVALID //如果新创建的消息
			|| IMessage.STATE_READ_DONE == state //如果发送从另外通道读取的消息。
			){	//消息头没有写
			if( rawPacket.length() >0 ){
				rawPacket.delete(0, rawPacket.length());
			}
			if( null != prefix ){
				writeBuffer.put(prefix);		//前导字符串
				rawPacket.append(HexDump.hexDumpCompact(prefix, 0, prefix.length));
			}
			//写消息头
			int pos0 = writeBuffer.position();	//消息写入之前的位置
			byte c = 0;
			_cs = 0;
			writeBuffer.put((byte) 0x68);
			_cs += 0x68;
			c = (byte)( (head.rtua>>24) & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			c = (byte)( (head.rtua>>16) & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			//颠倒
			c = (byte)( (head.rtua) & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			c = (byte)( (head.rtua>>8) & 0xFF);
			_cs += c;
			writeBuffer.put(c);

			short iTemp = 0;
			iTemp |= (short) head.msta;
			iTemp |= (short) (head.fseq << 6);
			iTemp |= (short) (head.iseq << 13);
			c = (byte) (iTemp & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			c = (byte) ((iTemp >> 8) & 0xFF );
			_cs += c;
			writeBuffer.put(c);
			
			_cs += 0x68;
			writeBuffer.put((byte) 0x68);
			
			c = head.c_func; // & 0x3f
			c |= 0x40 & (head.c_expflag << 6);
			c |= 0x80 & (head.c_dir << 7);
			_cs += c;
			writeBuffer.put(c);

			if( null == data )
				head.dlen = 0;
			else{
				if( data.position()>0 )
					data.position(0);
				head.dlen = (short)data.remaining();
			}
			iTemp = head.dlen;
			c = (byte) (iTemp & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			c = (byte) ((iTemp >> 8) & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			int pos1 = writeBuffer.position();
			byte[] bts = new byte[pos1-pos0];
			for(int i=0;i<bts.length;i++){
				bts[i] = writeBuffer.get(pos0+i);
			}
			rawPacket.append(HexDump.hexDumpCompact(bts, 0, bts.length));
			//帧头写完毕，下面开始写数据体部分
			state = IMessage.STATE_SEND_DATA;
			return _writeDataSection(writeBuffer);
		}
		if( IMessage.STATE_SEND_DATA == state ){
			return _writeDataSection(writeBuffer);
		}
		return IMessage.STATE_SEND_DONE == state;
	}
	
	private boolean _writeDataSection(ByteBuffer writeBuffer){
		byte c;
		int bufLen = writeBuffer.remaining();
		if( IMessage.STATE_SEND_DATA == state ){
			if( bufLen < head.dlen+2 ){
				log.info("缓冲区太短，不能一次把数据体发送完毕");
				return false;
			}
			if( head.dlen>0 ){
				while( data.hasRemaining() ){
					c = data.get();
					_cs += c;
					writeBuffer.put(c);
				}
				data.rewind();
				rawPacket.append(HexDump.hexDumpCompact(data));
			}
			head.cs = _cs;
			writeBuffer.put(_cs);		rawPacket.append(HexDump.toHex(_cs));
			writeBuffer.put((byte) 0x16); rawPacket.append("16");
			
			state = IMessage.STATE_SEND_DONE;
			return true;
		}
		//报文尾与数据体一起写。对于浙江规约消息，不应出现其它状态
		state = IMessage.STATE_SEND_DONE;
		return true;
	}

	public IChannel getSource() {
		return source;
	}

	public void setSource(IChannel src) {
		source = src;
	}

	public String getRawPacketString() {
		//对于发送出去的消息，在发送之前，rawPacket为空
		synchronized(rawPacket){
			if( IMessage.STATE_INVALID == state || 0 == rawPacket.length() ){
				//还没有write过，rawPacket为空串。
				ByteBuffer buf = ByteBuffer.allocate(3072);
				int _state = state;
				write(buf);
				state = _state;
				return rawPacket.toString();
			}
			else{
				//已经可能部分写，注意保留rawPacket信息
				if( IMessage.STATE_SEND_DONE == state || IMessage.STATE_READ_DONE == state )
					return rawPacket.toString();
				else if( IMessage.STATE_SEND_DATA == state || IMessage.STATE_SEND_HEAD == state 
						|| IMessage.STATE_SEND_TAIL == state ){
					ByteBuffer buf = ByteBuffer.allocate(3072);
					String old = rawPacket.toString();
					rawPacket.delete(0, old.length());
					int _state = state;
					write(buf);
					String raw = rawPacket.toString();
					rawPacket.delete(0, raw.length());
					rawPacket.append(old);
					state = _state;
					return raw;
				}
				//可能读取部分数据，然后捕获异常，则消息不完整，可以打印部分已收数据
				return rawPacket.toString();
			}
		}
	}
	
	/**
	 * 该方法，一般用于收到报文情况下调用。
	 * 浙江规约消息：ioTime=xx|peeraddr=xxx|txfs=xx|（浙江规约原始帧）
	 */
	public byte[] getRawPacket() {
		byte[] ret;
		byte[] raw = HexDump.toByteBuffer(getRawPacketString()).array();
		if( ioTime>0 ){
			StringBuffer sb = new StringBuffer(64);
			sb.append("iotime=").append(ioTime);
			sb.append("|peeraddr=").append(peerAddr).append("|txfs=");
			sb.append(txfs).append("|");
			byte[] att = null;
			try{
				att = sb.toString().getBytes(charset);
			}catch(UnsupportedEncodingException e){
				att = sb.toString().getBytes();
			}
			ret = new byte[att.length+raw.length];
			System.arraycopy(att, 0, ret, 0, att.length);
			System.arraycopy(raw, 0, ret, att.length, raw.length);
		}
		else
			ret = raw;
		return ret;
	}

	public long getIoTime() {
		return ioTime;
	}

	public void setIoTime(long ioTime) {
		this.ioTime = ioTime;
	}

	public String getPeerAddr() {
		return peerAddr;
	}

	public void setPeerAddr(String peerAddr) {
		this.peerAddr = peerAddr;
	}

	/**
	 * 消息序列化。
	 */
	public String toString(){
		return getRawPacketString();
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		if( priority> IMessage.PRIORITY_MAX )
			priority = IMessage.PRIORITY_MAX;
		else if( priority< IMessage.PRIORITY_LOW )
			priority = IMessage.PRIORITY_LOW;
		this.priority = priority;
	}

	public String getTxfs() {
		return txfs;
	}

	public void setTxfs(String txfs) {
		this.txfs = txfs;
	}
	
	public int length(){
		//帧固定长度+数据区长度
		return 13+head.dlen;
	}
	
	public void setPrefix(byte[] pre){
		this.prefix = pre;
	}
	
	/**
	 * 发送失败应答
	 * 
	 */
	public MessageZj createSendFailReply(){
		return createExtErrorReply((byte)0xF2);
	}

	//浙江规约报文下行时，可能发生报文解析异常、无下行通道（发送失败）、超时等错误。
	private MessageZj createExtErrorReply(byte errcode){
		MessageZj msg = new MessageZj();
		msg.setIoTime(System.currentTimeMillis());
		msg.setPeerAddr(this.getPeerAddr());
		msg.setTxfs(this.getTxfs());
		msg.setSource(this.getSource());
		
		MessageZjHead h = msg.head;
		h.c_dir = 1;
		h.c_expflag = 1;
		h.c_func = this.head.c_func;
		h.dlen = 0x01;
		h.fseq = this.head.fseq;
		h.iseq = 0;
		h.msta = this.head.msta;
		h.rtua = this.head.rtua;
		h.rtua_a1 = this.head.rtua_a1;
		h.rtua_a2 = this.head.rtua_a2;
		h.rtua_b1b2 = this.head.rtua_b1b2;
		
		byte [] bts = new byte[1];
		bts[0] = errcode;
		msg.data = ByteBuffer.wrap(bts);
		return msg;

/*		//生成原始消息.
		byte c = 0;
		msg._cs = 0;
		ByteBuffer raw = ByteBuffer.wrap(new byte[14]);
		raw.put((byte) 0x68);
		msg._cs += 0x68;
		c = (byte)( (h.rtua>>24) & 0xFF);
		raw.put(c);
		msg._cs += c;
		c = (byte)( (h.rtua>>16) & 0xFF);
		raw.put(c);
		msg._cs += c;
		//颠倒
		c = (byte)( (h.rtua) & 0xFF);
		raw.put(c);
		msg._cs += c;
		c = (byte)( (h.rtua>>8) & 0xFF);
		raw.put(c);
		msg._cs += c;
		
		short iTemp = 0;
		iTemp |= (short) h.msta;
		iTemp |= (short) (h.fseq << 6);
		iTemp |= (short) (h.iseq << 13);
		c = (byte) (iTemp & 0xFF);
		raw.put(c);
		msg._cs += c;
		c = (byte) (iTemp >> 8);
		raw.put(c);
		msg._cs += c;

		raw.put((byte) 0x68);
		msg._cs += 0x68;

		c = h.c_func; // & 0x3f
		c |= 0x40 & (h.c_expflag << 6);
		c |= 0x80 & (h.c_dir << 7);
		raw.put(c);
		msg._cs += c;
		
		// 模块发送预处理函数已经计算完毕
		iTemp = h.dlen;
		c = (byte) (iTemp & 0xFF);
		raw.put(c);
		msg._cs += c;
		c = (byte) (iTemp >> 8);
		raw.put(c);
		msg._cs += c;
		
		//put data field
		raw.put(bts[0]);
		msg._cs += bts[0];
		// check sum
		h.cs = msg._cs;

		raw.put(h.cs);
		raw.put((byte) 0x16);
		raw.flip();
*/	
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCmdId() {
		return cmdId;
	}

	public void setCmdId(Long cmdId) {
		this.cmdId = cmdId;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	public boolean isExceptionPacket(){
		return head.c_expflag == 0x01;
	}
	
	public byte getErrorCode(){
		if( isExceptionPacket() ){
			data.rewind();
			if( data.remaining()>0 )
				return data.get(0);
		}
		return 0;
	}

	public boolean isHeartbeat() {
		return head.c_func == MessageConst.ZJ_FUNC_HEART;
	}
	
	public boolean isLogin(){
		//浙江登陆帧  控制码21.广规登陆帧 控制码A1
		return head.c_func == MessageConst.ZJ_FUNC_LOGIN || head.c_func==0xA1;
	}
	
	public int getRtua(){
		return head.rtua;
	}
	
	public String getLogicalAddress(){
		return HexDump.toHex(head.rtua);
	}
	
	public void setLogicalAddress(String logicAddr){
		if(null != logicAddr && logicAddr.length()>0 ){
			head.rtua = (int)Long.parseLong(logicAddr, 16);
		}
	}

	public boolean isTask() {
		return head.c_func == MessageConst.ZJ_FUNC_READ_TASK;
	}

	public void setTask(boolean isTask) {
	}
}
