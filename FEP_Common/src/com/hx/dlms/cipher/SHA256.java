package com.hx.dlms.cipher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.HexDump;

public class SHA256 {
	private static final Logger log = Logger.getLogger(SHA256.class);

	public static final byte[] digest( byte[] content ){
		try {
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
			msgDigest.update( content );
			return msgDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			log.error("SHA256 digest error.", e);
			return null;
		}
	}
	
	public static final String digestAsString( byte[] content){
		byte[] result = digest( content );
		if( null == result )
			return "";
		return HexDump.toHex(result);
	}
	
	public static void main(String[] args) {
		String str = "abcd";
		String expected = "88D4266FD4E6338D13B845FCF289579D209C897823B9217DA3E161936F031589";
		System.out.println("string="+str+" digest:");
		System.out.println(digestAsString( str.getBytes() ));
		System.out.println("expected: ");
		System.out.println(expected);
	}

}
