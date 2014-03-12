package test.hx.dlms.asn;

import java.nio.ByteBuffer;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.DlmsData;

public class DlmsFloatTest {
	
	private static final void testToken(){
		String token = "67379149057961958218";
		DlmsData data = new DlmsData();
		data.setBcdStringAsOctets(token);
		System.out.println(HexDump.toHex(data.getValue()));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testToken();
		//47 72 68 00
		byte[] float1Bytes = new byte[]{ 0x47,0x72,0x68,0x00 };
		float f = ByteBuffer.wrap(float1Bytes).getFloat();
		System.out.println("float="+f);
	}

}
