package com.hx.ansi.parse;



/**
 * IP:PORT与十六进制字符串转换格式
 *
 */
public class ParserIP {	
	/**
	 * HEX->IP地址（XXX.XXX.XXX.XXX:NNNNN）
	 * @param data  				例如：
	 * @param len(字符数)				例如：
	 * @return String(HEX)			例如：
	 */
	public static String parseValue(String data,int len){
		String rt="";
		try{
			data=data.substring(0,len);
			for(int i=0;i<4;i++)
				rt=rt+ParserHTB.parseValue(data.substring(i*2,i*2+2), 2)+".";
			data=data.substring(8);
			if (len==12){//IP:PORT				
				rt=rt.substring(0,rt.length()-1)+":"+ParserHTB.parseValue(AnsiDataSwitch.ReverseStringByByte(data.substring(0,4)),4);
			}
			else {//IP
				rt=rt.substring(0,rt.length()-1);
			}
		}catch(Exception e){
			
		}
		return rt;
	}
	
	/**
	 * IP地址（XXX.XXX.XXX.XXX:NNNNN）->HEX
	 * @param data(IP地址) 				例如：
	 * @param len(返回十进制字符长度)		例如：
	 * @return String(HEX)				例如：
	 */
	public static String constructor(String data,int len){
		String rt="";
		try{			
			String[] strs=data.split(":");
			if(strs.length==2){//IP:PORT
				String[] ips= strs[0].trim().split("\\.");
				for (int i=0;i<ips.length;i++)
					rt=rt+ParserHTB.constructor(ips[i].trim(), 2);
				rt=rt+AnsiDataSwitch.ReverseStringByByte(ParserHTB.constructor(strs[1].trim(), 4));
			}
			else if(strs.length==1&&len==8){//IP
				String[] ips= strs[0].trim().split("\\.");
				for (int i=0;i<ips.length;i++)
					rt=rt+ParserHTB.constructor(ips[i].trim(), 2);
			}
			else{
				
			}
		}catch(Exception e){
			
		}
		return rt;
	}
}
