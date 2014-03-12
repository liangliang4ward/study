package com.hx.dlms;

import java.io.UnsupportedEncodingException;

public class ASN1String extends ASN1OctetString {
	private static final long serialVersionUID = 3743625177034531245L;

	/**
	 * PrintableString is subset of ASCII, only include: 32,39,40~41,43~58,61,63,65~122
	 * IA5String contain more than PrintableString, including NULL,BEL,TAB,NL,LF,CR,32~126.
	 * @return
	 */
	//IA5STRING type 
	public static final ASN1String IA5String(){
		return new ASN1String(ASN1Constants.TAG_IA5STRING);
	}
	public static final ASN1String IA5String(int fixedSize){
		return new ASN1String(ASN1Constants.TAG_IA5STRING,fixedSize);
	}
	
	//Visible String
	public static final ASN1String VisibleString(){
		return new ASN1String(ASN1Constants.TAG_VISIBLESTRING);
	}
	public static final ASN1String VisibleString(int fixedSize){
		return new ASN1String(ASN1Constants.TAG_VISIBLESTRING,fixedSize);
	}
	
	public ASN1String(){
		this(ASN1Constants.TAG_VISIBLESTRING);
	}

	protected ASN1String(int tagNumber){
		this(tagNumber,-1);
	}
	
	protected ASN1String(int tagNumber,int fixedSize){
		super(tagNumber,fixedSize);
	}
	
	public ASN1String setString(String initValue){
		try {
			setValue(initValue.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public String getString(){
		try {
			return new String(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ASN1String setFixedSize(int fixedSize){
		fixedLength = fixedSize;
		return this;
	}
}
