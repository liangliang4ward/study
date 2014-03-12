package test.hx.dlms.asn;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1Integer;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.ASN1UnsignedInt16;
import com.hx.dlms.ASN1UnsignedInt8;
import com.hx.dlms.DecodeStream;

public class BigIntegerTest {
	protected static void testBigInteger(int initValue){
		BigInteger bi = BigInteger.valueOf(initValue);
		byte[] dump = bi.toByteArray();
		System.out.println("HexDump="+HexDump.hexDumpCompact(dump,0, dump.length));
	}
	
	protected static void testASN1BerInteger(int initValue){
		ASN1Type i = new ASN1Integer(initValue);
		i.setBerCodec();
		try {
			byte[] encoded = i.encode();
			System.out.println("BER.Integer : "+ HexDump.hexDump(encoded, 0, encoded.length));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void testASN1Integer(int initValue){
		ASN1Type i = new ASN1Integer(initValue);
		try {
			byte[] encoded = i.encode();
			System.out.println("A-XDR.Integer : "+ HexDump.hexDump(encoded, 0, encoded.length));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void testUnsigned8(int initValue){
		ASN1Type i = new ASN1UnsignedInt8(initValue);
		try {
			byte[] encoded = i.encode();
			System.out.println("A-XDR.Unsigned8 : "+ HexDump.hexDump(encoded, 0, encoded.length));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void testUnsigned16(int initValue){
		ASN1Type i = new ASN1UnsignedInt16(initValue);
		try {
			byte[] encoded = i.encode();
			System.out.println("A-XDR.Unsigned16 : "+ HexDump.hexDump(encoded, 0, encoded.length));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void decodeBERInteger(String s){
		ByteBuffer buf = HexDump.toByteBuffer(s);
		DecodeStream decoder = new DecodeStream(buf);
		ASN1Integer asnInt = new ASN1Integer();
		asnInt.setBerCodec();
		try {
			asnInt.decode(decoder);
			System.out.println("BER.Integer.decode: str="+s+" ,value="+asnInt.getInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void decodeAXDRInteger(String s){
		ByteBuffer buf = HexDump.toByteBuffer(s);
		DecodeStream decoder = new DecodeStream(buf);
		ASN1Integer asnInt = new ASN1Integer();
		try {
			asnInt.decode(decoder);
			System.out.println("AXDR.Integer.decode: str="+s+" ,value="+asnInt.getInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void decodeAXDRUnsigned16(String s){
		ByteBuffer buf = HexDump.toByteBuffer(s);
		DecodeStream decoder = new DecodeStream(buf);
		ASN1UnsignedInt16 asnInt = new ASN1UnsignedInt16();
		try {
			asnInt.decode(decoder);
			System.out.println("AXDR.unsigned16.decode: str="+s+" ,value="+asnInt.getInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int x = 0xFAab;
		System.out.println("Encode test, initValue="+x);
		testBigInteger(x);
		testASN1BerInteger(x);
		testASN1Integer(x);
		testUnsigned8(x);
		testUnsigned16(x);
		
		String axdrS1 = "8300FAAB";
		String axdrS2 = "FAAB";
		String berS1 = "020300FAab";
		decodeAXDRInteger(axdrS1);
		decodeAXDRUnsigned16(axdrS2);
		decodeBERInteger(berS1);
	}

}
