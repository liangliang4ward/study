package com.hx.dlms.cipher;

public class CipherManager {
	private static CipherManager instance = null;
	public static final CipherManager getInstance(){
		if( null == instance )
			instance = new CipherManager();
		return instance;
	}
	
	private CipherManager(){}
	
	

}
