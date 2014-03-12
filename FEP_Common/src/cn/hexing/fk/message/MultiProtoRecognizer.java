package cn.hexing.fk.message;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.hexing.fk.message.bengal.BengalMessage;
import cn.hexing.fk.message.gate.MessageGate;
import cn.hexing.fk.message.gw.MessageGw;
import cn.hexing.fk.message.zj.MessageZj;
import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.message.AnsiMessage;
import com.hx.dlms.message.DlmsHDLCMessage;
import com.hx.dlms.message.DlmsMessage;

public class MultiProtoRecognizer implements IMessageCreator{
	private static final Logger log = Logger.getLogger(MultiProtoRecognizer.class);
	private static final byte[] gateFlag = "JBBS".getBytes();
	/**
	 * 多规约识别器.
	 * 国网规约至少16字节。浙江规约至少13字节。
	 * 如果缓冲区<13，那么不能识别消息类型
	 * @param buf
	 * @return
	 */
	public static IMessage recognize(ByteBuffer buf) {
		if( buf.remaining()<9 )//dlms心跳报文确认9个字节
			return null;	//缓冲区长度不足以识别报文类型
		int startPos = buf.position();
		//0. 判断是否字符串
		byte ch = 0;
		int p = buf.position();
		while(buf.hasRemaining() && (ch = buf.get(p)) == 0 ){
			buf.get();
			p++;
		}
		if( ch == '<' || ch == '\n' || ch == '\r' ){
			int len = Math.min(buf.remaining(), 60);
			byte[] xmlBytes = new byte[len];
			int posSave = buf.position();
			buf.get(xmlBytes);
			buf.position(posSave);
			String xmlFrag = new String(xmlBytes);
			if( xmlFrag.indexOf("xml")>0 && xmlFrag.indexOf("ProtocolHead")>0 )
				return new BengalMessage();
		}
		
		
		//1. 先识别网关规约
		int flen = gateFlag.length;
		boolean matched = true;
		for( int pos = buf.position(); pos+flen < buf.limit(); pos++ ){
			matched = true;
			for( int i=0;i<flen; i++){
				if( buf.get(pos+i) != gateFlag[i] ){
					matched = false;
					break;
				}
			}
			if( matched ){
				buf.position(pos);
				break;
			}
		}
		if( matched )
			return new MessageGate();
		//2. 识别浙江规约或者国网规约
		IMessage gwOrZjMsg = null;
		int gwOrZjLoc=0;
		int last68 = -1;
		for( int pos = buf.position(); pos+10 <= buf.limit(); pos++){
			//先定位第一个68H
			gwOrZjLoc++;
			if( 0x68 != buf.get(pos) )
				continue;
			last68 = pos;
			//先定位国网规约
			if( 0x68 == buf.get(pos+5) ){
				//可能是国网规约：68H＋L(2)+L(2)+68H
				short len1 = buf.getShort(pos+1);
				short len2 = buf.getShort(pos+3);
				if( len1 == len2 ){
					gwOrZjMsg = new MessageGw();					
					break;
				}
				
			}
			//再定位浙江规约.如果是浙江规约，那么pos+5位置绝对不可能是68H
			if (0x68 == buf.get(pos + 7)) {// 浙江规约判断太简单，需要加入更多的判断
				// 68 xx xx xx xx xx xx 68 C LL HL xx..xx CS 0x16
				try {
					byte ll = buf.get(pos + 9); // 长度低字节
					byte hl = buf.get(pos + 10);// 长度高字节
					byte[] len = new byte[] { hl, ll };
					BigInteger bi = new BigInteger(len);
					int iLen = bi.intValue();
					if (buf.limit() < pos + iLen + 12 && pos + iLen + 12 > 0)
						continue;

					if (0x16 == buf.get(pos + iLen + 12)) {// 还可再加上计算校验码
						gwOrZjMsg = new MessageZj();
						break;
					}
				} catch (Exception e) {
					break;
				}
			}
		}
	
		//Dlms链路层帧
		buf.position(startPos);
		for(int pos=buf.position();pos<buf.limit()-2;pos++){
			
			if(gwOrZjMsg!=null) break;
			
			byte c = buf.get(pos);
			if( Character.isLetterOrDigit(c) || c == '|' ){
				continue;
			}
			if(c==(byte)0x7E){
				if(buf.get(pos+1)==(byte)0xA0){//这样判断不太安全，可能还需要添加其他判断
					
					int len=buf.get(pos+2)&0xFF;
					try {
						if(0x7E==buf.get(len+1))
							return new DlmsHDLCMessage();
					} catch (Exception e) {
						break;
					}
				}
			}
		}
		
		IMessage dlmsOrAnsiMsg=null;
		int dlmsOrAnsiMsgLoc=0;
		/*
		 *Dlms帧 &Ansi帧
		 */
		buf.position(startPos);
		for(int pos=buf.position(); pos<buf.limit()-2; pos++ ){
			byte c = buf.get(pos);
			dlmsOrAnsiMsgLoc++;
			if( Character.isLetterOrDigit(c) || c == '|' ){
				continue;
			}
			
			//如果第一次进入循环,符合下面条件,直接认为是DLMS帧
			if( c == 0x00 ){
				if( buf.get(pos+1) == 0x01 ){
					//如何准确定位是DLMS帧，并且不影响下面帧定位
					dlmsOrAnsiMsg=new DlmsMessage();
					break;
				}
			}
			//譬如帧是这样：60xxA2...0001 ....这个帧是ansi(如果不加判断，肯定会认为DLMS)
			//如果帧是这样:0001...60xxA200001这个帧是DLMS(如果先判断ansi，那么肯定会认为是ansi)
			if(c == 0x60 && 
			(buf.get(pos+2) == (byte)0xA2 || 
			 buf.get(pos+2) == (byte)0xA4 ||  
			 buf.get(pos+2) == (byte)0xA8 ||  
			 buf.get(pos+2) == (byte)0xBE)){
				dlmsOrAnsiMsg = new  AnsiMessage();
				break;
			}
		}
		buf.position(startPos);
		if(dlmsOrAnsiMsg!=null || gwOrZjMsg!=null){
			//同时定位到两种类型,计算帧开始的位置，如果开始位置小，则是该了性帧的可能性大。
			return gwOrZjLoc>dlmsOrAnsiMsgLoc?dlmsOrAnsiMsg:gwOrZjMsg;
		}
		
		//3. 现有缓冲区不可识别。需要把68之前所有信息丢弃。
		byte[] dump = null;
		if( last68 == -1 ){
			dump = new byte[buf.remaining()>200 ? 200 : buf.remaining() ];
			for(int i=0; i<dump.length; i++)
				dump[i] = buf.get(buf.position()+i);
			//未发现68，全部丢弃
			buf.position(0); buf.limit(0);
		}
		else{
			//丢弃最后一个68之前数据。
			int len = buf.limit() - last68;
			if( len == 0 ){
				//打印丢弃的数据内容
				dump = new byte[ buf.remaining() > 200 ? 200 : buf.remaining() ];
				for(int i=0; i<dump.length; i++)
					dump[i] = buf.get(buf.position()+i);
				//清空缓冲区
				buf.position(0); buf.limit(0);
			}
			else{
				//打印丢弃的数据内容
				int rem = last68-buf.position();
				dump = new byte[rem>200 ? 200 : rem ];
				for(int i=0; i<dump.length; i++ )
					dump[i] = buf.get(buf.position()+i);
				for(int i=0; i<len; i++ )
					buf.put(i, buf.get(last68+i));
				buf.position(0);
				buf.limit(len);
			}
		}
		if( log.isDebugEnabled() ){
			log.debug("多规约识别器丢弃无效数据："+HexDump.hexDumpCompact(dump, 0, dump.length));
		}
		return null;
	}
	
	public IMessage create() {
		return null;
	}
	public IMessage createHeartBeat(int reqNum) {
		return null;
	}

}
