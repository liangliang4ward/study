package test_.serial;

import java.io.*;

public class toByte {
	

		public static byte intToByteArray(int num) { 
		  byte b ;
		 // int mask = 0xff;
		//  for(int i=0;i<4;i++)
		//  {
		 //  int j = 3;
		   b  = (byte)num;//(num>>>(24-3*8));
		  
		//  }
		  return b;
		 } 
		

	

	public static void main(String args[]) {
		toByte a = new toByte();
		byte q=a.intToByteArray(18);
		System.out.print(q);
		//toHex w=new toHex();
		//w.WriteToHex(q);
		
	}
}
