package com.hx.ansi.parse;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexing.fk.utils.HexDump;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-22 上午10:28:49
 * @version 1.0 
 */

public class AnsiDataSwitch {//数据转换类
	private static final Log log=LogFactory.getLog(AnsiDataSwitch.class);
	  public AnsiDataSwitch() {
	  }
	  /*public static void main(String[] args){
	    String sSHex="",sSSJGS="",sPutout="";
	    DataSwitch dataSW=new DataSwitch();
	    //物理数据转换成逻辑数据
	    sSHex="FFFFFFFF";
	    sSSJGS="00000000";
	    sPutout=dataSW.HexToInt(sSHex,sSSJGS);
	    System.out.println((sPutout));
	    sSHex="20050926101213";
	    sSSJGS="yyyymmddhhnnss";
	    sPutout=dataSW.BCDToDateTime(sSHex,sSSJGS);
	    System.out.println((sPutout));
	    sSHex="1234";
	    sSSJGS="00.00";
	    sPutout=dataSW.BCDToFloat(sSHex,sSSJGS);
	    System.out.println((sPutout));
	    sSHex="1234";
	    sSSJGS="2";
	    sPutout=dataSW.HexToString(sSHex,sSSJGS);
	    System.out.println((sPutout));
	    //逻辑数据转换成物理数据
	    sSHex="1234";
	    sSSJGS="0000";
	    sPutout=dataSW.IntToHex(sSHex,sSSJGS);
	    System.out.println((sPutout));
	    sSHex="20050907110112";
	    sSSJGS="yymmddhhnnss";
	    sPutout=dataSW.DateTimeToBCD(sSHex,sSSJGS);
	    System.out.println((sPutout));
	    sSHex="112111";
	    sSSJGS="0000.00";
	    sPutout=dataSW.FloatToBCD(sSHex,sSSJGS);
	    System.out.println((sPutout));
	    sSHex="1234";
	    sSSJGS="1";
	    sPutout=dataSW.StringToHex(sSHex,sSSJGS);
	    System.out.println((sPutout));
	    sSHex="1bcd";
	    System.out.println(sPutout);
	    sSHex="112233445566";
	    sPutout=dataSW.ReverseStringByByte(sSHex);
	    System.out.println(sPutout);
	    sSHex="Aabcdef123";
	    sPutout=dataSW.StringtoHexASCII(sSHex);
	    System.out.println(sPutout);
	    sSHex="41616263646566313233";
	    sPutout=dataSW.HexASCIIToString(sSHex);
	    System.out.println(sPutout);
	
	    sSHex="00FEFEFEFE6800FE16";
	    sPutout=dataSW.StrFilter("FE",false,sSHex);
	    System.out.println(sPutout);
	
	
	    sSHex="20051114151200";
	    sPutout=dataSW.IncreaseDateTime(sSHex,2,5);
	    System.out.println(sPutout);
	
	
	
	    String s1 = "2006-01-05 10:26:00";
	    String s2 = "2003-08-15 17:15:30";
	    try {
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        ParsePosition pos = new ParsePosition(0);
	        ParsePosition pos1 = new ParsePosition(0);
	        Date dt1 = formatter.parse(s1, pos);
	        Date dt2 = formatter.parse(s2, pos1);
	        Date dt3 =new Date();
	        System.out.println("dt1=" + dt1);
	        //System.out.println("dt2=" + dt2);
	        System.out.println("dt3=" + dt3);
	        double l = (dt3.getTime() - dt1.getTime());
	        l=l/3600000;
	        int iTemp=(int)(l);
	        System.out.println("Hello World!=" + iTemp);
	
	
	    } catch (Exception e) {
	        System.out.println("exception" + e.toString());
	    }
	   }*/
	  public static String IntToHex(String sInt,int len){ //十进制转换成十六进制
		  String sDataContent="";
		  try{
			  sInt=(Integer.toString(Integer.parseInt(sInt),16)).toUpperCase();
			  sDataContent=StrStuff("0",len,sInt,"left");
		  }
		  catch(Exception e){
		  }
		  return sDataContent;
	  }
//按字节倒置
public static String ReverseStringByByte(String str){
	  String sOutput="";
	  try{
		  if (str.length() % 2 == 0) {
			  for (int i=0;i<str.length()/2;i++){
				  sOutput=sOutput+str.substring((str.length()-(i+1)*2),(str.length()-i*2)) ;
			  }
		  }
		  else{
		  }
	  }
	  catch(Exception e){
	  }
	  return sOutput;
}
//BCD码合法性检验
public static boolean isBCDString(String str){
	  boolean tag=true;
	  if (str!=null&&str.length()>0){
		  try{
			  for(int i=0;i<str.length();i++){
				  int value=Integer.parseInt(str.substring(i,i+1),16);
				  if(value>9){
					  tag=false;
					  break;
				  }				  
			  }
		  }
		  catch(Exception e){
			  return tag=false;	 
		  }
	  }
	  else
		  tag=false;
	  return tag;	  
}

//字符补足匹配函数:补足字符；要求长度；输入字符串；补足方向
public static String StrStuff(String str,int iLen,String sInput,String sSign){
  String sOutput="";
	try{
	    int iLenStr=sInput.length();
	    if (iLen>iLenStr){//输入字符需要补足
		    for (int i = 0; i < (iLen-iLenStr); i++){
			    if (sSign.equals("left")){//左补足
			      sInput=str+sInput;
			    }
			    else {//右补足
			      sInput=sInput+str;
			    }
			}
	    }
		else if (iLen<iLenStr){//输入字符过长需要消去
		  if (sSign.equals("left")){//消去左部
		    sInput = sInput.substring(iLenStr-iLen,iLenStr);
		  }
		  else {//消去右部
		    sInput = sInput.substring(0,iLen);
		  }
		}
		sOutput=sInput;
	}
	catch(Exception e){

	}
	return sOutput;
}
public static String Fun8BinTo2Hex(String sBit8){ //8位2进制字符转化为1个字节的十六进制
  String sResult="";
  String sTemp="";
  try{
    try{
      for (int i=0;i<2;i++){
        sTemp=sBit8.substring(i*4,i*4+4);
        if (sTemp.equals("0000")) {sTemp="0";}
        if (sTemp.equals("0001")) {sTemp="1";}
        if (sTemp.equals("0010")) {sTemp="2";}
        if (sTemp.equals("0011")) {sTemp="3";}
        if (sTemp.equals("0100")) {sTemp="4";}
        if (sTemp.equals("0101")) {sTemp="5";}
        if (sTemp.equals("0110")) {sTemp="6";}
        if (sTemp.equals("0111")) {sTemp="7";}
        if (sTemp.equals("1000")) {sTemp="8";}
        if (sTemp.equals("1001")) {sTemp="9";}
        if (sTemp.equals("1010")) {sTemp="A";}
        if (sTemp.equals("1011")) {sTemp="B";}
        if (sTemp.equals("1100")) {sTemp="C";}
        if (sTemp.equals("1101")) {sTemp="D";}
        if (sTemp.equals("1110")) {sTemp="E";}
        if (sTemp.equals("1111")) {sTemp="F";}
        sResult=sResult+sTemp;
      }
    }
    catch(Exception e){
      System.out.println("数据区解析出错Fun8BinTo2Hex:"+e.toString());
    }
  }
  finally{
    return sResult;
  }
}
public static String Fun2HexTo8Bin(String sBit8){ //将一个字节16进制数转换成8位的二进制字符
  String sResult="";
  String sTemp="";
  try{
    try{
      for (int i=0;i<2;i++){
        sTemp=sBit8.substring(i,1+i);
        if (sTemp.toUpperCase().equals("0")) {sTemp="0000";}
        if (sTemp.toUpperCase().equals("1")) {sTemp="0001";}
        if (sTemp.toUpperCase().equals("2")) {sTemp="0010";}
        if (sTemp.toUpperCase().equals("3")) {sTemp="0011";}
        if (sTemp.toUpperCase().equals("4")) {sTemp="0100";}
        if (sTemp.toUpperCase().equals("5")) {sTemp="0101";}
        if (sTemp.toUpperCase().equals("6")) {sTemp="0110";}
        if (sTemp.toUpperCase().equals("7")) {sTemp="0111";}
        if (sTemp.toUpperCase().equals("8")) {sTemp="1000";}
        if (sTemp.toUpperCase().equals("9")) {sTemp="1001";}
        if (sTemp.toUpperCase().equals("A")) {sTemp="1010";}
        if (sTemp.toUpperCase().equals("B")) {sTemp="1011";}
        if (sTemp.toUpperCase().equals("C")) {sTemp="1100";}
        if (sTemp.toUpperCase().equals("D")) {sTemp="1101";}
        if (sTemp.toUpperCase().equals("E")) {sTemp="1110";}
        if (sTemp.toUpperCase().equals("F")) {sTemp="1111";}
        sResult=sResult+sTemp;
      }
    }
    catch(Exception e){
      System.out.println("数据区解析出错Fun2HexTo8Bin:"+e.toString());
    }
  }
  finally{
    return sResult;
  }
}
public static String parseStringToBit (String sBit8){
	String rt="";
	try{
		for(int i=0;i<sBit8.length()/2;i++)
			rt=rt+AnsiDataSwitch.Fun2HexTo8Bin(sBit8.substring(i*2,i*2+2));
	}    
	catch(Exception e){
      System.out.println("数据区解析出错parseStringToBit:"+e.toString());
    }finally{
  	  return rt;
    }

}

public static int parseBytetoInt(byte b){
	return b&0xFF;
}

public static byte calculateCS(byte[] data,int start,int len){
	int cs=0;
	for(int i=start;i<start+len;i++){
		cs+=(data[i] & 0xff);
		cs&=0xff;
	}
	cs=~cs+1;//累加和取补码
	return (byte)(cs & 0xff);
}
/**
 * 将10进制字符转为16进制字符
 * @param s
 * @return
 */
public static String toHexString(String s) 
{ 
String str=""; 
for (int i=0;i<s.length()/2;i++) 
{ 
int ch = Integer.parseInt(s.substring(2*i, 2+2*i));
String s4 = Integer.toHexString(ch); 
s4="00".substring(s4.length())+s4;
str = str + s4; 
} 
return str.toUpperCase(); 
}
/**
 * 将16进制字符转为10进制字符
 * @param s
 * @return
 */
public static String hexToString(String s) 
{ 
String str="";
String ss="";
for(int i=0;i<s.length()/2;i++){
	ss=s.substring(i*2, 2+2*i);
	byte b[]=HexDump.toArray(ss);
	ss=""+parseBytetoInt(b[0]);
	ss="00".substring(ss.length())+ss;
	str=str+ss;
}
return str.toUpperCase(); 
}

public static String getDouble(String s,int len){
	s=s+"0000";
	int index=s.indexOf(".")+1+len;
	s=s.substring(0, index);
	return s;
}

public static boolean isAllFF(String s,int len){
	String ff="";
	for(int i=0;i<len;i++){
		ff+="F";
	}
	return s.equalsIgnoreCase(ff);
}
public static String  parseInt2HexString(int i){
	String ss=Integer.toHexString(i);
	if(1==(ss.length()%2)){
		ss=0+ss;
	 }
	return ss;
}
public static String  parseInt2String(int i){
	String ss=Integer.toString(i);
	if(1==(ss.length()%2)){
		ss=0+ss;
	 }
	return ss;
}
public static String getFF(int len){
	String s="";
	for(int i=0;i<len;i++){
		s=s+"F";
	}
	return s;
}

public static String IncreaseDateTime(String sDateTime,int iIncreaseNo,int iIncreaseType){ //把输入的字符串型的时间累加分、小时、日、月,相应IncreaseType：2,3,4,5
  String sResult = "";
  try{
      Calendar DateTime= Calendar.getInstance();
      //时间格式:YYYYMMDDHHNNSS
      DateTime.set(Integer.parseInt(sDateTime.substring(0,4)),Integer.parseInt(sDateTime.substring(4,6))-1,Integer.parseInt(sDateTime.substring(6,8)),Integer.parseInt(sDateTime.substring(8,10)),Integer.parseInt(sDateTime.substring(10,12)),0);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      try {
        switch (iIncreaseType){
          case 2: DateTime.add(DateTime.MINUTE,iIncreaseNo);//累加分钟               
            break;
          case 3: DateTime.add(DateTime.HOUR,iIncreaseNo);//累加小时 
            break;
          case 4: DateTime.add(DateTime.DATE,iIncreaseNo);//累加日    
            break;
          case 5: DateTime.add(DateTime.MONTH,iIncreaseNo);//累加月  
            break;
        }
        sResult=formatter.format(DateTime.getTime());
      }
      catch (Exception e) {
      	log.error("数据区解析IncreaseDateTime出错:"+e.toString());
      }
  }
  finally{
    return sResult;
  }
  
}
}
