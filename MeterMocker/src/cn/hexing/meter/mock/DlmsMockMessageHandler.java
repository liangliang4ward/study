package cn.hexing.meter.mock;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.hexing.fk.utils.HexDump;
import cn.hexing.meter.mock.listener.FutureMsgSendListener;

import com.hx.dlms.DecodeStream;
import com.hx.dlms.DlmsData;
import com.hx.dlms.aa.AareApdu;
import com.hx.dlms.aa.AarqApdu;
import com.hx.dlms.aa.AuthenticationMechanismName;
import com.hx.dlms.aa.InitiateResponse;
import com.hx.dlms.message.DlmsMessage;

public class DlmsMockMessageHandler extends MockerMessageHandler{

	private static AtomicInteger ai = new AtomicInteger();
	public DlmsMockMessageHandler(MockerClient dlmsMockClient) {
		super(dlmsMockClient);
		setValue(System.getProperty("meter.channelData"));
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		byte[] relMsg = (byte[]) msg;
		
		DlmsMessage dm = new DlmsMessage();
		dm.read(ByteBuffer.wrap(relMsg));
		ByteBuffer apdu = dm.getApdu();
		byte tag = apdu.get();
		byte[] sendMsg = null;
		switch(tag){
		case (byte) 0xDA:
			break;
		case 0x60:
			sendMsg=handleAARQ(dm.getApdu().array());
			break;
		case (byte) 0xC0:
			sendMsg=handleGet(dm.getApdu().array());
			break;
		case (byte) 0xC3:
			break;
		default:
			break;
		}
		
		if(sendMsg != null){
			ctx.channel().writeAndFlush(sendMsg).addListener(new FutureMsgSendListener(this.client));
			System.out.println(ai.incrementAndGet());
		}
	}

	private byte[] handleGet(byte[] apdu) throws IOException {
		String frame=HexDump.toHex(apdu);
		
		String seq = frame.substring(4, 6);
		int  classId=Integer.parseInt(frame.substring(6, 10));
		String obis=frame.substring(10, 6*2+10);
		int attrId=Integer.parseInt(frame.substring(6*2+10, 6*2+10+2));
		StringBuffer sb=createApdu(seq,classId,obis,attrId);
		byte[] returnApdu = null;
		returnApdu = HexDump.toArray(sb.toString());
		return createMsg(returnApdu);
	}
	
	private StringBuffer createApdu(String seq, int classId, String obis,
			int attrId) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("C401").append(seq).append("00");
		if(classId==8 && "0000010000FF".equals(obis) && attrId==2){//时间
			DlmsData dd = new DlmsData();
			dd.setDlmsDateTime(new Date());
			sb.append(HexDump.toHex(dd.encode()));
		}else if(classId==7 && "0001180300FF".equals(obis) && attrId==2){//通道数据，只支持1号通道
			DlmsData time = new DlmsData();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			String dateTime=sdf.format(new Date())+":00:00";
			time.setDlmsDateTime(dateTime);
			sb.append("0101").append("02").append(HexDump.toHex((byte)(channelValue.size()+1)));
			sb.append(HexDump.toHex(time.encode()));
			for(int i=0;i<channelValue.size();i++){
				DlmsData value = new DlmsData();
				value.setDoubleLongUnsigned(channelValue.get(i));
				sb.append(HexDump.toHex(value.encode()));
			}
		
		}
		return sb;
	}
	private List<Integer> channelValue = new ArrayList<Integer>();

	private byte[] handleAARQ(byte[] apdu) throws IOException {
		AarqApdu aa = new AarqApdu();
		aa.decode(DecodeStream.wrap(apdu));
		AareApdu aare = new AareApdu();
		InitiateResponse resp = new InitiateResponse();
		resp.getMaxRecvPduSize().setValue(0x01f4);
		resp.getConformance().setInitValue(new byte[]{0,(byte)0x50,(byte)0x1F});
		aare.setInitResponse(resp);
		aare.getAaResult().setValue(0);
		aare.getDiagnostics()[0].setValue(0);
		aare.getDiagnostics()[0].setBerCodec();
		aare.getAaResultDiagnostic().choose(aare.getDiagnostics()[0]);
		aare.setMechanismName(AuthenticationMechanismName.LLS);
		byte[] encodes = aare.encode();
		return createMsg(encodes);
	}
	
	private short version = 0x0001;
	private short srcAddr = 0x0001;
	private short dstAddr = 0x0010;
	
	private byte[] createMsg(byte[] apdu){
		ByteBuffer buffer = ByteBuffer.allocate(apdu.length+6+2); 
		buffer.putShort(version);
		buffer.putShort(srcAddr);
		buffer.putShort(dstAddr);
		buffer.putShort((short) apdu.length);
		buffer.put(apdu);
		buffer.flip();
		return buffer.array();
	}
	
	private void setValue(String value){
		String[] values = value.split(";");
		for(int i=0;i<values.length;i++){
			channelValue.add(Integer.parseInt(values[i]));
		}
	}
}
