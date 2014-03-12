package com.hx.ansi.parse;


/**
 * 十六进制与十进制转换格式
 *
 */
public class ParserHTB {	
	/**
	 * HEX->BCD
	 * @param data(按字节倒置的HEX) 	例如：0B0A
	 * @param len(字符数)				例如：4
	 * @return String(BCD)			例如：2571
	 */
	public static String parseValue(String data,int len){
		String rt="";
		try{
			data=AnsiDataSwitch.ReverseStringByByte(data.substring(0,len));
			rt=""+Long.parseLong(data,16);
		}catch(Exception e){
			
		}
		return rt;
	}
	
	/**
	 * BCD->HEX
	 * @param data(BCD) 				例如：2571
	 * @param len(返回十六进制字符长度)		例如：4
	 * @return String(按字节倒置的HEX)		例如：0B0A
	 */
	public static String constructor(String data,int len){
		String rt="";
		try{
			rt=(Long.toString(Long.parseLong(data),16)).toUpperCase();
	        rt=AnsiDataSwitch.StrStuff("0",len,rt,"left");//不足长度补0
	        rt=AnsiDataSwitch.ReverseStringByByte(rt);//按字节倒置
		}catch(Exception e){
			
		}
		return rt;
	}
}
