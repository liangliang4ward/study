package cn.hexing.fk.message.gw;

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
 * 国网规约帧报文对象.帧格式如下：
 * 68h+ 2个L(每L 2字节) + 68H + C + A1 + A2 + A3 + data + CS + 16H
 *
 */
public class MessageGw implements IMessage {
	private static final Logger log = Logger.getLogger(MessageGw.class);
	private static final MessageType type = MessageType.MSG_GW_10;
	private IChannel source;
	public ByteBuffer data = null;
	private ByteBuffer aux = null;
	private StringBuffer rawPacket = new StringBuffer(256);
	private byte[] prefix;		//前导字符串
	private int priority = IMessage.PRIORITY_LOW;	//low priority
	private long ioTime;		//完整收到消息或者发送完毕时间
	private String peerAddr;	//对方的IP:PORT地址
	private String serverAddress;	//终端实际连接的外网IP：PORT。用于接收到消息时与终端资产表比对。
	private String txfs="";
	public MessageGwHead head = new MessageGwHead();
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
    boolean isTask = false;

    public void setSendable(){
    	 state = IMessage.STATE_INVALID;
    }
	/**
	 * 国网规约帧定位
	 * @param readBuffer
	 * @return
	 */
	public int locatePacket(ByteBuffer readBuffer)  throws MessageParseException{
		int pos = -1;
		int pos0 = readBuffer.position();
		boolean located = false;
		for( pos=pos0; pos+16 <= readBuffer.limit(); pos++){
			if( 0x68 != readBuffer.get(pos) )
				continue;
			if( 0x68 == readBuffer.get(pos+5) ){
				//貌似找到国网规约帧，需要把刚刚定位的之前部分作为prefix保存起来。
				//即68xxxxxxxxxx68之前部分作为prefix。如果还是定位失败，则68xxxxxxxxxx部分作为丢弃，从下68开始找。

				//第1步：检测数据长度，看看是否非法。  每帧接收的字符数为用户数据长度L1+8
				byte c1=0;
				short len1=0,len2=0;
				c1 = readBuffer.get(pos+1);
				len1 = (short)(c1 & 0xFF);
				c1 = readBuffer.get(pos+2);
				len1 |= (c1 & 0xFF) << 8;
				//short len1 = readBuffer.getShort(pos+1);
				head.decodeL(len1);
				//short len2 = readBuffer.getShort(pos+3);
				c1 = readBuffer.get(pos+3);
				len2 |= (c1 & 0xFF);
				c1 = readBuffer.get(pos+4);
				len2 |= (c1 & 0xFF) << 8;
				if( len1 != len2 || head.proto_flag != 2 || head.dlen-8<0 ){
					//肯定错误。丢弃68之后4字节。提示continue之后会执行pos++
					pos += 4;
					continue;
				}
				head.decodeC( readBuffer.get(pos+6) );
				
				//第2步：检测帧前缀携带的信息
				if( pos>pos0 ){
					prefix = new byte[pos-pos0];
					//先定位到最后一个'|'
					int lastDelimiter = -1;
					for(int i=0;i<prefix.length;i++){
						prefix[i] = readBuffer.get();
						if( '|' == prefix[i] )
							lastDelimiter = i;
					}
					//从新版网关上行国网规约消息，格式如下
					//iotime=xxxx|peeraddr=xxxxx|txfs=xxx|国网规约原始帧
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
			byte[] bts = new byte[pos-pos0];
			readBuffer.get(bts);
			String expInfo = "exp:unmatched GW Protocol,Message will be discard:"+ HexDump.hexDumpCompact(bts, 0, bts.length);//String expInfo = "exp不符合浙江规约，报文被丢弃："+ HexDump.hexDumpCompact(bts, 0, bts.length);
			if (expInfo.length()>1000)//字符串超长不完全打印
				expInfo=expInfo.substring(0,1000);
			log.warn(expInfo);
			throw new MessageParseException(expInfo);
		}
		//从后面的地方开始读报文，过滤前面内容。
		return pos;
	}

	public boolean read(ByteBuffer readBuffer) throws MessageParseException {
		if( state == IMessage.STATE_INVALID && readBuffer.remaining()<16 ){
			if( log.isDebugEnabled() )
				log.debug("长度不足以读取国网规约报文头，继续读取。readBuffer.remaining="+readBuffer.remaining());
			return false;		//长度明显不足，等会儿继续读取
		}
		if( state == IMessage.STATE_INVALID ){	//消息头没有读取
			//检测前导字符串，定位国网规约帧头
			int pos= locatePacket(readBuffer);
			if( readBuffer.limit()-pos < 16 ){
				if( log.isDebugEnabled() )
					log.debug("经过国网规约定位与过滤后，长度不足以读取报文头。readBuffer.remaining="+readBuffer.remaining());
				return false;
			}
			//依次读取 报文头、数据体、报文尾
			readBuffer.position(pos);
			byte [] bts = new byte[14];
			_cs = 0;
			for( int i = 0; i< 14 ; i++ ){
				bts[i] = readBuffer.get(i+pos);		//绝对位置读不影响position()
				//用户数据区所有字节的八位位组算术和. 用户数据区包括控制域、地址域、链路用户数据（应用层）三部分
				if( i> 5 )  // i>=6
					_cs += bts[i];
			}
			rawPacket.append(HexDump.hexDumpCompact(bts, 0, bts.length));
			
			head.flag1 = readBuffer.get();
			readBuffer.getShort();readBuffer.getShort();	//读取2个L
			head.flag2 = readBuffer.get(); //第二个68
			readBuffer.get(); //控制域C. 在locate函数中已经读取
			byte c1 = readBuffer.get();
			head.rtua |= (c1 & 0xFF) << 16;
			c1 = readBuffer.get();
			head.rtua |= (c1 & 0xFF) << 24;
			c1 = readBuffer.get();
			head.rtua |= (c1 & 0xFF);
			c1 = readBuffer.get();
			head.rtua |= (c1 & 0xFF) << 8;
			head.decodeA3(readBuffer.get());
			//应用层的2个字节，代码层面把它规到报文头，因为所有帧都是固定的。
			head.app_func = readBuffer.get();
			head.decodeSEQ(readBuffer.get());
			//set priority
			if( head.c_dir == 0 ){
				//down
				if( afn() == 0x04 || afn() == 0x05 || afn() == 0x06 )
					this.priority = IMessage.PRIORITY_VIP;
				else if( afn() == 0x0d )
					this.priority = IMessage.PRIORITY_LOW;
				else
					this.priority = IMessage.PRIORITY_NORMAL;
			}
			else{
				//up
				if( head.c_prm == 1 ){
					if( afn() == 0x0E )
						this.priority = IMessage.PRIORITY_HIGH;
					else
						this.priority = IMessage.PRIORITY_LOW;
				}
				else{
					if( afn()== 0x0d )
						this.priority = IMessage.PRIORITY_NORMAL;
					else
						this.priority = IMessage.PRIORITY_VIP;
				}
			}
			//实际应用数据区,按照业务变化。
			state = IMessage.STATE_READ_DATA;
			//head.dlen长度= C + A + AFN + SEQ + data + aux
			//已经读取8字节
			if( head.dlen-8 > readBuffer.remaining() ){	//需要继续等待数据
				if( log.isDebugEnabled() )
					log.debug("现有缓冲区内容长度[buflen="+readBuffer.remaining()+"]<国网规约数据区长度["+(head.dlen+2)+"]");
				return false;
			}
			//能够完整读取数据区
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
		if( null == data )
			data = ByteBuffer.allocate(head.dlen-8);
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
		if( state == IMessage.STATE_READ_TAIL ){
			if( readBuffer.remaining()>=2 ){
				byte cs0 = readBuffer.get();
				rawPacket.append(HexDump.toHex(cs0));
				byte flag16 = readBuffer.get();
				rawPacket.append(HexDump.toHex(flag16));
				if( cs0 != _cs ){
					//校验码不一致
					data = null;
					String packet = rawPacket.toString();
					rawPacket.delete(0, packet.length());
					state = IMessage.STATE_INVALID;		//重新开始读消息的状态。
					throw new MessageParseException("exp校验码不正确:"+packet);
				}
				if( 0x16 != flag16 ){
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
		if( head.dlen == 0 )
			head.dlen = (short)(8 + (null != data ? data.remaining() : 0 ) + ( null != aux ? aux.remaining() : 0));
		if( (state == IMessage.STATE_INVALID || IMessage.STATE_READ_DONE == state)
				&& writeBuffer.remaining()<16+prefixLen ){
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
			short stemp = head.encodeL();
			writeBuffer.putShort(stemp); writeBuffer.putShort(stemp);
			writeBuffer.put((byte) 0x68);
			c = head.encodeC();
			_cs += c;
			writeBuffer.put(c);
			
			//输出A地址域
			c = (byte)( (head.rtua>>16) & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			c = (byte)( (head.rtua>>24) & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			c = (byte)( (head.rtua) & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			c = (byte)( (head.rtua>>8) & 0xFF);
			_cs += c;
			writeBuffer.put(c);
			c = head.encodeA3();
			_cs += c;
			writeBuffer.put(c);
			
			//发送应用层的AFN（业务功能码）+ SEQ
			c = head.app_func;
			_cs += c;
			writeBuffer.put(c);
			c = head.encodeSEQ();
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
		else if( IMessage.STATE_SEND_DATA == state || IMessage.STATE_SEND_TAIL == state ){
			return _writeDataSection(writeBuffer);
		}
		else return IMessage.STATE_SEND_DONE == state;
	}
	
	private boolean _writeDataSection(ByteBuffer writeBuffer){
		byte c;
		if( ! writeBuffer.hasRemaining() ){
			log.info("发送缓冲区长度为0，发送调用失败");
			return false;
		}
		if( IMessage.STATE_SEND_DATA == state ){
			//发送data区域
			if( null != data && data.hasRemaining() ){
				while( data.hasRemaining() && writeBuffer.hasRemaining() ){
					c = data.get();
					_cs += c;
					writeBuffer.put(c);
				}
				if( !data.hasRemaining() ){
					data.rewind();
					rawPacket.append(HexDump.hexDumpCompact(data));
					data.position(data.limit());
				}
			}
			if( null != aux && aux.hasRemaining() ){
				while( aux.hasRemaining() && writeBuffer.hasRemaining() ){
					c = aux.get();
					_cs += c;
					writeBuffer.put(c);
				}
				if( !aux.hasRemaining() ){
					aux.rewind();
					rawPacket.append(HexDump.hexDumpCompact(aux));
					aux.position(aux.limit());
				}
			}
			//检测是否全部发送完毕。如果是部分发送完毕，则返回false
			//如果有data，而data还有数据，或者有aux，而aux还有数据，那么没有发送完毕
			boolean notFinished = ( null!=data && data.hasRemaining() ) || (null!=aux && aux.hasRemaining() );
			if( notFinished ){
				if( log.isDebugEnabled() )
					log.debug("缓冲区太短，不能一次把数据体发送完毕");
				return false;
			}
			//数据区发送完毕，需要恢复data以及aux
			if( null != data )
				data.rewind();
			if( null != aux )
				aux.rewind();
			state = IMessage.STATE_SEND_TAIL;
		}
		if( IMessage.STATE_SEND_TAIL == state ){
			//发送报文尾
			if( writeBuffer.remaining()>=2 ){
				writeBuffer.put(_cs);		rawPacket.append(HexDump.toHex(_cs));
				writeBuffer.put((byte) 0x16); rawPacket.append("16");
				state = IMessage.STATE_SEND_DONE;
				return true;
			}
		}
		return false;
	}

    public void setCmdId(Long cmdId) {
		this.cmdId = cmdId;
	}

	public Long getCmdId() {
		return cmdId;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public long getIoTime() {
		return ioTime;
	}

	public MessageType getMessageType() {
		return type;
	}

	public String getPeerAddr() {
		return peerAddr;
	}

	public int getPriority() {
		return priority;
	}

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

	public String getRawPacketString() {
		if( rawPacket.length()<13 ){
			ByteBuffer buf = ByteBuffer.allocate(1024*5);
			int _state = state;
			write(buf);
			state = _state;
		}
		return rawPacket.toString();
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public IChannel getSource() {
		return source;
	}

	public String getStatus() {
		return status;
	}

	public String getTxfs() {
		return txfs;
	}

	public boolean isHeartbeat() {
		return head.app_func == MessageConst.GW_FUNC_HEART;
	}
	public int getFseq(){
		return head.seq_pseq;
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

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setSource(IChannel src) {
		source = src;
	}

	public void setTxfs(String fs) {
		txfs = fs;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public ByteBuffer getAux() {
		return aux;
	}

	/**
	 * 设置附加信息域。
	 * @param aux ： 完整附加信息域
	 * @param hasTp: 指示是否包含时间标签
	 */
	public void setAux(ByteBuffer aux, boolean hasTp) {
		this.aux = aux;
		this.head.seq_tpv = (byte)(hasTp ? 1 : 0);
	}
	
	public void setSEQ( byte seq ){
		head.seq_pseq = seq;
		if (aux!=null){//如果存在时间标签，则其中的帧计数器需要同步赋值
			aux.rewind();		
			String sAux=HexDump.hexDumpCompact(aux);
			if (sAux.length()==12&&seq<=15){//国网帧序号0~15
				sAux="0"+Integer.parseInt(""+seq,16)+sAux.substring(2);
				setAux(HexDump.toByteBuffer(sAux), true);
			}
		}
		 
	}
	
	public boolean isNeedConfirm(){
		return this.head.seq_con == 0x01;
	}
	/**
	 * 指定报文是否需要确认帧。
	 * @param needConfirm
	 */
	public void needConfirm( boolean needConfirm ){
		this.head.seq_con = (byte)( needConfirm ? 1 : 0);
	}

	public byte afn(){
		return getAFN();
	}
	public void afn(byte afn){
		setAFN(afn);
	}
	public byte getAFN(){
		return head.app_func;
	}
	
	public void setAFN( byte afn ){
		needConfirm(false);
		this.head.app_func = afn;
		byte frameFn = 0;
		switch( afn ){
		case MessageConst.GW_FUNC_REPLY:
			break;
		case MessageConst.GW_FUNC_RESET:
			frameFn = MessageConst.GW_FN_RESET;
			needConfirm(true);
			break;
		case MessageConst.GW_FUNC_HEART:	//心跳, 链路接口检测
			frameFn = MessageConst.GW_FN_HEART;
			break;
		case MessageConst.GW_FUNC_SETPARAM: //设置参数
		case MessageConst.GW_FUNC_CONTROL: //控制命令		
			frameFn = MessageConst.GW_FN_LEVEL1;
			needConfirm(true);
			break;		
		case MessageConst.GW_FUNC_AUTH: //身份认证及密钥协商
		case MessageConst.GW_FUNC_RELAY_CTRL: //中继站命令
		case MessageConst.GW_FUNC_REQ_CASCADE_UP: //请求被级联终端主动上报
		case MessageConst.GW_FUNC_REQ_RTU_CFG: //请求终端配置
		case MessageConst.GW_FUNC_GETPARAM: //查询参数
		case MessageConst.GW_FUNC_GET_TASK: //请求任务数据
		case MessageConst.GW_FUNC_GET_DATA1: //请求1类数据（实时数据）
		case MessageConst.GW_FUNC_GET_DATA2: //请求2类数据（历史数据）
		case MessageConst.GW_FUNC_GET_DATA3: //请求3类数据（事件数据）
		case MessageConst.GW_FUNC_RELAY_READ: //数据转发(中继抄表)
		case MessageConst.GW_FUNC_FILE: //文件传输		
			frameFn = MessageConst.GW_FN_LEVEL2;
			break;			
		}
		this.head.c_func = frameFn;
	}
	
	/**
	 * 是否是登陆帧
	 * @return
	 */
	public boolean isLogin(){
		if(afn()!=MessageConst.GW_FUNC_HEART) return false;
		
		if(data.array().length!=4) return false;
		
		return data.array()[0]==0x00 && data.array()[1]==0x00&&data.array()[2]==0x01&&data.array()[3]==0x00;
	}
	
	public MessageGw createConfirm(){
		MessageGw con = null;
		if( head.seq_con == 1 ){
			con = new MessageGw();
			byte cfunc = 0;
			switch( head.c_func ){
			case 1:
				cfunc = 0;
				break;
			case 9:
				cfunc = 11;
				break;
			default:
				cfunc = 8;
			}
			con.head.c_func = cfunc;
			con.head.c_prm = 0;
			con.head.dlen = 12;
			con.head.rtua = head.rtua;
			con.head.seq_con = 0;
			con.head.seq_pseq = head.seq_pseq;
			byte[] repData = { 0x00, 0x00, 0x01, 0x00 };
			con.data = ByteBuffer.wrap(repData);
		}
		return con;
	}
	
	public int length(){
		return head.dlen + 8;
	}

	public boolean isTask() {
		return isTask;
	}

	public void setTask(boolean isTask) {
		this.isTask = isTask;
	}

	@Override
	public String toString() {
		return this.getRawPacketString();
	}
}
