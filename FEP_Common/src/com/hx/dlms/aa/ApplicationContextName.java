/**
 * In the COSEM environment, it is intended that an application context pre-exists and 
 * it is referenced by its name during the establishment of an AA. 
 * The application context name is specified as OBJECT IDENTIFIER ASN.1 type. 
 * COSEM identifies the application context name by the following object identifier value:
 * 
 * COSEM_Application_Context_Name ::= {joint-iso-ccitt(2) country(16) country-name(756)
 *     identified-organisation(5) DLMS-UA(8) application-context(1) context_id(x)}
 */
package com.hx.dlms.aa;

import com.hx.dlms.ASN1Oid;

public class ApplicationContextName extends ASN1Oid {
	private static final long serialVersionUID = -1270816997291121198L;

	private static final String[] staticContextName = new String[] {
		"invalid","LN_NO_CIPHERING(1)","SN_NO_CIPHERING(2)",
		"LN_WITH_CIPHERING(3)","SN_WITH_CIPHERING(4)"
	};
	
	public static final ApplicationContextName LN_NO_CIPHERING = 
			new ApplicationContextName("{2.16.756.5.8.1.1}");
	public static final ApplicationContextName LN_WITH_CIPHERING = 
			new ApplicationContextName("{2.16.756.5.8.1.3}");
	public static final ApplicationContextName SN_NO_CIPHERING = 
			new ApplicationContextName("{2.16.756.5.8.1.2}");
	public static final ApplicationContextName SN_WITH_CIPHERING = 
			new ApplicationContextName("{2.16.756.5.8.1.4}");

	public static final ApplicationContextName createNoCipher(){
		return new ApplicationContextName("{2.16.756.5.8.1.1}");
	}
	public static final ApplicationContextName createWithCipher(){ 
		return new ApplicationContextName("{2.16.756.5.8.1.3}");
	}
	
	public ApplicationContextName( ){
		super();
	}
	
	public ApplicationContextName( String strOID ){
		super(strOID);
	}
	
	public ApplicationContextName(int[] contextNameOID ){
		super();
		if( ! validate(contextNameOID))
			throw new RuntimeException("COSEM_Application_Context_Name not valid.");
		oid = contextNameOID;
	}
	
	public boolean isCipherEnabled(){
		int id = getContextId();
		return id == 3 || id == 4;
	}
	
	public int getContextId(){
		int id = 0;
		if( null != oid && oid.length == 7 )
			id = oid[6];
		return id;
	}
	
	@Override
	public String toString(){
		int id = getContextId();
		if( id == 0 )
			return staticContextName[id]+"(0)";
		if( id<0 || id>= staticContextName.length )
			return staticContextName[id]+"("+id+")";
		return staticContextName[id];
	}
}
