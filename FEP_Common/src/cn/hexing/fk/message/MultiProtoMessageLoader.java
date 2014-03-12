package cn.hexing.fk.message;

import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.HexDump;

/**
 * 多规约Message对象序列号以及反序列号
 *
 */
public class MultiProtoMessageLoader implements MessageLoader {
	private static final Logger log = Logger.getLogger(MultiProtoMessageLoader.class);

	public IMessage loadMessage(String serializedString) {
		StringTokenizer st = new StringTokenizer(serializedString,"|");
		IMessage msg = null;
		String token;
		try{
			token = st.nextToken();
			ByteBuffer buf = HexDump.toByteBuffer(token);
			msg = MultiProtoRecognizer.recognize(buf);
			if( null == msg || !msg.read(buf) ){
				log.info("从缓存加载的信息，非终端规约消息："+serializedString);
				return null;
			}
			while(st.hasMoreTokens()){
				String item = st.nextToken();
				if( "ioti".equalsIgnoreCase(item.substring(0, 4))){
					token = item.substring(7);	//iotime=
					msg.setIoTime(Long.parseLong(token));
				}
				else if( "peer".equalsIgnoreCase(item.substring(0, 4))){
					token = item.substring(9);	//peeraddr=
					msg.setPeerAddr(token);
				}
				else if( "txfs".equalsIgnoreCase(item.substring(0, 4))){
					token = item.substring(5);	//peeraddr=
					msg.setTxfs(token);
				}else if( "logicAddress".equalsIgnoreCase(item.substring(0, 12))){
					token = item.substring(13);
					msg.setLogicalAddress(token);
				}
			}
			msg.setPriority(IMessage.PRIORITY_LOW);
			return msg;
		}catch(Exception exp){
			log.warn("缓存加载错误：buf="+serializedString+",exp="+exp.getLocalizedMessage());
		}
		return null;
	}

	public String serializeMessage(IMessage message) {
		StringBuffer sb = new StringBuffer(512);
		sb.append(message.getRawPacketString()).append("|iotime=");
		sb.append(message.getIoTime()).append("|peeraddr=").append(message.getPeerAddr());
		sb.append("|txfs=").append(message.getTxfs());
		sb.append("|logicAddress="+message.getLogicalAddress());
		return sb.toString();
	}

}
