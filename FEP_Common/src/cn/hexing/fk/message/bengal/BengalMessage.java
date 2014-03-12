package cn.hexing.fk.message.bengal;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.hexing.fk.common.spi.socket.IChannel;
import cn.hexing.fk.exception.MessageParseException;
import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.MessageType;

/**
 * Refer to Hexing_XML_Protocol.doc
 * @author bhw
 *
 */
public class BengalMessage implements IMessage {
	private static final Logger log = Logger.getLogger(BengalMessage.class);
	private static int SEQUENCE = 0;
	private static final MessageType messageType = MessageType.MSG_BENGAL;
	private static final int getNextSeq(){
		SEQUENCE++;
		if( SEQUENCE>99 ){
			SEQUENCE = 1;
		}
		return SEQUENCE;
	}
	
	private StringBuilder rawMessage = null;
	private IChannel source;
	private int priority = IMessage.PRIORITY_LOW;	//low priority
	private long ioTime;		//完整收到消息或者发送完毕时间
	private String peerAddr;	//对方的IP:PORT地址
	private String serverAddress;	//终端实际连接的外网IP：PORT。用于接收到消息时与终端资产表比对。
	private String txfs="";			//通信方式： gprs、CDMA、230、LAN、FIBER、SMS
	private int state = IMessage.STATE_INVALID;
	private int returnCount = 0;
	private char lastChar = 0;
	private int writePos = 0;

	//protocol - related attributes:
	private String version = "1";
	private String dir = "down";
	public String meterAddress = null;
	private String dataProtocol = "1";  // 1==hex of "DLMS", 2=="xml";
	private int funcCode = 3;			// 0 undefined,
	private int seq = getNextSeq();
	private String cryptoAlgorithm = "10";  //00：不适用加密算法;10：AES-128;11：AES-192;12：AES-256;20：DES;30：SHA;40：ECC
	private int signatureType = 1 ;  //0：不使用数字签名; 1：SHA-256
	private String random = null;	 //
	private String data = null;
	private String signatureData = null;

	public IChannel getSource() {
		return source;
	}

	public void setSource(IChannel src) {
		source = src;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public boolean read(ByteBuffer readBuffer) throws MessageParseException {
		if( state == IMessage.STATE_INVALID ){
			rawMessage = new StringBuilder();
			returnCount = 0;
			state = IMessage.STATE_READ_DATA;
		}
		if( state == IMessage.STATE_READ_DATA ){
			while( readBuffer.hasRemaining() ){
				char c = (char)readBuffer.get();
				if( c == '\r' || c=='\n' ){
					returnCount++;
					//Note: to handle case "\r\n"
					if( c == '\n' && lastChar == '\r' ){
						returnCount--;
					}
					lastChar = c;
					if( returnCount == 2 ){
						state = IMessage.STATE_READ_DONE;
						onReadDone();
						break;
					}
				}
				else{
					returnCount = 0;
					rawMessage.append(c);
				}
			}
		}
		return state == IMessage.STATE_READ_DONE;
	}
	
	private String xmlTagValue(String tagName){
		if( null == rawMessage )
			return null;
		int index1 = rawMessage.indexOf("<"+tagName+">");
		int index2 = rawMessage.indexOf("</"+tagName+">");
		if( index1>=0 && index2>0 ){
			try{
				return rawMessage.substring(index1+tagName.length()+2, index2);
			}catch(Exception e){
				log.warn("xmlTagValue Exception. tag="+tagName+",index1="+index1+",index2="+index2+",raw="+rawMessage.toString());
			}
		}
		return null;
	}
	
	private int xmlTagIntValue(String tagName){
		String val = xmlTagValue(tagName);
		if( null == val )
			return 0;
		val = val.trim();
		if( val.length() == 0 )
			return 0;
		return Integer.parseInt(val);
	}
	
	private void populateXmlTagValue(String tag,String val){
		rawMessage.append("<").append(tag).append(">");
		if( null != val )
			rawMessage.append(val);
		rawMessage.append("</").append(tag).append(">");
	}
	
	private void populateXml(){
		rawMessage = new StringBuilder();
		rawMessage.append("<? xml version='1.0' ?><h:rt xmlns h='ProtocolHead'>");
		populateXmlTagValue("h:pv",version);
		populateXmlTagValue("h:addr",this.meterAddress);
		populateXmlTagValue("h:dir",this.dir);
		populateXmlTagValue("h:pt",this.dataProtocol);
		populateXmlTagValue("h:fc",String.valueOf(this.funcCode));
		populateXmlTagValue("h:seq",String.valueOf(this.seq));
		populateXmlTagValue("h:e",this.cryptoAlgorithm);
		populateXmlTagValue("h:a",String.valueOf(this.signatureType));
		populateXmlTagValue("h:r",this.random);
		populateXmlTagValue("h:d",this.data);
		populateXmlTagValue("h:sg",this.signatureData);
		rawMessage.append("</h:rt>");
	}
	
	private void onReadDone(){
		this.version = xmlTagValue("h:pv");
		this.dir = xmlTagValue("h:dir");
		this.meterAddress = xmlTagValue("h:addr");
		this.dataProtocol = xmlTagValue("h:pt");
		this.funcCode = xmlTagIntValue("h:fc");
		this.seq = xmlTagIntValue("h:seq");
		this.cryptoAlgorithm = xmlTagValue("h:e");
		this.signatureType = xmlTagIntValue("h:a");
		this.random = xmlTagValue("h:r");
		this.data = xmlTagValue("h:d");
		this.signatureData = xmlTagValue("h:sg");
		if( log.isDebugEnabled() )
			log.debug("rawMessage="+rawMessage);
	}

	public void enableRewrite(){
		state = IMessage.STATE_INVALID;
		writePos = 0;
	}
	
	//XML do not support GBK characters, only support ISO-8859-1
	public boolean write(ByteBuffer writeBuffer) {
		if( null == rawMessage ){
			populateXml();
		}
		while(writePos<=(rawMessage.length()-1) && writeBuffer.hasRemaining() ){
			writeBuffer.put((byte)rawMessage.codePointAt(writePos));
			writePos++;
		}
		//Write "\n\n";
		while( writePos>=rawMessage.length() && writePos < rawMessage.length()+2 && writeBuffer.hasRemaining() ){
			writeBuffer.put((byte)'\n');
			writePos++;
		}
		return writePos == rawMessage.length()+2;
	}

	public long getIoTime() {
		return ioTime;
	}

	public void setIoTime(long time) {
		ioTime = time;
	}

	public String getPeerAddr() {
		return peerAddr;
	}

	public void setPeerAddr(String peer) {
		peerAddr = peer;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getTxfs() {
		return txfs;
	}

	public void setTxfs(String fs) {
		txfs = fs;
	}

	public int getRtua() {
		return 0;
	}

	public String getStatus() {
		return null;
	}

	public void setStatus(String status) {
	}

	public Long getCmdId() {
		return null;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public byte[] getRawPacket() {
		if( null == rawMessage )
			populateXml();
		ByteBuffer buf = ByteBuffer.allocate(length()+2);
		write(buf);
		return buf.array();
	}

	public String getRawPacketString() {
		if( null == rawMessage )
			populateXml();
		return rawMessage.toString();
	}

	public boolean isHeartbeat() {
		return funcCode == 2;
	}

	public boolean isTask() {
		return funcCode == 5 || funcCode == 6;
	}

	public void setTask(boolean isTask) {
		
	}

	public int length() {
		return null==rawMessage? 0 : rawMessage.length();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMeterAddress() {
		return meterAddress;
	}

	public void setMeterAddress(String fromAddress) {
		this.meterAddress = fromAddress;
	}

	public String getDataProtocol() {
		return dataProtocol;
	}

	public void setDataProtocol(String dataProtocol) {
		this.dataProtocol = dataProtocol;
	}

	public int getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(int funcCode) {
		this.funcCode = funcCode;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getCryptoAlgorithm() {
		return cryptoAlgorithm;
	}

	public void setCryptoAlgorithm(String cryptoAlgorithm) {
		this.cryptoAlgorithm = cryptoAlgorithm;
	}

	public int getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(int signatureType) {
		this.signatureType = signatureType;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSignatureData() {
		return signatureData;
	}

	public void setSignatureData(String signatureData) {
		this.signatureData = signatureData;
	}

	
	public BengalMessage createConfirm(){
		BengalMessage confirm = null;
		if( this.funcCode == 1 ){
			confirm = new BengalMessage();
			confirm.dataProtocol = this.dataProtocol;
			confirm.data = this.data;
			confirm.dir = "down";
			confirm.funcCode = 2;
			confirm.meterAddress = this.meterAddress;
			confirm.seq = this.seq;
			confirm.cryptoAlgorithm = "00";
			confirm.signatureType = 0;
		}
		else if( this.funcCode == 6 ){  //Meter send event to Master-Station. 
			confirm = new BengalMessage();
			confirm.dataProtocol = this.dataProtocol;
			confirm.data = "";
			confirm.dir = "down";
			confirm.funcCode = 5;
			confirm.meterAddress = this.meterAddress;
			confirm.seq = this.seq;
			confirm.cryptoAlgorithm = "00";
			confirm.signatureType = 0;
		}
		return confirm;
	}

	@Override
	public String toString() {
		if( null != meterAddress )
			return this.getRawPacketString();
		else
			return "Null Message";
	}

	@Override
	public String getLogicalAddress() {
		return "000000000000".substring(this.meterAddress.length())+this.meterAddress;
	}

	@Override
	public void setLogicalAddress(String logicAddr) {
		this.meterAddress = logicAddr;
	}

	public final String getDir() {
		return dir;
	}

	public final void setDir(String dir) {
		this.dir = dir;
	}
}
