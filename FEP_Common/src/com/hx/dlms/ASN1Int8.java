package com.hx.dlms;

public class ASN1Int8 extends ASN1Integer {
	private static final long serialVersionUID = -5327720934024061381L;
	public ASN1Int8(){
		super();
		fixedLength(1);
	}
	public ASN1Int8(int initValue){
		super(initValue);
		fixedLength(1);
	}
}
