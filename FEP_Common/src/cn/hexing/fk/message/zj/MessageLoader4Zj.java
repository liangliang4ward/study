/**
 * 浙江规约消息的系列化和加载器
 */
package cn.hexing.fk.message.zj;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.MessageLoader;
import cn.hexing.fk.message.MessageType;
import cn.hexing.fk.utils.HexDump;

/**
 */
public class MessageLoader4Zj implements MessageLoader {
	private static final Logger log = Logger.getLogger(MessageLoader4Zj.class);

	public MessageZj loadMessage(String serializedString) {
		StringTokenizer st = new StringTokenizer(serializedString,"|");
		MessageZj msg = new MessageZj();
		String token;
		boolean stop = false;
		try{
			token = st.nextToken();
			if( token.equals(MessageZj.class.getName()) ){
				//老缓存格式，第一个放Class名字|uprawstring=XXXXX
				token = st.nextToken().substring(12);
				stop = true;
			}
			if( !msg.read(HexDump.toByteBuffer(token)) ){
				log.info("从缓存加载的信息，非浙江规约消息："+serializedString);
				return null;
			}
			if( stop )
				return msg;		//后续不读取
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
		if( message.getMessageType() != MessageType.MSG_ZJ )
			return null;
		MessageZj msg = (MessageZj)message;
		StringBuffer sb = new StringBuffer(512);
		sb.append(msg.getRawPacketString()).append("|iotime=");
		sb.append(msg.getIoTime()).append("|peeraddr=").append(msg.getPeerAddr());
		sb.append("|txfs=").append(msg.getTxfs());
		return sb.toString();
	}

}
