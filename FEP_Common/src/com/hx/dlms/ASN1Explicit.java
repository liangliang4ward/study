package com.hx.dlms;

import java.io.IOException;

public class ASN1Explicit extends ASN1Type {
	private static final long serialVersionUID = 4574068368947722135L;
	private final ASN1Type refType;

	public static final ASN1Explicit newApplication(int appNum,ASN1Type type){
		return new ASN1Explicit(ASN1Constants.CLASS_APPLICATION,appNum,type);
	}
	public static final ASN1Explicit newContextSpec(int appNum,ASN1Type type){
		return new ASN1Explicit(ASN1Constants.CLASS_CONTEXTSPECIFIC,appNum,type);
	}
	
	public ASN1Explicit(int tagNum, ASN1Type type){
		this(ASN1Constants.CLASS_CONTEXTSPECIFIC,tagNum,type);
	}
	
	public ASN1Explicit(int tagClass,int tagNum, ASN1Type type){
		super(tagClass,tagNum,ASN1Constants.PC_CONSTRUCTED,-1,false);
		refType = type;
		//type is not CHOICE or ANY
	}

	@Override
	public void encodeTag(EncodeStream output) throws IOException{
		value = refType.encode();
		super.encodeTag(output);
	}
	
	@Override
	public void encodeContent(EncodeStream output) throws IOException{
		if( null == value )
			value = refType.encode();
		super.encodeContent(output);
	}

	@Override
	public boolean decodeContent(DecodeStream input) throws IOException{
		boolean ret = super.decodeContent(input);
		if( ret ){
			ret = refType.decode( new DecodeStream(value));
			assert ret;
		}
		return ret;
	}

}
