package com.hx.ansi.element;
/** 
 * @Description  AC--calling-authentication-value-element
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-3-21 ÉÏÎç09:26:28
 * @version 1.0 
 */

public class CallingAuthenticationValueElement implements Element{
	
	private String value_external;
	private String value_indirect_reference;
	private String value_single_asn1;
	private String value_asn1;//<calling-authentication-value-c1222> | 
							  //<calling-authentication-value-c1221> | 
							  //<calling-authentication-value-other-asn1>
	private String value_octet_aligned;
	
	
	
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	public String getValue_external() {
		return value_external;
	}
	public void setValue_external(String value_external) {
		this.value_external = value_external;
	}
	public String getValue_indirect_reference() {
		return value_indirect_reference;
	}
	public void setValue_indirect_reference(String value_indirect_reference) {
		this.value_indirect_reference = value_indirect_reference;
	}
	public String getValue_single_asn1() {
		return value_single_asn1;
	}
	public void setValue_single_asn1(String value_single_asn1) {
		this.value_single_asn1 = value_single_asn1;
	}
	public String getValue_asn1() {
		return value_asn1;
	}
	public void setValue_asn1(String value_asn1) {
		this.value_asn1 = value_asn1;
	}
	public String getValue_octet_aligned() {
		return value_octet_aligned;
	}
	public void setValue_octet_aligned(String value_octet_aligned) {
		this.value_octet_aligned = value_octet_aligned;
	}
	
	
	
	

}
