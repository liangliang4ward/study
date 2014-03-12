package com.hx.dlms;

import java.io.IOException;
import java.math.BigInteger;

import org.apache.log4j.Logger;

/**
 * Variable length Integer
 * @author hbao
 *
 */
public class ASN1Integer extends ASN1Type {
	private static final long serialVersionUID = 26088109203212610L;
	private static final Logger log = Logger.getLogger(ASN1Integer.class);
	protected Integer initValue = null;
	protected boolean unsigned = false;

	public ASN1Integer(){
		super(ASN1Constants.TAG_INTEGER);
	}
	/**
	 * @param initValue
	 */
	public ASN1Integer(int initValue) {
		super(ASN1Constants.TAG_INTEGER);
		this.initValue = initValue;
	}
	
	public boolean isUnsigned(){
		return unsigned;
	}
	
	public void setValue(int val){
		initValue = val;
	}

	@Override
	public void onPrepareContent(){
		if( fixedLength>0 && null != initValue ){ //fixed length
			if( fixedLength>4 )
				fixedLength = 4;
			value = new byte[fixedLength];
			switch(fixedLength){
			case 1:
				value[0] = (byte)( initValue.intValue() & 0xFF);
				break;
			case 2:
				value[0] = (byte)( (initValue.intValue()>>8) & 0xFF);
				value[1] = (byte)( initValue.intValue() & 0xFF);
				break;
			case 3:
				value[0] = (byte)( (initValue.intValue()>>16) & 0xFF);
				value[1] = (byte)( (initValue.intValue()>>8) & 0xFF);
				value[2] = (byte)( initValue.intValue() & 0xFF);
				break;
			case 4:
				value[0] = (byte)( (initValue.intValue()>>24) & 0xFF);
				value[1] = (byte)( (initValue.intValue()>>16) & 0xFF);
				value[2] = (byte)( (initValue.intValue()>>8) & 0xFF);
				value[3] = (byte)( initValue.intValue() & 0xFF);
				break;
			}
		}
		else if( null != initValue ){
			//variable length Integer
			if( isAxdrCodec() ){
				if( initValue.intValue() < 128 && initValue.intValue() >= 0 ){ //length omitted
					value = new byte[]{ initValue.byteValue() };
					return;
				}
				if( unsigned )
					value = BigInteger.valueOf(0x00FFFFFFFF & initValue.intValue() ).toByteArray();
				else
					value = BigInteger.valueOf( initValue.intValue() ).toByteArray();
			}
			else{
				if( unsigned )
					value = BigInteger.valueOf(0x00FFFFFFFF & initValue.intValue()).toByteArray();
				else
					value = BigInteger.valueOf(initValue.intValue()).toByteArray();
			}
		}
	}

	@Override
	public void encodeLength(EncodeStream output) throws IOException {
		if( fixedLength<0 && null != initValue ){
			//variable length Integer
			if( isAxdrCodec() ){
				if( initValue.intValue() < 128 && initValue.intValue() >= 0 ){ //length omitted
					return;
				}
				if( unsigned )
					value = BigInteger.valueOf(0x00FFFFFFFF & initValue.intValue() ).toByteArray();
				else
					value = BigInteger.valueOf( initValue.intValue() ).toByteArray();
				output.write(0x80 | value.length );
				return ;
			}
		}
		super.encodeLength(output);
	}

	@Override
	public boolean decodeLength(DecodeStream input) throws IOException {
		if( isAxdrCodec() ){
			if( fixedLength>=0 ){ //fixed length
				decodeState = DecodeState.DECODE_VALUE;
				return true;
			}
			//Variable length Integer
			if( input.available() == 0 )
				return false;
			int posSaved = input.position();
			int aByte = input.read();
			if( aByte>=0 && aByte< 127 ){
				value = new byte[]{ (byte)aByte };
				decodeState = DecodeState.DECODE_DONE;
			}
			length = aByte & 0x7F;
			if( length > input.available() ){
				input.position(posSaved);
				if( log.isDebugEnabled() )
					log.debug("Decode variable-length Integer, insuficient buf, need="+length+",avail="+input.available());
				return false;
			}
			decodeState = DecodeState.DECODE_VALUE;
			return true;
		}
		return super.decodeLength(input);
	}
	
	public int getInt8(){
		return value[0];
	}
	
	public int getUnsignedInt8(){
		return 0xFF & value[0];
	}
	
	public int getUnsignedInt16(){
		BigInteger bv = new BigInteger(value);
		return bv.intValue() & 0xFFFF;
	}

	public int getInt(){
		BigInteger bv = new BigInteger(value);
		if( unsigned ){
			if( fixedLength == 1)
				return bv.intValue() & 0xFF;
			else if( fixedLength == 2 )
				return bv.intValue() & 0xFFFF;
			else if( fixedLength == 3)
				return bv.intValue() & 0xFFFFFF;
		}
		return bv.intValue();
	}
	
	public int getInitValue(){
		if( null != initValue )
			return initValue.intValue();
		return -1;
	}
}
