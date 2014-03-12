/**
 * 字节类型消息。一般需要socket连接的session机制管理报文业务内容。
 * 字节型消息用于在客户端与服务器之间传递未预先定义报文格式的内容。
 */
package cn.hexing.fk.message.msgbytes;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.hexing.fk.exception.MessageParseException;
import cn.hexing.fk.message.MessageBase;
import cn.hexing.fk.message.MessageType;
import cn.hexing.fk.utils.HexDump;

/**
 *
 */
public class MessageBytes extends MessageBase{
	private static final Logger log = Logger.getLogger(MessageBytes.class);
	public static final byte[] EMPTY_DATA = new byte[0];
	
	public byte[] data = EMPTY_DATA;
	public int offset = 0;			//下次开始写的位置，以便支持内容长度超过缓冲区长度情况发送。
	
	public byte[] getRawPacket() {
		return data;
	}

	public String getRawPacketString() {
		return HexDump.hexDumpCompact(data, 0, data.length);
	}

	public int length() {
		return data.length;
	}

	public boolean read(ByteBuffer readBuffer) throws MessageParseException {
		int len = readBuffer.remaining();
		if( len >0 ){
			data = new byte[len];
			readBuffer.get(data);
		}
		return true;
	}
	
	public void setData( byte[] msgData ){
		offset = 0;
		if( null != msgData )
			data = msgData;
	}

	public boolean write(ByteBuffer writeBuffer) {
		if( data.length<= offset )	//空消息或者已经发送完的消息，提示消息发送完毕。
		{
			if( offset <=0 )	//Empty message
				return true;
			else{
				offset = 0;
				log.warn("MessageBytes resend.");
			}
		}
		while( offset < data.length ){
			if( writeBuffer.hasRemaining() )
				writeBuffer.put(data[offset++]);
			else
				return false;
		}
		return true;
	}

	@Override
	public String toString(){
		return this.getRawPacketString();
	}
	@Override
	public MessageType getMessageType() {
		return MessageType.MSG_BYTES;
	}

}
