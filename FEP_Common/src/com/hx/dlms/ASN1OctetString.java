package com.hx.dlms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ASN1OctetString extends ASN1Type {
	private static final long serialVersionUID = 1561673472159826574L;

	public ASN1OctetString(){
		this(ASN1Constants.TAG_OCTETSTRING,-1);
	}
	
	public ASN1OctetString(int size){
		this(ASN1Constants.TAG_OCTETSTRING,size);
	}
	
	protected ASN1OctetString(int tagNum,int fixedLen){
		super(tagNum,fixedLen);
	}
	
	public ASN1OctetString(byte[] octValue){
		this();
		value = octValue;
	}
	
	public ASN1OctetString(int size,byte[] octValue){
		this(size);
		value = octValue;
	}

	public void setValue(byte[] initValue){
		value = initValue;
	}
	
	public byte[] getValue(){
		return value;
	}

	@Override
	protected void onDecodeConstructedComplete(DecodeStream _input) {
		DecodeStream input = DecodeStream.wrap(ByteBuffer.wrap(value));
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(value.length);
		try {
			while( input.available()>2 ){
				ASN1OctetString octs = new ASN1OctetString();
				octs.decode(input);
				outStream.write(octs.value);
			}
			value = outStream.toByteArray();
			length = value.length;
		} catch (IOException e) {
		}
	}

}
