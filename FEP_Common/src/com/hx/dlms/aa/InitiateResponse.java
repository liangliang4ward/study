package com.hx.dlms.aa;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1BitString;
import com.hx.dlms.ASN1Int16;
import com.hx.dlms.ASN1Int8;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.ASN1UnsignedInt16;
import com.hx.dlms.ASN1UnsignedInt8;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.EncodeStream;
import com.hx.dlms.TagAdjunct;

public class InitiateResponse extends ASN1Sequence {
	private static final long serialVersionUID = -9163888844249806031L;
	//Negotiated quality of service
	private final ASN1Int8 qualityOfService = new ASN1Int8();
	//Negotiated DLMS version number
	private final ASN1UnsignedInt8 dlmsVersion = new ASN1UnsignedInt8();
	//negotiated conformance.
	private final ASN1BitString conformance = new ASN1BitString();
	private final ASN1UnsignedInt16 maxRecvPduSize = new ASN1UnsignedInt16(500);
	private final ASN1Int16 vaaName = new ASN1Int16(0x0007);

	public InitiateResponse(){
		super(TAG_SEQUENCE);
		this.setAxdrCodec();
		//Choice of APDU: [8], must encode tag.
		this.setTagAdjunct(TagAdjunct.primitiveImplicit(8));
		this.forceEncodeTag(true);
		int c = this.getTagAdjunct().getTagClass();
		if( (c | PC_CONSTRUCTED) !=0 ){
			this.getTagAdjunct().setTagClass(c ^ PC_CONSTRUCTED);
		}
		
		members = new ASN1Type[5];
		qualityOfService.setTagAdjunct(TagAdjunct.contextSpecificImplicit(0));
		members[0] = qualityOfService.setOptional(true);
		dlmsVersion.setValue(6);
		members[1] = dlmsVersion;
		conformance.setTagAdjunct(TagAdjunct.applicationImplicit(31));
		conformance.setBerCodec();
		members[2] = conformance;
		maxRecvPduSize.setValue(1200);
		members[3] = maxRecvPduSize;
		members[4] = vaaName;
	}

	@Override
	public void encode(EncodeStream output) throws IOException{
		prepare();
		super.encode(output);
	}
	
	protected void prepare(){
		for(int i=0; i<members.length; i++ )
			members[i].codec(this.codec());

		conformance.setBerCodec();
	}

	public final ASN1Int8 getQualityOfService() {
		return qualityOfService;
	}

	public final ASN1UnsignedInt8 getDlmsVersion() {
		return dlmsVersion;
	}

	public final ASN1BitString getConformance() {
		return conformance;
	}

	public final ASN1UnsignedInt16 getMaxRecvPduSize() {
		return maxRecvPduSize;
	}

	public final ASN1Int16 getVaaName() {
		return vaaName;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder(512);
		sb.append(" (InitiateResponse: qualityOfService=");
		if( qualityOfService.getValue() == null )
			sb.append("not present");
		else
			sb.append(qualityOfService.getInt());
		sb.append("; dlmsVersion=");
		if( dlmsVersion.getValue() == null)
			sb.append("not present");
		else
			sb.append(dlmsVersion.getInt());
		DlmsMeterConformance cf = new DlmsMeterConformance(conformance);
		cf.setMaxPduSize(maxRecvPduSize.getInt());
		sb.append("; conformance=").append(cf);
		//sb.append("; maxRecvPduSize=");
		sb.append("; vaaName=");
		if( vaaName.getValue() == null)
			sb.append("not present");
		else
			sb.append(vaaName.getInt());
		sb.append(" )");
		return sb.toString();
	}
	
	
	public static void main(String[] args){
		InitiateResponse rep = new InitiateResponse();
		rep.getDlmsVersion().setValue(6);
		rep.getMaxRecvPduSize().setValue(500);
		rep.getConformance().setInitValue(new byte[]{ (byte)0x00,(byte)0x50,(byte)0x1F});
		byte[] codes = new byte[0];
		try {
			codes = rep.encode();
			//result should be:08 00 06 5F 1F 04 00 00 50 1F 01 F4 00 07
			//      Actual is :08 00 06 5F 1F 04 00 00 50 1F 01 F4 00 07
			System.out.println("InitiateResponse="+HexDump.hexDump(codes, 0, codes.length));
			String s = "0800065F1F040000501F01F40007";
			ByteBuffer buf = HexDump.toByteBuffer(s);
			DecodeStream decoder = new DecodeStream(buf);
			rep = new InitiateResponse();
			rep.decode(decoder);
			buf = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
