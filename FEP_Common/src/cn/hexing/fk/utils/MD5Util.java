package cn.hexing.fk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
 * @Description  计算文件的MD5值
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-2-17 上午10:05:18
 * @version 1.0 
 */

public class MD5Util {

	public static char hexDigits[]={ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	public static MessageDigest messageDigest=null;
	 	static{
	 		try{
	 			messageDigest=MessageDigest.getInstance("MD5");
	 		}catch(NoSuchAlgorithmException nsaex){
	 		 System.err.println(MD5Util.class.getName()  
	 				 				+ "初始化失败，MessageDigest不支持MD5Util。");  
	 		 nsaex.printStackTrace(); 
	 		}
	 	}
	/**
	 * 生成字符串的MD5检验值
	 * 
	 */
	 	public  static String getMD5String(String s){
	 		return getMD5String(HexDump.toArray(s));
	 	}
	 	
	 	/**
	 	 * 校验字符串的MD5码是否与已知的MD5码匹配
	 	 * @param password
	 	 * @param MD5Password
	 	 * @return
	 	 */
	 	public static boolean checkPassword(String password,String MD5Password){
	 		String s=getMD5String(password);
	 		return s.equals(MD5Password);
	 	}
	 	
	 	/**
	 	 * 生成文件的MD5值
	 	 * @param file
	 	 * @return
	 	 * @throws IOException
	 	 */
	 	
	 	public static String getFileMD5String(File file) throws IOException {
	 		FileInputStream fis;
	 		fis=new FileInputStream(file);
	 		byte buffer[]=new byte[1024];
	 		int numRead=0;
	 		while((numRead=fis.read(buffer))>0){
	 			messageDigest.update(buffer,0,numRead);
	 		}
	 		fis.close();
	 		return bufferToHex(messageDigest.digest());
	 		
	 		
	 	}
	 	
	public static String getMD5String(byte[] bytes) {
		messageDigest.update(bytes);
		return bufferToHex(messageDigest.digest());
	}
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes,0,bytes.length);
	}
	private static String bufferToHex(byte[] bytes, int i, int length) {
		StringBuffer sb=new StringBuffer(2*length);
		int k=i+length;
		for(int l=i;l<k;l++){
			appendHexPair(bytes[l],sb);
		}
		return sb.toString();
	}
	private static void appendHexPair(byte b, StringBuffer sb) {
		char c0=hexDigits[(b&0xf0)>>4];
		char c1=hexDigits[b&0x0f];
		sb.append(c0);
		sb.append(c1);
	} 
	//测试
	public static void main(String[] args) {
		File file=new File("C:/Users/Administrator.DADI-20120822MM/Desktop/ddd");
		String md5 = null;
		try {
			md5 = getFileMD5String(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(md5);
		System.out.println(md5.length());
		System.out.println(MD5Util.getMD5String("123456123456000000123456").toUpperCase());
	}
	
	
}
