package com.hx.ansi.message;

import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cn.hexing.fk.exception.MessageParseException;
import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.MessageBase;
import cn.hexing.fk.message.MessageType;
import cn.hexing.fk.utils.HexDump;


/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-14 下午03:14:03
 * @version 1.0 
 */

public class AnsiMessage  extends MessageBase {
	private static final Logger log = Logger.getLogger(AnsiMessage.class);
	private static final int MAX_APDU_LEN = 1024*10;
	protected static enum IoState { INIT,READ_BODY,READ_DONE, WRITE_BODY,WRITE_DONE };
	private String logicalAddress = "";
	private int direction = IMessage.DIRECTION_UP;
	private String txfs="";
	private int length = 0;		//The length of protocol-body. 2bytes, as unsigned-short.
	private static final MessageType type=MessageType.MSG_ANSI;
	//APDU( protocol body)
	private ByteBuffer apdu = null;
	private IoState ioState = IoState.INIT;
	private int version = 0x60;
	
	protected  ByteBuffer locating(ByteBuffer buf) throws MessageParseException{
		int pos = buf.position();
		int ansiPos = -1;
		int iotimePos = -1;
		for(; pos<buf.limit()-2; pos++ ){
			if( buf.get(pos) == 0x60 &&  buf.get(pos+2) == (byte)0xA2  ){
				ansiPos = pos;
				break;
			}else if( buf.get(pos) == 0x60 &&  buf.get(pos+2) == (byte)0xA4  ){
				ansiPos = pos;
				break;
			}else if( buf.get(pos) == 0x60 &&  buf.get(pos+2) == (byte)0xA8  ){
				ansiPos = pos;
				break;
			}else if( buf.get(pos) == 0x60 &&  buf.get(pos+2) == (byte)0xBE  ){
				ansiPos = pos;
				break;
			}
			else if( iotimePos == -1 ){
				if( buf.get(pos) == 'i' ){
					iotimePos = pos;
				}
			}
		}
		if( ansiPos >= 0 ){
			if( iotimePos>=0 ){
				if( ansiPos-iotimePos>16 ){
					byte[] leadBytes = new byte[ansiPos-iotimePos];
					buf.position(iotimePos);
					buf.get(leadBytes);
					try{
						String leadString = new String(leadBytes);
						StringTokenizer st = new StringTokenizer(leadString,"|");
						String part = st.nextToken();
						String name = part.substring(0, 7);
						String value = part.substring(7);
						if( name.equals("iotime=")){
							ioTime = Long.parseLong(value);
						}
						part = st.nextToken();
						name = part.substring(0, 9);
						value = part.substring(9);
						if( name.equals("peeraddr=")){
							peerAddr = value;
						}
						part = st.nextToken();
						name = part.substring(0, 5);
						value = part.substring(5);
						if( name.equals("txfs="))
							txfs = value;
					}catch(Exception e){
						log.warn("Parse lead-string exp.",e);
					}
				}
				else{
					if( log.isDebugEnabled() )
						log.debug("Unknown leadString, buf="+HexDump.hexDump(buf));
				}
			}
			buf.position(ansiPos);
			return buf;
		}
		else{
			//Not located. detect to see if junk data
			if( pos - buf.position() > 60 ){
				int minLen = Math.min(pos-buf.position(), 200);
				byte[] junk = new byte[minLen];
				buf.get(junk);
				String expInfo = "Not DLMS protocol, discard ..."+ HexDump.hexDumpCompact(junk, 0, junk.length);
				log.warn(expInfo);
				throw new MessageParseException(expInfo);
			}
		}
		return buf;
	}
	
	public boolean read(ByteBuffer readBuffer) throws MessageParseException {
		if( ioState == IoState.INIT ) {
			readBuffer=locating(readBuffer); //locating message....
			if(version==(readBuffer.get(0)&0xFF));
			int pos=readBuffer.position();
			length = (readBuffer.getShort(pos) & 0XFF)+2;
			readBuffer.position(pos);
			if( length> MAX_APDU_LEN ){
				String err = "Invalid DLMS msg: apdu.length="+length ;
				throw new MessageParseException(err) ;
			}
			apdu = ByteBuffer.allocate(length) ;
			ioState = IoState.READ_BODY ;
		}
		if( ioState == IoState.READ_BODY ) {
			while( apdu.hasRemaining() ){		//APDU is not provisioned
				if( readBuffer.hasRemaining() )
					apdu.put(readBuffer.get());
				else
				{
					//缓冲区没有数据了，但是报文体还没有读取完毕
					if( log.isDebugEnabled() )
						log.debug("Insuficient buffer to provision DLMS apdu");
					return false ;
				}
			}
			apdu.flip() ;		//ready for read.
			ioState = IoState.READ_DONE ;
		}
		return ioState == IoState.READ_DONE;
	}

	public boolean write(ByteBuffer writeBuffer) {
		synchronized (this) {
			return _write(writeBuffer);
		}
	}

	private boolean _write(ByteBuffer writeBuffer) {
		if( ioState == IoState.INIT || ioState == IoState.READ_DONE ){
			ioState = IoState.WRITE_BODY ;
		}
		if( ioState == IoState.WRITE_BODY ){
			if( null != apdu ){
				while(apdu.hasRemaining() && writeBuffer.hasRemaining() )
					writeBuffer.put(apdu.get());
				if( ! apdu.hasRemaining() )
					ioState = IoState.WRITE_DONE;
			}
			else
				ioState = IoState.WRITE_DONE;
		}
		return ioState == IoState.WRITE_DONE;
	}
	
	public void reset(){
		ioState = IoState.INIT;
		if( null != apdu ){
			apdu.position(0);
			length = apdu.remaining();
		}
		else{
			length = 0;
		}
	}

	public byte[] getRawPacket() {
		if( ioState == IoState.INIT ){
			if( null == apdu )
				return new byte[0];
			length = apdu.remaining();
		}
		else if( ioState == IoState.WRITE_DONE ){
			reset();
		}
		ByteBuffer result = ByteBuffer.allocate(length + 300) ;
		StringBuffer sb = new StringBuffer(100);
		sb.append("iotime=").append(ioTime);
		sb.append("|peeraddr=").append(peerAddr).append("|txfs=");
		sb.append(txfs).append("|");
		result.put(sb.toString().getBytes());
		write(result);
		reset();		//Make message send-enabled
		result.flip();
		byte[] innerPacket = new byte[result.remaining()];
		result.get(innerPacket);
		return innerPacket;
	}

	public synchronized byte[] getRawFrame(){
		if( ioState == IoState.INIT ){
			if( null == apdu )
				return new byte[0];
			length = apdu.remaining();
		}
		else if( ioState == IoState.WRITE_DONE ){
			reset();
		}
		ByteBuffer result = ByteBuffer.allocate(length ) ;
		write(result);
		result.flip();
		reset();		//Make message send-enabled
		return result.array();
	}
	
	public String getRawPacketString() {
		return HexDump.toHex(getRawFrame());
	}

	public int length() {
		if( ioState == IoState.INIT ){
			if( null == apdu )
				return 0;
			length = apdu.remaining();
		}
		return length;
	}

	@Override
	public final MessageType getMessageType() {
		return MessageType.MSG_ANSI;
	}

	@Override
	public boolean isHeartbeat() {
		if( null != apdu && apdu.remaining()>=1 ){
			String data=HexDump.hexDumpCompact(apdu);
			AnsiMessageElement ame=new AnsiMessageElement();
			ame.decodeMessage(data);
			if(ame.getServerTag()!=null&&(ame.getServerTag().equalsIgnoreCase("FF")||ame.getServerTag().equalsIgnoreCase("FE"))){
				return true;
			}
		}
		return false;
	}
	public boolean isLogon(){
		if( null != apdu && apdu.remaining()>=1 ){
			String data=HexDump.hexDumpCompact(apdu);
			AnsiMessageElement ame=new AnsiMessageElement();
			ame.decodeMessage(data);
			if(ame.getServerTag()!=null&&(ame.getServerTag().equalsIgnoreCase("FF"))){
				return true;
			}
		}
		return false;
	
	}
	
	public void setApdu(byte[] buf){
		if( null != buf )
			apdu = ByteBuffer.wrap(buf);
	}
	
	public void setApdu(ByteBuffer buf){
		apdu = buf;
	}
	
	public ByteBuffer getApdu(){
		return apdu;
	}

	@Override
	public String getTxfs() {
		return txfs;
	}

	@Override
	public void setTxfs(String fs) {
		txfs = fs;
	}

	public final int getDirection() {
		return direction;
	}

	public final void setDirection(int direction) {
		this.direction = direction;
	}

	public final String getLogicalAddress() {
		return logicalAddress;
	}

	public final void setLogicalAddress(String logicalAddress) {
		this.logicalAddress = logicalAddress;
	}
	
	@Override
	public final String toString(){
		return this.getRawPacketString();
	}
}
