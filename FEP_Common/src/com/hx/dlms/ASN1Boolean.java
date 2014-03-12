package com.hx.dlms;

public class ASN1Boolean extends ASN1Type {
	private static final long serialVersionUID = 379461664154545489L;

	public ASN1Boolean(){
		super(ASN1Constants.TAG_BOOLEAN);
		fixedLength(1);
	}
	
	public ASN1Boolean(boolean boolValue){
		super(ASN1Constants.TAG_BOOLEAN);
		setBoolValue(boolValue);
	}
	
	public void setBoolValue(boolean boolValue){
		value = new byte[1];
		if( boolValue )
			value[0] = -1;
		else
			value[0] = 0;
	}
	
	public boolean getBoolValue(){
		if( null == value || value.length==0 )
			throw new RuntimeException("ASN1Boolean is null, should decode first");
		if( value[0] == 0 )
			return false;
		else
			return true;
	}
}
