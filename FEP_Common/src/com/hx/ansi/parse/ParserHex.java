package com.hx.ansi.parse;
/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-5-6 ÉÏÎç09:59:52
 * @version 1.0 
 */

public class ParserHex {
	
	public static String parseValue(String data,int len){
		String rt="";
		try{
			data=data.substring(0, len);
			for(int i=0;i<data.length()/2;i++){
				String s="";
				int k=Integer.parseInt(data.substring(2*i, 2+2*i), 16);
				if(k>9){
					s=""+k;
				}else{
					s="0"+k;
				}
				rt+=s;
			}
		}catch(Exception e){
			
		}
		
		return rt;
	}
	
	
	
}
