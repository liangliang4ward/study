/**
 * Refer to <DLMS规约解析(本地E模式）V1.4 2012-02-23.doc>.
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
 * @author gaoll
 *
 */
public class DlmsHDLCMessage extends MessageBase{
	private static final Logger log = Logger.getLogger(DlmsHDLCMessage.class);
	protected static enum IoState { INIT,READ_BODY,READ_DONE, WRITE_BODY,WRITE_DONE };
	private String logicalAddress = "";
	private int direction = IMessage.DIRECTION_UP;
	private String txfs="";

	//DLMS TCP mode protocol head parts definition: which 8 bytes.
	private int length = 0;		//The length of protocol-body. 2bytes, as unsigned-short.

	private byte[] serverAddr = new byte[]{0x00,0x02,(byte) 0xFE,(byte) 0xFF};
	private byte[] clientAddr = new byte[]{0x03};
	
	private byte[] destAddr;
	private byte[] srcAddr;
	
	private byte[] hcs;//头校验序列域
	private byte[] fcs;//帧校验序列域
	private byte controlField=0x32;
	//DLMS APDU( protocol body)
	private ByteBuffer apdu = null;
	private IoState ioState = IoState.INIT;
	
	private String dcLogicAddress =null;
	
	private ByteBuffer allFrame = null;
	
	/**是否厂家心跳*/
	public boolean moduleHeartBeart=false;
	
	protected void locating(ByteBuffer buf) throws MessageParseException{
		int pos = buf.position();
		
		//locating bytes = version: [00 01] ;
		int dlmsPos = -1;
		int iotimePos = -1;
		for(; pos<buf.limit()-2; pos++ ){
			if( buf.get(pos) == (byte)0x7E && buf.get(pos+1) == (byte)0xA0 ){
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
			int position=readBuffer.position();
			if( readBuffer.remaining()<8 ){
				if( log.isDebugEnabled() )
					log.debug("Insuficient buffer to resolve DLMS protocol-head");
				return false;		//Insuficient buffer to resolve protocol-head.
			}
			readBuffer.get(); //7E
			int pos=readBuffer.position();
			byte a0 = readBuffer.get(); //A0
			if(a0==0x00){ //外置模块的心跳 %>_<%
				readBuffer.position(pos);
				DlmsMessage dm = new DlmsMessage();
				if(dm.read(readBuffer)){
					this.apdu = dm.getApdu();
					this.moduleHeartBeart = true;
					return true;
				}else{
					return false;
				}
				//return dm.read(readBuffer);
			}
			length = readBuffer.get() & 0XFF;
			allFrame = ByteBuffer.allocate(length+2) ;
			ioState = IoState.READ_BODY ;
			readBuffer.position(position);
		}
		if( ioState == IoState.READ_BODY ) {
			while( allFrame.hasRemaining() ){		//APDU is not provisioned
				if( readBuffer.hasRemaining() )
					allFrame.put(readBuffer.get());
				else
				{
					//缓冲区没有数据了，但是报文体还没有读取完毕
					if( log.isDebugEnabled() )
						log.debug("Insuficient buffer to provision DLMS apdu");
					return false ;
				}
			}
			int apduLen = length;
			allFrame.flip() ;		//ready for read.
			allFrame.get();//7E
			allFrame.get();//A0/A8
			allFrame.get();//length
			apduLen = apduLen-2;
			int pos = allFrame.position();
			int serverLen = 0;
			apduLen = apduLen-1;
			while((allFrame.get()&0x01)==0){
				serverLen++;
				apduLen = apduLen-1;
			}
			serverAddr = new byte[serverLen+1];
			allFrame.position(pos);
			allFrame.get(serverAddr, 0, serverAddr.length);
			pos = allFrame.position();
			int clientLen = 0;
			apduLen = apduLen-1;
			while((allFrame.get()&0x01)==0){
				clientLen++;
				apduLen = apduLen-1;
			}
			clientAddr = new byte[clientLen+1];
			if(direction==DIRECTION_UP){
				destAddr=clientAddr;
				srcAddr =serverAddr;
			}else{
				destAddr=serverAddr;
				srcAddr =clientAddr;
			}
			allFrame.position(pos);
			allFrame.get(clientAddr,0,clientAddr.length);
			controlField = allFrame.get();
			hcs = new byte[2];
			apduLen = apduLen-2;
			allFrame.get(hcs);//HCS
			pos = allFrame.position();
			if(apduLen==1){
				fcs = hcs;
			}else{
				apduLen-=6;
				allFrame.get();allFrame.get();allFrame.get();//E6 E6 00
				apdu = ByteBuffer.allocate(apduLen);
				while(apdu.hasRemaining()){
					if(allFrame.hasRemaining()){
						apdu.put(allFrame.get());
					}
				}
				fcs=new byte[2];
				allFrame.get(fcs);
				apdu.flip();
			}
			ioState = IoState.READ_DONE ;
			allFrame.flip();
		}
		return ioState == IoState.READ_DONE;
	}

	public boolean write(ByteBuffer writeBuffer) {
		synchronized (this) {
			return _write(writeBuffer);
		}
	}

	private boolean _write(ByteBuffer writeBuffer) {
		int pos=writeBuffer.position();
		if( ioState == IoState.INIT || ioState == IoState.READ_DONE ){
			if( writeBuffer.remaining()<8 ){
				if( log.isDebugEnabled() )
					log.debug("Insuficient buffer to write DLMS protocol-head");
				return false;		//Insuficient buffer to resolve protocol-head.
			}
			writeBuffer.put((byte)0x7E);
			writeBuffer.put((byte)0xA0);
			//Make sure the apdu is ready.
			length=2+serverAddr.length+clientAddr.length+6+(null != apdu ? apdu.remaining() : 0)+2;
			writeBuffer.put((byte)length);
			if(srcAddr!=null && destAddr!=null){
				writeBuffer.put(destAddr);
				writeBuffer.put(srcAddr);
			}else{
				writeBuffer.put(serverAddr);
				writeBuffer.put(clientAddr);				
			}
			writeBuffer.put(controlField);
			byte[] head = new byte[2+serverAddr.length+clientAddr.length+1];
			writeBuffer.position(pos+1);
			writeBuffer.get(head);
			String strHcs=FCS.fcs(HexDump.toHex(head));
			byte[] rawHcs = HexDump.toArray(strHcs);
			hcs = new byte[2];
			hcs[0]=rawHcs[1]; hcs[1]=rawHcs[0];
			writeBuffer.put(hcs);
			writeBuffer.put((byte)0xE6);
			writeBuffer.put((byte)0xE6);
			writeBuffer.put((byte)0x00);
			ioState = IoState.WRITE_BODY ;
		}
		if( ioState == IoState.WRITE_BODY ){
			if( null != apdu ){
				while(apdu.hasRemaining() && writeBuffer.hasRemaining() )
					writeBuffer.put(apdu.get());
				
				if( ! apdu.hasRemaining() ){
					writeBuffer.position(pos+1);
					byte[] hdlc = new byte[length-2];
					writeBuffer.get(hdlc);
					String strHcs=FCS.fcs(HexDump.toHex(hdlc));
					byte[] rawHcs = HexDump.toArray(strHcs);
					fcs = new byte[2];
					fcs[0]=rawHcs[1]; fcs[1]=rawHcs[0];
					writeBuffer.put(fcs);
					writeBuffer.put((byte)0x7E);
					ioState = IoState.WRITE_DONE;
				}
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
		if(allFrame == null && apdu==null) return null;

		if(apdu!=null) return apdu.array();
		
		if(allFrame!=null) return allFrame.array();
		
		return null;
	}
	
	public String getRawPacketString() {
		if(getRawFrame() == null) return null;
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
		return MessageType.MSG_DLMS_HDLC;
	}

	@Override
	public boolean isHeartbeat() {
		if( null != apdu && apdu.remaining()>=1 ){
			if( apdu.get(0) == (byte)0xDD )
				return true;
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

	public String getDcLogicAddress() {
		return dcLogicAddress;
	}

	public void setDcLogicAddress(String dcLogicAddress) {
		this.dcLogicAddress = dcLogicAddress;
	}

	public byte getControlField() {
		return controlField;
	}

	public void setControlField(byte controlField) {
		this.controlField = controlField;
	}

	public byte[] getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(byte[] serverAddr) {
		this.serverAddr = serverAddr;
	}

	public byte[] getClientAddr() {
		return clientAddr;
	}

	public void setClientAddr(byte[] clientAddr) {
		this.clientAddr = clientAddr;
	}
}
