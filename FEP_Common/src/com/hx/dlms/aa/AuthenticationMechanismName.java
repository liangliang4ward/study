package com.hx.dlms.aa;

import com.hx.dlms.ASN1Oid;

public class AuthenticationMechanismName extends ASN1Oid {
	private static final long serialVersionUID = -2650844246684407525L;
	private static final String[] MECHANISM_NAME = new String[]{
		"NO_AUTHENTICATION","LLS(1)","HLS_2(2)","HLS_MD5(3)","HLS_SHA1(4)","HLS_GMAC(5)","UNKNOW"
	};
	//lowest_level_security_mechanism_name
	public static final AuthenticationMechanismName NO_AUTHENTICATION = 
			new AuthenticationMechanismName();//("{2.16.756.5.8.2.0}");
	//low_level_security_mechanism_name
	public static final AuthenticationMechanismName LLS = 
			new AuthenticationMechanismName("{2.16.756.5.8.2.1}");
	//high_level_security_mechanism_name
	//  the method of processing the challenge is secret
	public static final AuthenticationMechanismName HLS_2 = 
			new AuthenticationMechanismName("{2.16.756.5.8.2.2}");
	//high_level_security_mechanism_name_using_MD5
	public static final AuthenticationMechanismName HLS_MD5 = 
			new AuthenticationMechanismName("{2.16.756.5.8.2.3}");
	//high_level_security_mechanism_name_using_SHA-1
	public static final AuthenticationMechanismName HLS_SHA1 = 
			new AuthenticationMechanismName("{2.16.756.5.8.2.4}");
	//High_Level_Security_Mechanism_Name_Using_GMAC
	public static final AuthenticationMechanismName HLS_GMAC = 
			new AuthenticationMechanismName("{2.16.756.5.8.2.5}");

	public static final AuthenticationMechanismName createNoAuthentication(){
		return new AuthenticationMechanismName();//("{2.16.756.5.8.2.0}");
	}

	public AuthenticationMechanismName(){
		super();
	}
	
	public AuthenticationMechanismName(String stroid){
		super(stroid);
	}
	
	public boolean isAuthenticationHLSGMAC(){
		return getMechanismNameId() == 5;
	}
	
	public boolean isNoAuthentication(){
		return getMechanismNameId() == 0;
	}
	
	public int getMechanismNameId(){
		int id = -1;
		if( null == oid )
			id = 0;
		else if( oid.length == 7 )
			id = oid[6];
		else
			id = -1;
		return id;
	}
	
	public String toString(){
		int id = getMechanismNameId();
		if( id>=0 && id<MECHANISM_NAME.length-1 )
			return MECHANISM_NAME[id];
		return MECHANISM_NAME[MECHANISM_NAME.length-1] + "(" + id + ")";
	}
}
