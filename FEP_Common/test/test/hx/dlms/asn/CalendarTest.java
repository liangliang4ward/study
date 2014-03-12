package test.hx.dlms.asn;

import java.io.IOException;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1GeneralizedTime;
import com.hx.dlms.ASN1Oid;
import com.hx.dlms.DecodeStream;

public class CalendarTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		testGeneralTime1();
//CPU¿ÕÏÐÖµµÄOIDÎª£º1.3.6.1.4.1.2011.11.11.0 , 2b 06 01 04 01 8f 65 0b 0b 00
//RSA Data Security, Inc: { 1 2 840 113549 }, 06062a864886f70d
		String oid = "1.3.6.1.4.1.2011.11.11.0";
		String berOidDecode = "06062a864886f70d";
		testOID(oid,berOidDecode);
	}
	
	private static void testOID(String strOid1,String berOid) throws IOException {
		ASN1Oid oid1 = new ASN1Oid(strOid1);
		oid1.setBerCodec();
		byte[] encoded = oid1.encode();	
		System.out.println("RAW="+strOid1);
		System.out.println("        BER="+ HexDump.hexDump(encoded, 0, encoded.length));
		oid1.setAxdrCodec();
		encoded = oid1.encode();
		System.out.println( "      A-XDR=   "+ HexDump.hexDump(encoded, 0, encoded.length));

		oid1.setBerCodec();
		DecodeStream input = DecodeStream.wrap(HexDump.toByteBuffer(berOid));
		oid1.decode(input);
		System.out.println("     BER.decode = "+oid1);
		oid1 = new ASN1Oid();
		oid1.setAxdrCodec();
		berOid = berOid.substring(2);
		input = DecodeStream.wrap(HexDump.toByteBuffer(berOid));
		oid1.decode(input);
		System.out.println("    AXDR.decode = "+oid1);
	}

	private static void testGeneralTime1() throws IOException{
		String strToEncode = "2012-06-06 10:07:34.12";
		ASN1GeneralizedTime gt = new ASN1GeneralizedTime(strToEncode);
		gt.setBerCodec();
		byte[] encoded = gt.encode();
		System.out.println("GENERALIZEDTIME ["+strToEncode+"] encoded="+HexDump.hexDumpCompact(encoded, 0,encoded.length));
		System.out.println("GMT time="+gt.getStringTime());
	}
}
