package com.hx.dlms;

public class ASN1UnsignedInt8 extends ASN1Integer {
	private static final long serialVersionUID = 8376706412631378830L;

	public ASN1UnsignedInt8(){
		super();
		fixedLength(1);
		unsigned = true;
	}
	
	public ASN1UnsignedInt8(int initValue){
		super(initValue);
		fixedLength(1);
		unsigned = true;
	}
}
