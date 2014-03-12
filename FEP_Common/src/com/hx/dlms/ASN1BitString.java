/**
 * BITSTRING 
 * (1)BER CODEC: TLV, content part: first byte indicate unused-bits length,
 * 		remaining is bit-string content.
 * (2)A-XDR CODEC: fixed length: LENGTH omited.
 *    variable-length: first byte > 0, first byte is bit-length; 
 *        otherwise: (byte0 & 0x7F) is number of bytes to construct unsigned integer which is bit-length.
 *    Content part: bit-string content. No unused bits length.
 */
package com.hx.dlms;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * BitString: 
 * @author hbao
 *
 */
public class ASN1BitString extends ASN1OctetString {
	private static final long serialVersionUID = -7542792993019841100L;
	private static final Logger log = Logger.getLogger(ASN1BitString.class);
    private static final byte[] SET_MASK = { (byte) 128, 64, 32, 16, 8, 4, 2, 1 };

    private static final byte[] RESET_MASK = { 0x7f, (byte) 0xbf, (byte) 0xdf,
            (byte) 0xef, (byte) 0xf7, (byte) 0xfb, (byte) 0xfd, (byte) 0xfe, };
    private static final String[] BITS4_TO_BIN = {"0000","0001","0010","0011","0100","0101","0110","0111",
    	"1000","1001","1010","1011","1100","1101","1110","1111"};

	private int fixedBitsLength = -1;
	private int unusedBits = 0;

	/**
	 * variable length bit-string
	 */
	public ASN1BitString(){
		super(ASN1Constants.TAG_BITSTRING,-1);
	}
	
	/**
	 * fixed length bit-string
	 * @param fixedBitsSize
	 */
	public ASN1BitString(int fixedBitsSize){
		super(ASN1Constants.TAG_BITSTRING,-1);
		fixedBitsLength = fixedBitsSize;
		if( fixedBitsSize>0 ){
			int mod = fixedBitsSize & 0x07; // = fixedBitsSize % 8
			int bytesCount = fixedBitsSize >> 3;
			if( mod > 0 ){
				bytesCount++;
				unusedBits = 8-mod;
			}
			super.fixedLength = bytesCount+1;
			value = new byte[fixedLength];
			Arrays.fill(value, (byte)0);
			value[0] = (byte)unusedBits;
		}
	}
	
	/**
	 * variable length bit-string with initValue
	 * @param initValue
	 */
	public ASN1BitString(boolean[] initValue){
		this(initValue,-1);
	}
	
	/**
	 * fixed length bit-string with initValue
	 * @param initValue
	 * @param fixedSize
	 */
	public ASN1BitString(boolean[] initValue, int fixedSize){
		this(fixedSize);
		setInitValue(initValue);
	}
	
	public void setInitValue(byte[] bitsValue){
		value = new byte[bitsValue.length+1];
		value[0] =  0;
		for(int i=0; i<bitsValue.length; i++)
			value[i+1] = bitsValue[i];
	}
	
	public void setInitValue(boolean[] initValue){
		int byteCount = 0, unused = 0;
		if( fixedBitsLength>0 ){
			unused = unusedBits;
			byteCount = value.length;
		}
		else{
			unused = (8 - (initValue.length & 0x07 )) & 0x07;
			unusedBits = unused;
			byteCount = (initValue.length >> 3) + (unused !=0 ? 2 : 1);
			value = new byte[byteCount];
			value[0] = (byte)unused;
		}
		int bitCount = (byteCount-1) << 3;
		for(int i=0; i<bitCount; i++){
			setBitValue(i,i<initValue.length ? initValue[i] : false);
		}
	}
	
    public void setBitValue(int bit, boolean bitValue) {
        int offset = bit & 0x07 ;
        int index = (bit >>3 ) + 1;
        if (bitValue) {
            value[index] |= SET_MASK[offset];
        } else {
        	value[index] &= RESET_MASK[offset];
        }
    }
    
    public boolean getBitValue(int bit){
        int offset = bit & 0x07 ;
        int index = (bit >>3) + 1;
        return (value[index] & SET_MASK[offset]) != 0;
    }

	@Override
	public void encodeLength(EncodeStream output) throws IOException{
		if( isAxdrCodec() ){
			if( fixedBitsLength > 0 )
				return;
			//Variable size BITSTRING
			//First byte>0, it is the length value(<128).
			int varLen = null == value ? 0 : ((value.length-1)<<3) - unusedBits;
			if( varLen<128 ){
				output.write(varLen);
				return;
			}
			//First byte 0x80 | (number of bytes for unsigned LENGTH)
			byte[] lenVal = BigInteger.valueOf( varLen ).toByteArray();
			output.write(0x80 | lenVal.length );
			output.write(lenVal);
			return;
		}
		super.encodeLength(output);
	}

	@Override
	public boolean decodeLength(DecodeStream input) throws IOException {
		if( isAxdrCodec() ){
			if( fixedBitsLength > 0 ){ //fixed length
				decodeState = DecodeState.DECODE_VALUE;
				return true;
			}
			//Variable length BITSTRING
			if( input.available() == 0 )
				return false;
			input.mark();
			int aByte = input.read();
			int bitsLen = 0;
			if( aByte>=0 ){
				//Actual bit-length of BITSTRING
				bitsLen = aByte;
				if(aByte>128){
					int lenOfLen=aByte&0x0F;
					byte[] le = new byte[lenOfLen]; 
					input.read(le, 0, lenOfLen);
					BigInteger bi = new BigInteger(le);
					bitsLen = bi.intValue();
				}
			}
			else{
				int lenBytes = aByte & 0x7F;
				if( lenBytes > input.available() ){
					input.reset();
					if( log.isDebugEnabled() )
						log.debug("Decode variable-length BITSTRING, insuficient buf, need="+lenBytes+",avail="+input.available());
					return false;
				}
				byte[] bytes = new byte[lenBytes];
				input.read(bytes);
				bitsLen = (new BigInteger(bytes)).intValue() & 0x7FFFFFFF;
			}
			unusedBits = (8 - (bitsLen & 0x07))&0x07;
			int bitsContents = (bitsLen>>3) + (unusedBits != 0 ? 2 : 1);
			value = new byte[bitsContents];
			value[0] = (byte)unusedBits;
			decodeOffset = 1;
			decodeRemainLength = bitsContents - 1;
			decodeState = DecodeState.DECODE_VALUE;
			return true;
		}
		return super.decodeLength(input);
	}
	
    @Override
	public void encodeContent(EncodeStream output) throws IOException{
    	if( isAxdrCodec() ){
    		if( null != value && value.length>1 ){
    			output.write(value, 1, value.length-1);
    		}
    		return;
    	}
    	super.encodeContent(output);
	}


    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	if( null == value )
    		sb.append("no value");
    	else{
    		sb.append("unused:").append(value[0]).append(",value:");
			sb.append(toBinaryString(value,1,value.length-1));
    	}
    	return sb.toString();
    }
    
    public static boolean[] toBoolean(String binString){
    	int cnt = 0;
    	for(int i=0; i<binString.length(); i++){
    		char c = binString.charAt(i);
    		if( c =='0' || c == '1' )
    			cnt++;
    		else if( c != ' ' )
    			throw new RuntimeException("Invalid binary string:"+binString);
    	}
    	boolean[] result = new boolean[cnt];
    	int offset = 0;
    	for(int i=0; i<binString.length(); i++){
    		char c = binString.charAt(i);
    		if( c =='0' || c == '1' )
    			result[offset++] = c == '1';
    	}
    	return result;
    }
    
    public static String toBinaryString(byte[] buf, int offset, int len){
    	StringBuilder sb = new StringBuilder();
    	for( ; offset <buf.length && len>0 ; offset++,len--){
    		sb.append(BITS4_TO_BIN[ (buf[offset] & 0xF0 )>>4]).append(BITS4_TO_BIN[ buf[offset] & 0x0F ]);
    	}
    	return sb.toString();
    }

}
