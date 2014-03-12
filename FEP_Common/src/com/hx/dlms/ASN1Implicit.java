package com.hx.dlms;

import java.io.IOException;

public class ASN1Implicit extends ASN1Type {
	private static final long serialVersionUID = 1595920221106114653L;
	private final ASN1Type refType;

	public static final ASN1Implicit newApplication(int appNum,ASN1Type type){
		return new ASN1Implicit(ASN1Constants.CLASS_APPLICATION,appNum,type);
	}
	public static final ASN1Implicit newContextSpec(int appNum,ASN1Type type){
		return new ASN1Implicit(ASN1Constants.CLASS_CONTEXTSPECIFIC,appNum,type);
	}
	
	public ASN1Implicit(int tagNum, ASN1Type type){
		this(ASN1Constants.CLASS_CONTEXTSPECIFIC,tagNum,type);
	}
	
	public ASN1Implicit(int tagClass,int tagNum, ASN1Type type){
		super(tagClass,tagNum,type.getConstructFlag(),-1,false);
		refType = type;
		fixedLength = refType.fixedLength ;
		//type is not CHOICE or ANY
	}
	
	@Override
	public void encodeTag(EncodeStream output) throws IOException{
		EncodeStream encoder = new EncodeStream();
		refType.encodeContent(encoder);
		value = encoder.dump();
		if( isAxdrCodec() ){
			//A-XDR treat context-specific as universal
			if( tagClass == ASN1Constants.CLASS_CONTEXTSPECIFIC )
				tagClass = ASN1Constants.CLASS_UNIVERSAL;
		}
		super.encodeTag(output);
	}
	
	@Override
	public boolean decodeTag(DecodeStream input) throws IOException{
		fixedLength = refType.fixedLength ;
		return super.decodeTag(input);
	}
	
	@Override
	public boolean decodeLength(DecodeStream input) throws IOException{
		boolean ret = super.decodeLength(input);
		if( ret ){
			refType.length = length;
			refType.decodeState = decodeState;
		}
		return ret;
	}
	
	public boolean decodeContent(DecodeStream input) throws IOException{
		boolean ret = super.decodeContent(input);
		if( ret ){
//			refType.value = value;
//			refType.decodeState = decodeState;
			refType.length = length;
			ret = refType.decodeContent( new DecodeStream(value));
		}
		return ret;
	}

	@Override
	public void encodeContent(EncodeStream output) throws IOException{
		if( null == value ){
			EncodeStream encoder = new EncodeStream();
			refType.encodeContent(encoder);
			value = encoder.dump();
		}
		super.encodeContent(output);
	}
}
