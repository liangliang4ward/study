package cn.hexing.meter.mock.factory;

import java.nio.ByteBuffer;

import cn.hexing.meter.mock.spi.Attribute;
import cn.hexing.meter.mock.spi.AttributeOption;

public class DlmsMessageCreator implements MessageCreator{

	@Override
	public byte[] createHeartBeat(Attribute attr) {
		if(attr==null || !attr.contains(AttributeOption.METER_ID)) return null;
		String logicAddress=(String) attr.get(AttributeOption.METER_ID);
		ByteBuffer heart=ByteBuffer.allocate(26);
		//--------帧头------------
		heart.putShort((short) 0x0001);
		heart.putShort((short) 0x0001);
		heart.putShort((short) 0x0010);
		//--------帧头------------
		heart.putShort((short)0x0012); //长度
		heart.put((byte) 0xDD);
		heart.put((byte)0x10);
		heart.put((byte)0x00);
		heart.put((byte)0x00);
		heart.put((byte)0x00);
		heart.put((byte)0x00);
		byte[] b_logic=logicAddress.getBytes();
		heart.put(b_logic);
		heart.flip();	
		return heart.array();
	}

}
