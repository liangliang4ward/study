package com.hx.dlms;

public class ASN1Int16 extends ASN1Integer {
	private static final long serialVersionUID = -7354815572087596720L;
	public ASN1Int16(){
		super();
		fixedLength(2);
	}
	public ASN1Int16(int initValue){
		super(initValue);
		fixedLength(2);
	}
}
