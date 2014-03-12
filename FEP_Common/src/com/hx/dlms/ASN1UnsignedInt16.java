package com.hx.dlms;

public class ASN1UnsignedInt16 extends ASN1Integer {
	private static final long serialVersionUID = 7940684993912532192L;

	public ASN1UnsignedInt16(){
		super();
		fixedLength(2);
		unsigned = true;
	}
	
	public ASN1UnsignedInt16(int initValue){
		super(initValue);
		fixedLength(2);
		unsigned = true;
	}
}
