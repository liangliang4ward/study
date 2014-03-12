/**
 * Refer to <DLMS规约解析(TCP模式）V1.4 2012-02-23.doc>.
 * DlmsMessage wrap the APDU using IP communication mode.
 * source_address & dest_address may support Data-concentrator.
 */
package com.hx.dlms.message;

import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cn.hexing.fk.exception.MessageParseException;
import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.MessageBase;
import cn.hexing.fk.message.MessageType;
import cn.hexing.fk.utils.FCS;
import cn.hexing.fk.utils.HexDump;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class DlmsMessage extends MessageBase{
	private static final Logger log = Logger.getLogger(DlmsMessage.class);
	private static final int MAX_APDU_LEN = 1024*10;
	protected static enum IoState { INIT,READ_BODY,READ_DONE, WRITE_BODY,WRITE_DONE };
	private String logicalAddress = "";
	private int direction = IMessage.DIRECTION_UP;
	private String txfs="";
	
	public static short DstAddrToMetter = 0x0001;
	public static short DstAddrToModule = 0x0070;

	//DLMS TCP mode protocol head parts definition: which 8 bytes.
	private short version = 0x0001;
	private short srcAddr = 0x0001;
	private short dstAddr = 0x0001;
	private int length = 0;		//The length of protocol-body. 2bytes, as unsigned-short.

	//DLMS APDU( protocol body)
	private ByteBuffer apdu = null;
	private IoState ioState = IoState.INIT;
	
	private String dcLogicAddress =null;
	
	private byte functionCode = 0x00;
	
	protected void locating(ByteBuffer buf) throws MessageParseException{
		int pos = buf.position();
		//locating bytes = version: [00 01] ;
		int dlmsPos = -1;
		int iotimePos = -1;
		for(; pos<buf.limit()-2; pos++ ){
			if( buf.get(pos) == 0x00 && buf.get(pos+1) == 0x01 ){
				dlmsPos = pos;
				break;
			}
			else if( iotimePos == -1 ){
				if( buf.get(pos) == 'i' ){
					iotimePos = pos;
				}
			}
		}
		if( dlmsPos >= 0 ){
			if( iotimePos>=0 ){
				if( dlmsPos-iotimePos>16 ){
					byte[] leadBytes = new byte[dlmsPos-iotimePos];
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
			buf.position(dlmsPos);
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
	}
	
	public boolean read(ByteBuffer readBuffer) throws MessageParseException {
		if( ioState == IoState.INIT ) {
			locating(readBuffer); //locating DLMS message....
			if( readBuffer.remaining()<8 ){
				if( log.isDebugEnabled() )
					log.debug("Insuficient buffer to resolve DLMS protocol-head");
				return false;		//Insuficient buffer to resolve protocol-head.
			}
			
			version = readBuffer.getShort();
			srcAddr = readBuffer.getShort();
			dstAddr = readBuffer.getShort();
			length = readBuffer.getShort() & 0XFFFF;
			//Validate the DLMS message protocol-head.
			if( version > 10 || version <= 0 ){
				String err = "Invalid DLMS msg: ver="+version ;
				throw new MessageParseException(err) ;
			}
			if( length> MAX_APDU_LEN ){
				String err = "Invalid DLMS msg: apdu.length="+length ;
				throw new MessageParseException(err) ;
			}
			apdu = ByteBuffer.allocate(length) ;
			ioState = IoState.READ_BODY ;
		}
		if( ioState == IoState.READ_BODY ) {
			boolean isFirstByte = true;
			while( apdu.hasRemaining() ){		//APDU is not provisioned
				if( readBuffer.hasRemaining() ){
					byte c = readBuffer.get();
					if(isFirstByte && functionCode==0x00){
						isFirstByte = false;
						functionCode = c ;
					}
					apdu.put(c);					
				}else
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
			if( writeBuffer.remaining()<8 ){
				if( log.isDebugEnabled() )
					log.debug("Insuficient buffer to write DLMS protocol-head");
				return false;		//Insuficient buffer to resolve protocol-head.
			}
			writeBuffer.putShort(version);
			writeBuffer.putShort(srcAddr);
			writeBuffer.putShort(dstAddr);
			//Make sure the apdu is ready.
			length = null != apdu ? apdu.remaining() : 0;
			writeBuffer.putShort((short)length);
			ioState = IoState.WRITE_BODY ;
		}
		if( ioState == IoState.WRITE_BODY ){
			if( null != apdu ){
				boolean isFirstByte = true;
				while(apdu.hasRemaining() && writeBuffer.hasRemaining() ){
					byte c = apdu.get();
					if(isFirstByte && functionCode == 0x00){
						isFirstByte = false;
						functionCode = c;
					}
					writeBuffer.put(c);
				}
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
		ByteBuffer result = ByteBuffer.allocate(length + 8 + 300) ;
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
		ByteBuffer result = ByteBuffer.allocate(length + 8 ) ;
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
		return length+8;
	}

	@Override
	public final MessageType getMessageType() {
		return MessageType.MSG_DLMS;
	}

	@Override
	public boolean isHeartbeat() {
		if( null != apdu && apdu.remaining()>=1 ){
			if( apdu.get(0) == (byte)0xDD )
				return true;
		}
		return false;
	}
	
	/**
	 * 生成HDLC层的帧
	 * 标志	帧格式  	目的地址	源地址	控制	HCS	信息	FCS	标志
	 * @return
	 */
	public boolean createHDLCFrame(){
		//7E A0|A8 LL ADDR CC HCS_H HCS_L E6 E6 00 APDU 7E
		String strApdu = HexDump.toHex(apdu.array());
		StringBuilder sb = new StringBuilder();
		sb.append("A0")
		  .append("00")
		  .append("4868FEFF")
		  .append("03")
		  .append("10")
		  .append("CS")
		  .append("CS")
		  .append("E6E600")
		  .append(strApdu)
		  .append("CS")
		  .append("CS");
		sb.replace(2, 4, HexDump.toHex((byte)sb.length()));
		String hcs = FCS.fcs(sb.substring(0, 16));
		sb.replace(16, 18, hcs.substring(2, 4));
		sb.replace(18, 20, hcs.substring(0, 2));
		String fcs = FCS.fcs(sb.substring(0,sb.length()-4));
		sb.replace(sb.length()-4, sb.length()-2, fcs.substring(2, 4));
		sb.replace(sb.length()-2, sb.length(), fcs.substring(0, 2));
		sb.insert(0, "7E");
		sb.append("7E");
		apdu = HexDump.toByteBuffer(sb.toString());
		this.ioState=IoState.WRITE_BODY;
		return true;
	}
	
	public static void main(String[] args) {
		DlmsMessage dm = new DlmsMessage();
		dm.setApdu(HexDump.toArray("601da109060760857405080101be10040e01000000065f1f0400007e1f04b00001000100010001da000100010001000dc0010100010100000403ff0200"));
		dm.createHDLCFrame();
	}
	
	/**
	 * 判断是否是认证请求
	 */
	public boolean isAA(){
		if(null != apdu && apdu.remaining()>=1){
			byte b=apdu.get(0);
			if((byte)0x60==b || (byte)0x61==b){
				return true;
			}
		}
		return false;
	}
	public boolean isEventNeedReply(){
		if(null != apdu&& (apdu.get(0)& 0xFF)==194 && apdu.capacity()==28 ){
			String strApdu=HexDump.toHex(apdu.array());
			String obis = strApdu.substring(14*2, 14*2+9*2);
			return "00010000618A00FF02".equals(obis)||
				   "00010000618100FF02".equals(obis);
		}
		return false;
	}
	public static final DlmsMessage createHeartReply(){
		DlmsMessage msg = new DlmsMessage();
		msg.apdu = ByteBuffer.allocate(1);
		msg.apdu.put((byte)0xDA);
		msg.apdu.flip();
		msg.length = 1;
		return msg;
	}
	
	public static final DlmsMessage createEventReply(){
		DlmsMessage msg = new DlmsMessage();
		msg.apdu = ByteBuffer.allocate(2);
		msg.apdu.put((byte)0xF3);
		msg.apdu.put((byte)0x00);
		msg.apdu.flip();
		msg.length = 2;
		return msg;
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

	public String getDcLogicAddress() {
		return dcLogicAddress;
	}

	public void setDcLogicAddress(String dcLogicAddress) {
		this.dcLogicAddress = dcLogicAddress;
	}

	public short getSrcAddr() {
		return srcAddr;
	}

	public void setSrcAddr(short srcAddr) {
		this.srcAddr = srcAddr;
	}

	public short getDstAddr() {
		return dstAddr;
	}

	public void setDstAddr(short dstAddr) {
		this.dstAddr = dstAddr;
	}

	public byte getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(byte functionCode) {
		this.functionCode = functionCode;
	}
}
