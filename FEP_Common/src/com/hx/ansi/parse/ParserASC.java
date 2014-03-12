package com.hx.ansi.parse;



/**
 * 字符和其ASCII码十六进制值转换(无需倒置)
 *
 */
public class ParserASC {	
	/**
	 * ASCII->String
	 * @param data(ASCII) 		例如：
	 * @param len(字符数)			例如：
	 * @return String(String)	例如：
	 */
	public static String parseValue(String data,int len){
		String rt="";
		try{
			data=data.substring(0,len);
			if((data.length()%2)==0){
	          int byteLen=data.length()/2;  //字节长度
	          char[] chrList=new char[byteLen];
	          for (int i=0;i<byteLen;i++){
	            chrList[i]=(char)(Integer.parseInt(ParserHTB.parseValue(data.substring(2*i,2*i+2),2)));
	          }
	          rt=(new String(chrList)).trim();
	        }
		}catch(Exception e){
			
		}
		return rt;
	}
	
	/**
	 * String->ASCII
	 * @param data(String) 			例如：
	 * @param len(返回字符长度)		例如：
	 * @return String(ASCII)		例如：
	 */
	public static String constructor(String data,int len){
		String rt="";
		try{						
	        byte[] bt=data.getBytes();
	        for (int i=0;i<bt.length;i++){
	        	rt=rt+Integer.toHexString(bt[i]).toUpperCase();
	        }
	        rt=AnsiDataSwitch.StrStuff("0",len,rt,"right");//不足长度右补0
		}catch(Exception e){
			
		}
		return rt;
	}
}
