package com.hx.dlms;

public class ASN1Enum extends ASN1UnsignedInt8 {
	private static final long serialVersionUID = -4382371529353442011L;

	public ASN1Enum(){
		super();
		super.tagValue = ASN1Constants.TAG_ENUM;
	}
	
	/**
	 * Enum is unsigned8 type.
	 * enumOrder is inital value for encoding
	 * @param enumOrder
	 */
	public ASN1Enum(int enumOrder){
		super(enumOrder);
		super.tagValue = ASN1Constants.TAG_ENUM;
	}
	
	public int getEnumValue(){
		if( isDecodeDone() )
			return this.getUnsignedInt8();
		else
			return this.getInitValue();
	}
}
