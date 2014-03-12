package com.hx.dlms.aa;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1BitString;
import com.hx.dlms.ASN1Boolean;
import com.hx.dlms.ASN1Constants;
import com.hx.dlms.ASN1Int8;
import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.ASN1UnsignedInt16;
import com.hx.dlms.ASN1UnsignedInt8;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.EncodeStream;
import com.hx.dlms.TagAdjunct;

public class InitiateRequest extends ASN1Sequence {
	private static final long serialVersionUID = 4136782642103372927L;
	private final ASN1OctetString dedicatedKey = new ASN1OctetString();
	private final ASN1Boolean responseAllowed = new ASN1Boolean();
	private final ASN1Int8 proposedQualityOfService = new ASN1Int8();
	private final ASN1UnsignedInt8 proposedDlmsVersion = new ASN1UnsignedInt8();
	private final ASN1BitString proposedConformance = new ASN1BitString();
	private final ASN1UnsignedInt16 maxRecvPduSize = new ASN1UnsignedInt16();
	
	public InitiateRequest(){
		super(ASN1Constants.TAG_SEQUENCE);
		this.setAxdrCodec();
		//Choice of APDU: [1], must encode tag.
		this.setTagAdjunct(TagAdjunct.primitiveImplicit(1));
		this.forceEncodeTag(true);
		int c = this.getTagAdjunct().getTagClass();
		if( (c | ASN1Constants.PC_CONSTRUCTED) !=0 ){
			this.getTagAdjunct().setTagClass(c ^ ASN1Constants.PC_CONSTRUCTED);
		}
		
		members = new ASN1Type[6];
		proposedConformance.setInitValue(new byte[]{ (byte)0x00,(byte)0xBC,(byte)0x1F});
		members[0] = dedicatedKey.setOptional(true);
		responseAllowed.setOptional(true);
		members[1] = responseAllowed.setDefaultValue(true);
		proposedQualityOfService.setTagAdjunct(TagAdjunct.contextSpecificImplicit(0));
		members[2] = proposedQualityOfService.setOptional(true);
		proposedDlmsVersion.setValue(6);
		members[3] = proposedDlmsVersion;
		proposedConformance.setBerCodec().setTagAdjunct(TagAdjunct.applicationImplicit(31));
		members[4] = proposedConformance;
		maxRecvPduSize.setValue(1200);
		members[5] = maxRecvPduSize;
	}
	
	@Override
	public void encode(EncodeStream output) throws IOException{
		prepare();
		super.encode(output);
	}
	
	protected void prepare(){
		for(int i=0; i<members.length; i++ )
			members[i].codec(this.codec());

		proposedConformance.setBerCodec();
	}

	public final ASN1OctetString getDedicatedKey() {
		return dedicatedKey;
	}

	public final ASN1Boolean getResponseAllowed() {
		return responseAllowed;
	}

	public final ASN1Int8 getProposedQualityOfService() {
		return proposedQualityOfService;
	}

	public final ASN1UnsignedInt8 getProposedDlmsVersion() {
		return proposedDlmsVersion;
	}

	public final ASN1BitString getProposedConformance() {
		return proposedConformance;
	}

	public final ASN1UnsignedInt16 getMaxRecvPduSize() {
		return maxRecvPduSize;
	}
	
	public static void main(String[] args){
		InitiateRequest req = new InitiateRequest();
		byte[] codes = new byte[0];
		try {
			codes = req.encode();
			//result should be:01 00 00 00 06 5F 04 00 00 BC 1F 04 B0
			//      Actual is :01 00 00 00 06 7F 1F 04 00 00 BC 1F 04 B0
			System.out.println("initiateRequest="+HexDump.hexDump(codes, 0, codes.length));
			String s = "01000000067F1F040000BC1F04B0";
			ByteBuffer buf = HexDump.toByteBuffer(s);
			DecodeStream decoder = new DecodeStream(buf);
			req.decode(decoder);
			buf = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
