package cn.hexing.fk.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * related jar: jce.jar ,security/US_export_policy.jar ,security/local_policy.jar ,ext/sunjce_provider.jar 
 *
 */
public class DESEncryptUtil {
	public static Key createKey() throws NoSuchAlgorithmException {
		Security.insertProviderAt(new com.sun.crypto.provider.SunJCE(), 1);
		KeyGenerator generator = KeyGenerator.getInstance("DES");
		generator.init(new SecureRandom());
		Key key = generator.generateKey();
		return key;
	}

	public static Key getKey(InputStream is) {
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			return (Key) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static byte[] doEncrypt(Key key, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] raw = cipher.doFinal(data);
			return raw;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static byte[] doDecrypt(Key key, byte[] in) {//
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] raw = cipher.doFinal(in);
			return raw;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static String encrypt(String content) throws Exception{
		Key key = null;
		try{
			key = getKey(new FileInputStream("key.data"));
		}catch(Exception e){
			key = DESEncryptUtil.createKey();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("key.data"));
			oos.writeObject(key);
			oos.close();
			System.out.println("key file generated.");
		}
		byte[] raw = DESEncryptUtil.doEncrypt(key, content.getBytes());
		String result = byte2hex(raw);
		System.out.println("\tEncrypt result: "+result);
		return result;
	}
	
	public static String decrypt(String hexString) throws Exception{
		Key key = null;
		try{
			key = getKey(new FileInputStream("key.data"));
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		byte[] raw = DESEncryptUtil.doDecrypt(key, hex2bytes(hexString));
		String result = new String(raw);
		System.out.println("\tDecrypt hex result:"+byte2hex(raw)+" string result="+result);
		return result;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
	public static byte[] hex2bytes(String hexString){
		hexString = hexString.replaceAll(":","");
		byte[] result = new byte[hexString.length()/2];
		for(int i=0; i<result.length; i++ ){
			String sub = hexString.substring(i*2,i*2+2);
			result[i] = (byte)Integer.parseInt(sub, 16);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("===================");
		if (args.length >= 2 ) {
			Key key = null;
			int index = 0;
			if( args[index].equals("-key") ){
				index++;
				key = getKey(new FileInputStream(args[index]));
				index++;
			}
			boolean encrypt = false, decrypt = false;
			byte[] contents = null;
			String dataFile = null;
			if( args[index].equals("-encrypt")){
				encrypt = true;
				index++;
				if( args[index].equals("-f")){
					index++;
					dataFile = args[index]; 
				}
				else{
					contents = args[index].getBytes();
				}
			}
			if( args[index].equals("-decrypt")){
				decrypt = true;
				index++;
				if( args[index].equals("-f")){
					index++;
					dataFile = args[index]; 
				}
				else
					contents = hex2bytes(args[index]);
			}
			if( null == key ){
				try{
					key = getKey(new FileInputStream("key.data"));
				}catch(Exception e){
					key = DESEncryptUtil.createKey();
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("key.data"));
					oos.writeObject(key);
					oos.close();
					System.out.println("key file generated.");
				}
			}
			if( null != dataFile ){
				FileInputStream in = new FileInputStream(dataFile);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				byte[] tmpbuf = new byte[1024];
				int count = 0;
				while ((count = in.read(tmpbuf)) != -1) {
					bout.write(tmpbuf, 0, count);
					tmpbuf = new byte[1024];
				}
				in.close();
				contents = bout.toByteArray();
			}
			if( encrypt ){
				byte[] raw = DESEncryptUtil.doEncrypt(key, contents);
				System.out.println("\tEncrypt result:");
				System.out.println(byte2hex(raw));
			}
			if( decrypt ){
				byte[] raw = DESEncryptUtil.doDecrypt(key, contents);
				System.out.println("\tDecrypt result:");
				System.out.println(byte2hex(raw));
				System.out.println(new String(raw));
			}
		}
		else if (args.length == 0 || (args.length == 1 && args[0].equals("-h"))) {
			System.out.println("\t Encrypt :");
			System.out.println("java -jar dbService.jar [-key key.dat] -encrypt [-f filename]['contents string'] ");
			System.out.println("\n");
			System.out.println("\t Decrypt :");
			System.out.println("java -jar dbService.jar [-key key.dat] -decrypt [-f filename]['contents string'] ");
		}
	}
}