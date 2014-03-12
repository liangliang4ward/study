package cn.hexing.fk.message.gate;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.apache.log4j.Logger;

import cn.hexing.fk.exception.MessageParseException;
import cn.hexing.fk.utils.HexDump;

public class GateHead {
	private static final Logger log = Logger.getLogger(GateHead.class);
	public static final byte ATT_VERSION = 0x01; // 协议版本
	public static final byte ATT_ENCRIPT = 0x02; // 加密标志
	public static final byte ATT_DESTID = 0x03; // 目的模块ID
	public static final byte ATT_PRIVATE = 0x04; // 私有消息，不能广播
	public static final byte ATT_CPYATT = 0x05; // 应答消息必须包含请求消息的属性
	public static final byte ATT_LOGTYPE = 0X06; // 见monitor消息定义
	public static final byte ATT_FILEPATH = 0x07; // 相对路径:"log/my.xml"
	public static final byte ATT_SRCADDR = 0x08; // 终端来源IP地址
	public static final byte ATT_DESTADDR = 0x09; // 网关目的IP地址
	public static final byte ATT_TXFS = 0x0a; // 通讯方式
	public static final byte ATT_TXSJ = 0x0b; // 通讯时间
	public static final byte ATT_MSGTYPE = 0x0c; // 报文体所属消息类型，见IMessage定义
	public static final byte ATT_MSGSEQ = 0x10; // 消息序号
	public static final byte ATT_MSGSRC = 0x13; // 消息来源
	public static final byte ATT_LOGICALADDR = 0x14; // 消息携带终端地址
	public static final byte ATT_SERVERADDR	= 0x11;	//网关的实际服务器地址(ip:port)
	public static final byte ATT_DOWN_CHANNEL = 0x12;	//指定下行方式：0 默认, 1 GPRS 2 短信
	public static final byte ATT_SIM_NUM=0x15;
	
	public static final byte CHANNEL_TYPE_DEFAULT = 0;
	public static final byte CHANNEL_TYPE_GPRS = 1;
	public static final byte CHANNEL_TYPE_UMS = 2;

	private final String flag = "JBBS";
	private byte src = 0x02;
	private short cmd;
	private int intBodyLen;
	private short headAttrLen;
	private HashMap<Byte, HeadAttribute> hmAttr = new HashMap<Byte, HeadAttribute>();
	private byte[] rawHead = null;		//网关消息定位之前的垃圾数据。

	public GateHead() {
		cmd = 0;
		intBodyLen = 0;
		headAttrLen = 0;
	}

	public boolean isValid() {
		return cmd != 0x00;
	}

	/**
	 * 从缓冲区读取数据。
	 * @param buffer
	 * @return true，如果完整读取报文头，false otherwise
	 */
	public boolean read(ByteBuffer buffer) throws MessageParseException{
		if( buffer.remaining()<13 )
			return false;
		//预先读取Head，如果不能完整读取，则返回false，并且buffer恢复未读状态
		buffer.mark();
		int posOld = buffer.position();		//报文头开始读位置
		int index = 0;
		// skip 4 bytes flag
		for (index = 0; index < 4; index++)
			buffer.get();
		src = buffer.get();
		cmd = buffer.getShort();
		intBodyLen = buffer.getInt();
		headAttrLen = buffer.getShort();
		if( headAttrLen<0 || headAttrLen>4096 || intBodyLen<0 ){
			String expStr = "报文格式错误。headAttrLen="+headAttrLen+",intBodyLen="+intBodyLen; 
			log.warn(expStr);
			buffer.position(buffer.limit());
			throw new MessageParseException(expStr);
//			return false;		//必须抛出异常。
		}
		//读取完整报文头
		if( headAttrLen > buffer.remaining() ){
			buffer.reset();
			String expStr = "BufferArea's Length not enough for Gate Head"+HexDump.hexDump(buffer)+" headAttrLen="+headAttrLen; 
			log.info(expStr);
			return false;
			//buffer.position(buffer.limit());
			//throw new MessageParseException(expStr);
		}
		index = 0;
		while( index< headAttrLen ){
			byte id = buffer.get();
			short attlen = buffer.getShort();
			setAttribute(id, buffer, attlen);
			index += attlen + 3;
		}
		int headLen = 13+index;
		rawHead = new byte[headLen];
		buffer.position(posOld);
		buffer.get(rawHead);
		return true;
	}

	public int getHeadLen() {
		if (headAttrLen == 0)
			headAttrLen = getHeadAttrLen();
		return 13 + headAttrLen;
	}

	public int getTotalLen() {
		return getHeadLen() + intBodyLen;
	}

	/**
	 * 报文头对象写入缓冲区。
	 * @param buffer
	 * @return true：报文头完整写入缓冲区，false otherwise
	 */
	public boolean write(ByteBuffer buffer) {
		short attrLen = getHeadAttrLen();
		if( buffer.remaining()< 13+ attrLen )
			return false;
		int posOld = buffer.position();
		buffer.put(flag.getBytes());
		buffer.put(src);
		buffer.putShort(cmd);
		buffer.putInt(intBodyLen);
		buffer.putShort(attrLen);
		for(HeadAttribute attr: hmAttr.values() ){
			buffer.put(attr.id).putShort(attr.len).put(attr.attr);
		}
		rawHead = new byte[this.getHeadLen()];
		buffer.position(posOld);
		int limitOld = buffer.limit();
		buffer.limit(posOld+rawHead.length);
		buffer.get(rawHead,0,rawHead.length);
		buffer.limit(limitOld);
		return true;
	}

	public void setCommand(short command) {
		cmd = command;
	}

	public void setCommand(int command) {
		cmd = (short) command;
	}

	public short getCommand() {
		return cmd;
	}

	public int getIntBodylen() {
		return intBodyLen;
	}

	public void setIntBodylen(int bodylen) {
		this.intBodyLen = bodylen;
	}

	public short getHeadAttrLen() {
		short hlen = 0;
		for(HeadAttribute attr: hmAttr.values() ){
			hlen += attr.len + 3;
		}
		return hlen;
	}

	public void setAttribute(byte id, ByteBuffer buf, short len) {
		assert buf.remaining() >= len;

		HeadAttribute attr = new HeadAttribute();
		attr.id = id;
		attr.len = len;
		attr.attr = new byte[len];
		for(int i=0;i<len;i++)
			attr.attr[i] = buf.get();
		hmAttr.put(id, attr);
	}

	public void setAttribute(byte id, byte[] attr, int pos, int len) {
		assert null != attr;
		assert len < 1024 && len > 0 && (pos + len) < attr.length;

		HeadAttribute attrObj = new HeadAttribute();
		attrObj.id = id;
		attrObj.len = (short) len;
		attrObj.attr = new byte[len];
		System.arraycopy(attr, pos, attrObj.attr, 0, len);
		hmAttr.put(new Byte(id), attrObj);
	}

	public void setAttribute(byte id, byte[] attr) {
		assert null != attr;
		setAttribute(id, attr, 0, attr.length);
	}
	//属性设置对于同一msg只能同时设置，不能分开，否则会导致报文头内容描述异常
	public void setAttribute(byte id, int data) {
		byte[] val = new byte[4];
		ByteBuffer bf = ByteBuffer.wrap(val);
		bf.putInt(data);
		bf.flip();
		setAttribute(id, bf.array());
	}

	public void setAttribute(byte id, long data) {
		byte[] val = new byte[8];
		ByteBuffer bf = ByteBuffer.wrap(val);
		bf.putLong(data);
		bf.flip();
		setAttribute(id, bf.array());
	}

	public void setAttribute(byte id, short data) {
		byte[] val = new byte[2];
		ByteBuffer bf = ByteBuffer.wrap(val);
		bf.putShort(data);
		bf.flip();
		setAttribute(id, bf.array());
	}

	public void setAttribute(byte id, byte data) {
		byte[] val = new byte[1];
		ByteBuffer bf = ByteBuffer.wrap(val);
		bf.put(data);
		bf.flip();
		setAttribute(id, bf.array());
	}

	public void setAttribute(byte id, String data) {
		byte[] val = data.getBytes();
		setAttribute(id, val);
	}

	public byte[] getAttribute(byte id) {
		HeadAttribute attr = (HeadAttribute) hmAttr.get(new Byte(id));
		if (null == attr)
			return null;
		return attr.attr;
	}

	public int getAttributeAsInt(byte id) {
		byte[] val = getAttribute(id);
		if (null == val || val.length != 4)
			return 0;
		ByteBuffer bf = ByteBuffer.wrap(val);
		return bf.getInt();
	}

	public long getAttributeAsLong(byte id) {
		byte[] val = getAttribute(id);
		if (null == val || val.length != 8)
			return 0;
		ByteBuffer bf = ByteBuffer.wrap(val);
		return bf.getLong();
	}

	public short getAttributeAsShort(byte id) {
		byte[] val = getAttribute(id);
		if (null == val || val.length != 2)
			return 0;
		ByteBuffer bf = ByteBuffer.wrap(val);
		return bf.getShort();
	}

	public String getAttributeAsString(byte id) {
		byte[] val = getAttribute(id);
		if (null == val)
			return "";
		try {
			return new String(val);
		} catch (Exception exp) {
			return HexDump.hexDumpCompact(val, 0, val.length);
		}
	}

	public byte getAttributeAsByte(byte id) {
		byte[] val = getAttribute(id);
		if (null == val)
			return 0x00;
		return val[0];
	}

	public byte[] getRawHead(){
		if( null == rawHead ){
			rawHead = new byte[13];
			ByteBuffer headBuffer = ByteBuffer.wrap(rawHead);
			this.write(headBuffer);
		}
		return rawHead;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("|source=").append(HexDump.toHex(this.src));
		sb.append(",cmd=").append(HexDump.toHex(this.cmd));
		sb.append(",bodylen=").append(this.getIntBodylen());
		sb.append(",attrlen=").append(this.getHeadAttrLen());
		if (hmAttr.size() > 0) {
			sb.append(",attributes:");
			for(HeadAttribute attr: hmAttr.values() ){
				sb.append(attr);
			}
		}
		return sb.toString();
	}

	class HeadAttribute {
		public byte id;
		public short len;
		public byte[] attr;

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(";att.id=").append(HexDump.toHex(id));
			sb.append(",att.len=").append(len);
			String str = "";
			try {
				str = new String(attr);
			} catch (Exception exp) {
				str = HexDump.hexDumpCompact(attr, 0, attr.length);
			}
			sb.append(",att.str=").append(str);
			return sb.toString();
		}
	}
}
