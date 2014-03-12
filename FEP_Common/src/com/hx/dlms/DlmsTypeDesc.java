package com.hx.dlms;

import java.io.IOException;
import java.math.BigInteger;

import org.apache.log4j.Logger;


public class DlmsTypeDesc extends ASN1Type implements DlmsDataType{
	private static final long serialVersionUID = 8229267376784043249L;
	private static final Logger log = Logger.getLogger(DlmsTypeDesc.class);
	protected int arraySize = -1;
	protected DlmsTypeDesc arrayType = null;
	protected DlmsTypeDesc[] structTypes = null;
	
	public DlmsTypeDesc(){
		super(ASN1Constants.TAG_ANY);
		codec = ASN1Constants.AXDR_CODEC;
		forceEncodeTag = true;
	}
	
	public void setType(int type){
		tagValue = type;
	}
	
	public void setArrayType(int size,DlmsTypeDesc typeDesc){
		tagValue = ARRAY;  //Implicit SEQUENCE, just encode tag of ARRAY.
		arraySize = size;
		arrayType = typeDesc;
	}
	
	public void setStructType(DlmsTypeDesc[] structureItems){
		tagValue = STRUCTURE;
		structTypes = structureItems;
	}
	
	public int getType(){
		return tagValue;
	}
	
	@Override
	public void encode(EncodeStream output) throws IOException{
		encodeTag(output);
		if( tagValue == ARRAY ){
			output.write( (arraySize>>8) & 0xff );
			output.write( arraySize & 0xff );
			arrayType.encode(output);
		}
		else if( tagValue == STRUCTURE ){
			int varSize = null == structTypes ? 0 : structTypes.length;
			if( varSize<128 ){
				output.write(varSize);
			}
			else{
				//First byte 0x80 | (number of bytes for unsigned LENGTH)
				byte[] lenVal = BigInteger.valueOf( varSize ).toByteArray();
				output.write(0x80 | lenVal.length );
				output.write(lenVal);
			}
			//encode contents
			for(int i=0; null != structTypes && i<structTypes.length; i++)
				structTypes[i].encode(output);
		}
	}
	
	public boolean decode(DecodeStream input) throws IOException{
		boolean result = false;
		if( decodeState == DecodeState.DECODE_TAG )
			result = decodeTag(input);
		if( !result ){
			return result;
		}
		if( tagValue != STRUCTURE && tagValue != ARRAY ){
			decodeState = DecodeState.DECODE_DONE;
			return true;
		}
		if( tagValue == STRUCTURE ){
			if( decodeState == DecodeState.DECODE_LENGTH ){
				structTypes = null;
				if( input.available() == 0 )
					return false;
				input.mark();
				int aByte = input.read();
				if( aByte>=0 && aByte< 127 ){
					//Actual size of SEQUENCEOF
					length = aByte;
					decodeState = DecodeState.DECODE_VALUE;
				}
				else{
					int lenCount = aByte & 0x7F;
					if( lenCount > input.available() ){
						input.reset();
						if( log.isDebugEnabled() )
							log.debug("Decode variable-length SEQUENCEOF, insuficient buf, need="+lenCount+",avail="+input.available());
						return false;
					}
					byte[] lenBytes = new byte[lenCount];
					input.read(lenBytes);
					length = (new BigInteger(lenBytes)).intValue() & 0x7FFFFFFF;
					decodeState = DecodeState.DECODE_VALUE;
				}
			}
			if( decodeState == DecodeState.DECODE_VALUE ){
				if( null == structTypes )
					structTypes = new DlmsTypeDesc[length];
				result = true;
				for( int i=0; result && i< length; i++ ){
					if( null != structTypes[i] ){
						if( structTypes[i].decodeState == DecodeState.DECODE_DONE )
							continue;
						result = result && structTypes[i].decode(input);
					}
					else{
						structTypes[i] = new DlmsTypeDesc();
						result = result && (structTypes[i].decode(input));
					}
				}
				if( result )
					decodeState = DecodeState.DECODE_DONE;
			}
		}
		else{
			//ARRAY type here
			if( decodeState == DecodeState.DECODE_LENGTH ){
				//Using decode-length as decoding array-size
				if( input.available()<2 )
					return false;
				arraySize = input.read() & 0xFF;
				arraySize = (arraySize<<8) | ( input.read() & 0xFF );
				decodeState = DecodeState.DECODE_VALUE;
				arrayType = new DlmsTypeDesc();
			}
			if( decodeState == DecodeState.DECODE_VALUE ){
				result = arrayType.decode(input);
				if( result )
					decodeState = DecodeState.DECODE_DONE;
			}
		}
		return decodeState == DecodeState.DECODE_DONE;
	}
	
	@Override
	public String toString(){
		if( tagValue >= 0 && tagValue<=27 ){
			if( tagValue != STRUCTURE && tagValue != ARRAY )
				return TYPE_NAME[tagValue];
			StringBuilder sb = new StringBuilder();
			sb.append(TYPE_NAME[tagValue]).append(" [");
			if( tagValue == ARRAY ){
				sb.append("size=").append(arraySize);
				sb.append(",type=").append(arrayType.toString());
			}
			else{
				for(int i=0; null != structTypes && i<structTypes.length; i++){
					sb.append(" ( member.").append(i+1).append("=").append(structTypes[i]).append(" )");
				}
			}
			sb.append("]");
			return sb.toString();
		}
		else if( tagValue == 255 )
			return "dont care";
		else
			return "undefine";
	}
}
