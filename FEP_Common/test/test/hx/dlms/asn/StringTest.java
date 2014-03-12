package test.hx.dlms.asn;

import java.io.IOException;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1BitString;
import com.hx.dlms.ASN1Explicit;
import com.hx.dlms.ASN1Implicit;
import com.hx.dlms.ASN1String;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;

public class StringTest {
	public static void main(String[] args) {
		try {
			new StringTest().runTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void runTest() throws IOException{
		String init = "Jones";
		String s1 = "1A054A6F6E6573";
		testType1(init,s1);
		String s2 = "43054A6F6E6573";
		testType2(init,s2);
		String s3 = "A20743054A6F6E6573";
		testType3(init,s3);
		String s4 = "670743054A6F6E6573";
		testType4(init,s4);
		String s5 = "82054A6F6E6573";
		testType5(init,s5);
		String bitString = "011011100101110111";
		String bitStringDecode = "0304066E5DC0";
		testBitString(bitString,bitStringDecode,false);
		String bitString2 = "011011100101110111";
		for(int i=0; i<4; i++ )
			bitString2 += bitString2;
//03 26 08 6E 5D DB 97 76 E5 DD B9 77 6E 5D DB 97 76 E5 DD B9 77 6E 5D DB 97 76 E5 DD B9 77 6E 5D DB 97 76 E5 DD B9 77 00
		String bitStringDecode2 = "126E5DC0";	//BER code: 0304066E5DC0
		testBitString(bitString2,bitStringDecode2,true);
	}

	private void testBitString(String bitString,String tobeDecode, boolean isAxdr) throws IOException{
		boolean[] bits = ASN1BitString.toBoolean(bitString);

		ASN1BitString t1 = new ASN1BitString(bits);
		if( isAxdr )
			t1.setAxdrCodec();
		else
			t1.setBerCodec();
		byte[] encoded = t1.encode();
		System.out.println("bitstring encoded: "+HexDump.hexDump(encoded,0,encoded.length));
		t1.decode(DecodeStream.wrap(tobeDecode));
		System.out.println("BITSTRING decoded: "+t1);
	}
	
	private void testType1(String s,String decs) throws IOException{
		ASN1String t1 = ASN1String.VisibleString().setString(s);
		t1.setBerCodec();
		
		byte[] encoded = t1.encode();
		System.out.println("type1 encoding: "+HexDump.hexDump(encoded,0,encoded.length));
		t1.setValue(null);
		t1.decode(DecodeStream.wrap(decs));
		System.out.println("type1 decoding: "+t1.getString());
	}
	
	private void testType2(String s,String decs) throws IOException{
		ASN1String t1 = ASN1String.VisibleString().setString(s);
		t1.setBerCodec();
		ASN1Type t2 = ASN1Implicit.newApplication(3,t1);
		t2.setBerCodec();
		
		byte[] encoded = t2.encode();
		System.out.println("type2 encoding: "+HexDump.hexDump(encoded,0,encoded.length));
		t1.setValue(null);
		t2.setValue(null);
		t2.decode(DecodeStream.wrap(decs));
		System.out.println("type2 decoding: "+t1.getString());
	}

	private void testType3(String s,String decs) throws IOException{
		ASN1String t1 = ASN1String.VisibleString().setString(s);
		t1.setBerCodec();
		ASN1Type t2 = ASN1Implicit.newApplication(3,t1);
		t2.setBerCodec();
		ASN1Type t3 = ASN1Explicit.newContextSpec(2, t2);
		t3.setBerCodec();
		
		byte[] encoded = t3.encode();
		System.out.println("type3 encoding: "+HexDump.hexDump(encoded,0,encoded.length));
		t1.setValue(null);
		t3.setValue(null);
		t3.decode(DecodeStream.wrap(decs));
		System.out.println("type3 decoding: "+t1.getString());
	}

	private void testType4(String s,String decs) throws IOException{
		ASN1String t1 = ASN1String.VisibleString().setString(s);
		t1.setBerCodec();
		ASN1Type t2 = ASN1Implicit.newApplication(3,t1);
		t2.setBerCodec();
		ASN1Type t3 = ASN1Explicit.newContextSpec(2, t2);
		t3.setBerCodec();
		ASN1Type t4 = ASN1Implicit.newApplication(7,t3);
		t4.setBerCodec();
		
		byte[] encoded = t4.encode();
		System.out.println("type4 encoding: "+HexDump.hexDump(encoded,0,encoded.length));
		t1.setValue(null);
		t4.setValue(null);
		t4.decode(DecodeStream.wrap(decs));
		System.out.println("type4 decoding: "+t1.getString());
	}


	private void testType5(String s,String decs) throws IOException{
		ASN1String t1 = ASN1String.VisibleString().setString(s);
		t1.setBerCodec();
		ASN1Type t2 = ASN1Implicit.newApplication(3,t1);
		t2.setBerCodec();
		ASN1Type t5 = ASN1Implicit.newContextSpec(2, t2);
		t5.setBerCodec();
		
		byte[] encoded = t5.encode();
		System.out.println("type5 encoding: "+HexDump.hexDump(encoded,0,encoded.length));
		t1.setValue(null);
		t5.setValue(null);
		t5.decode(DecodeStream.wrap(decs));
		System.out.println("type5 decoding: "+t1.getString());
	}
}
