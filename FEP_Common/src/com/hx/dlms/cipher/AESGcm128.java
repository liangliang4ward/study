package com.hx.dlms.cipher;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.Tables64kGCMMultiplier;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;


public class AESGcm128 {
	public static int AUTHENTICATION_TAG_LENGTH = 12;

	/**
	 * GCM encryption.
	 * @param encryptKey: encryption AES key
	 * @param initVector: IV, initialization vector
	 * @param plainText: P: plaintext
	 * @param associatedText: associated data, which only used for caculating authentication TAG.
	 * @return : ciphered text, with authenticatin TAG. 
	 * @throws InvalidCipherTextException 
	 * @throws
	 */
	public static final byte[] encrypt(byte[] encryptKey, byte[] initVector, byte[] plainText, byte[] associatedText) throws InvalidCipherTextException{
		if( null == associatedText )
			associatedText = CipherUtil.NULL_KEY;
		boolean encryptOnly = associatedText.length == 0;
		
		GCMMultiplier encryptMultiplier = new Tables64kGCMMultiplier();
        AEADParameters parameters = new AEADParameters(new KeyParameter(encryptKey), AUTHENTICATION_TAG_LENGTH * 8, initVector, associatedText);
        
        GCMBlockCipher cipher = new GCMBlockCipher(new AESFastEngine(),encryptMultiplier);
        cipher.init(true, parameters);
        
        int outlen = cipher.getOutputSize(plainText.length);
        byte[] out = new byte[outlen];
        int len = cipher.processBytes(plainText,0,plainText.length, out, 0);
       	len += cipher.doFinal(out, len);
       	
        if( encryptOnly ){
        	byte[] encOut = new byte[outlen - AUTHENTICATION_TAG_LENGTH];
			System.arraycopy(out, 0, encOut, 0, encOut.length);
			out = encOut;
        }
        return out;
	}
	
	/**
	 * 
	 * @param encryptKey : AES encryption key
	 * @param initVector : initialization vector
	 * @param cipherText : ciphered text
	 * @param associatedText : associated data
	 * @return : plaintext.
	 * @throws InvalidCipherTextException
	 */
	public static final byte[] decrypt(byte[] encryptKey, byte[] initVector, byte[] cipherText, byte[] associatedText) throws InvalidCipherTextException{
		boolean decryptOnly = false;
		if( null == associatedText || associatedText.length == 0){
			decryptOnly = true;
			associatedText = CipherUtil.NULL_KEY;
			cipherText = CipherUtil.cat(cipherText,CipherUtil.NULL_MAC);
		}

		GCMMultiplier decryptMultiplier = new Tables64kGCMMultiplier();
        AEADParameters parameters = new AEADParameters(new KeyParameter(encryptKey), AUTHENTICATION_TAG_LENGTH * 8, initVector, associatedText);
        
        GCMBlockCipher cipher = new GCMBlockCipher(new AESFastEngine(),decryptMultiplier);
        cipher.init(false, parameters);

        byte[] out = new byte[cipher.getOutputSize(cipherText.length)];
        int len = cipher.processBytes(cipherText,0,cipherText.length, out, 0);
        try{
        	len += cipher.doFinal(out, len);
        }
        catch(InvalidCipherTextException exp){
//        	System.out.println(exp.getLocalizedMessage());
        }
        if( decryptOnly ){
        	
        }
        return out;
	}
	
	private static void testBengal(){
		System.out.println("Test Bengal ciphering...");
		//initialize vector
		byte[] IV = Hex.decode("BB23760AB1D61D27E26D794B");
		
		//encryption key
		byte[] EK = CipherUtil.ZERO_KEY;
		
		//plaintext
//		byte[] P = Hex.decode("01011000112233445566778899AABBCCDDEEFF0000065F1F0400007E1F04B0");
		
		//association data
		byte[] A = Hex.decode("");
		
		//ciphered text
		String cipherText = "7EE2549DAF9DC78A0E2468E6E75F4F3EAC";
		byte[] C = Hex.decode(cipherText);
		
		try {
			System.out.println("Cipher Text: "+cipherText);
			byte[] ptext = decrypt(EK,IV,C,A);
			System.out.println("Plain  Text: " + new String(Hex.encode(ptext)));
			
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//initialize vector
		byte[] IV = Hex.decode("4D4D4D0000BC614E01234567");
		
		//encryption key
		byte[] EK = Hex.decode("000102030405060708090A0B0C0D0E0F");
		
		//plaintext
		byte[] P = Hex.decode("01011000112233445566778899AABBCCDDEEFF0000065F1F0400007E1F04B0");
		
		//association data
		byte[] A = Hex.decode("30D0D1D2D3D4D5D6D7D8D9DADBDCDDDEDF");
		
		//ciphered text
		byte[] C = Hex.decode("801302FF8A7874133D414CED25B42534D28DB0047720606B175BD52211BE6841DB204D39EE6FDB8E356855");

		try {
			byte[] cipherText =  encrypt(EK,IV,P,A);
			System.out.println(new String(Hex.encode(cipherText)));
			
			byte[] ptext = decrypt(EK,IV,C,A);
			System.out.println(new String(Hex.encode(ptext)));
			
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		}
		
		testBengal();
	}

}
