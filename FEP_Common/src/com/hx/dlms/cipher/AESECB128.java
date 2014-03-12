package com.hx.dlms.cipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import cn.hexing.fk.utils.HexDump;

public class AESECB128 {
	private static final byte[] staticKey = HexDump.toByteBuffer("00000000000000000000000000000000").array();

	public static byte[] encrypt(byte[] plainText) {
		return encrypt(plainText,staticKey);
	}
	
	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param enckey
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(byte[] content, byte[] enckey) {
		try {
			SecretKeySpec key = new SecretKeySpec(enckey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(byte[] cipherText) {
		return decrypt(cipherText,staticKey);
	}
	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param deckey
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, byte[] deckey) {
		try {
			SecretKeySpec key = new SecretKeySpec(deckey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String content = "6789:;<=>?@AB012";
		String password = "00000000000000000000000000000000";
		byte[] mykey = HexDump.toByteBuffer(password).array();
		// 加密
		System.out.println("加密前：" + content);
		byte[] encryptResult = encrypt(content.getBytes(), mykey);
		System.out.println("cipher text: " + HexDump.hexDumpCompact(encryptResult,0,encryptResult.length));
		// 解密
		byte[] decryptResult = decrypt(encryptResult, mykey);
		System.out.println("解密后：" + new String(decryptResult));
	}

}
