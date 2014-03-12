package com.hx.dlms.applayer;

import java.util.Arrays;

import cn.hexing.fk.utils.HexDump;

public class CosemObis {
	
	public static final byte[] parseObis(String obisString){
		if( null == obisString )
			return null;
		byte[] result = new byte[6];
		Arrays.fill(result, (byte)0);
		int index = -1;
		int len = obisString.length();
		char c = 0;
		int v = 0;
		boolean computing = false;
		int i = 0;
		for( ; i<len && index<6 ; i++ ){
			c = obisString.charAt(i);
			if( c>='0' && c<='9' ){
				if( computing ){
					v = v*10 + (c-'0');
				}
				else{
					index++;
					v = c - '0';
					computing = true;
				}
			}
			else{
				if( computing ){
					result[index] = (byte)v;
				}
				computing = false;
				v = 0;
			}
		}
		if( index == 5 && i==len )
			result[5] = (byte)v;
		else if( index != 6 )
			throw new RuntimeException("Invalid OBIS ID:"+obisString);
		return result;
	}
	
	public static final String OidToString(byte[] oid){
		if( null == oid )
			return "OID is null";
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<oid.length-1; i++ ){
            sb.append(0xFF & oid[i]);
            sb.append('.');
		}
		sb.append(0xFF & oid[oid.length - 1]);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String str = "1.2.12.230.15.9";
		byte[] obis = parseObis(str);
		System.out.println(HexDump.hexDump(obis,0,obis.length));
	}
}
