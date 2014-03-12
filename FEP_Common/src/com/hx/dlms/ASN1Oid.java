/**
 * In computing, an object identifier or OID is an identifier used to name an object 
 * (compare URN). Structurally, an OID consists of a node in a hierarchically-assigned 
 * name-space, formally defined using the ITU-T's ASN.1 standard.
 * 
 * In computer security, OIDs serve to name almost every object type in X.509 
 * certificates, such as components of Distinguished Names, CPSs, etc.
 * 
 * BER encoding. Primitive. Contents octets are as follows, where value1, ..., valuen denote the integer values of the components in the complete object identifier:

  	£¨1£© The first octet has value 40 * value1 + value2. (This is unambiguous, since value1 is limited to values 0, 1, and 2; value2 is limited to the range 0 to 39 when value1 is 0 or 1; and, according to X.208, n is always at least 2.) 
  	£¨2£© The following octets, if any, encode value3, ..., valuen. Each value is encoded base 128, most significant digit first, with as few digits as possible, and the most significant bit of each octet except the last in the value's encoding set to "1."

	Example: The following values both refer to the object identifier assigned to RSA Data Security, Inc.:

	{ iso(1) member-body(2) 840 113549 }
	{ 1 2 840 113549 }
 * 
 * 
 */
package com.hx.dlms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * 
 * @author HBAO
 *
 */
public class ASN1Oid extends ASN1Type {
	private static final long serialVersionUID = -8875982349511515629L;

	private static final Logger log = Logger.getLogger(ASN1Oid.class);
	
	protected int[] oid = null;
	protected String desc = "";
	
	public ASN1Oid(){
		super(ASN1Constants.TAG_OID);
	}
	
	public ASN1Oid(String strOID ){
		this();
		oid = parse(strOID);
		if( !validate(oid) )
			oid = null;
	}

	/**
	 * The v must positive and encode by 128.
	 * @param v
	 * @return
	 * @throws IOException 
	 */
	public static byte[] encode128(int v) throws IOException{
		if( v<0 )
			throw new IOException("encode128: parameter<0");

        int b = v;
        byte count = (byte)(b>0 ? 0 : 1);
        for (; b > 0; b = b >> 7) {
            count++;
        }
        byte[] result = new byte[count];
        b = v;
        for(int i=count-1; i>=0 ; i-- ){
        	b = 0x7F & v;
        	v = v >> 7;
        	if( i== count-1 )
        		result[i] = (byte)b;
        	else
        		result[i] = (byte)( 0x80 | b );
        }
        return result;
	}

	@Override
	public void onPrepareContent()throws IOException{
		if( null==oid )
			return;
		EncodeStream encoder = new EncodeStream();
		int b = oid[0]*40 + oid[1];
		encoder.write( encode128(b) );
		
		// the rest of sub-identifiers
		for(int i=2; i<oid.length; i++){
			encoder.write( encode128(oid[i]) );
		}
		value = encoder.dump();
		encoder.close();
	}

	@Override
	protected void onDecodeContentComplete(DecodeStream _input) throws IOException{
		int count = 0;
		for(int i=0; i<value.length; i++){
			if( (value[i] & 0x80) == 0 )
				count++;
		}
		oid = new int[count+1];
		int b = 0, v = 0;
		for(int i=0, oidIndex=1; i<value.length && oidIndex<oid.length ; oidIndex++, i++ ){
			b = value[i];
			v = b & 0x7F;
			while( (b & 0x80) != 0 ){
				//read next byte;
				i++;
				b = value[i];
				v = (v << 7) | ( b & 0x7F );
			}
			oid[oidIndex] = v;
		}
        // Special handling for the first packed OID element
        if (oid[1] > 79) {
            oid[0] = 2;
            oid[1] = oid[1] - 80;
        } else {
            oid[0] = oid[1] / 40;
            oid[1] = oid[1] % 40;
        }
	}

    /**
     * Compares object with OID for equality.
     * 
     * @return true if object is ObjectIdentifier and it has the same
     *         representation as array of integers, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        return Arrays.equals(oid, ((ASN1Oid)obj).oid);
    }

    @Override
	public String toString(){
		if( null == oid )
			return "OID is null";
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(int i=0; i<oid.length-1; i++ ){
            sb.append(0xFF & oid[i]);
            sb.append('.');
		}
		sb.append(0xFF & oid[oid.length - 1]);
		sb.append("}");
		return sb.toString();
	}
    
    public String getDescription(){
    	return desc;
    }
    
    public void setDescription(String descrip){
    	desc = descrip;
    }

	protected boolean validate(int[] oid){
		if( null == oid || oid.length == 0 ){
			log.error("OID empty");
			return false;
		}
		if( oid.length<2 ){
			log.error("OID.length < 2");
			return false;
		}
		for(int i=0; i<oid.length; i++){
			if( oid[i]<0 ){
				log.error("OID number<0");
				return false;
			}
		}
		if( oid[0]>2 ){
			log.error("OID[0]<=2, oid[0]="+oid[0]);
			return false;
		}
		if( oid[0]<2 && oid[1]>39 ){
			log.error("value2 is limited to the range 0 to 39 when value1 is 0 or 1");
			return false;
		}
		return true;
	}
	/**
	 * format: { 1 2 840 113549 1 } ; { 1.2.840.113549.1 }; 1.2.840.113549.1; 1 2 840 113549 1
	 * @param strOID
	 * @return
	 */
	public static int[] parse(String strOID){
		ArrayList<Integer> arrList = new ArrayList<Integer>();
		int len = strOID.length();
		int val = 0;
		boolean computing = false;
		char c;
		for(int i=0; i<len; i++ ){
			c = strOID.charAt(i);
			if( c>='0' && c <='9' ){
				if( ! computing )
					computing = true;
				val = val*10 + (c-'0');
			}
			else{
				if( computing ){
					arrList.add(val);
					computing = false;
					val = 0;
				}
				continue;
			}
		}
		if( computing )
			arrList.add(val);
		int[] result = new int[arrList.size()];
		for(int i=0; i<arrList.size(); i++)
			result[i] = arrList.get(i);
		return result;
	}
	
}
