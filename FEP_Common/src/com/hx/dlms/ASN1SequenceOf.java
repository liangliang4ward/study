package com.hx.dlms;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.HexDump;

public class ASN1SequenceOf extends ASN1Sequence {
	private static final long serialVersionUID = -7770320484411778343L;
	private static final Logger log = Logger.getLogger(ASN1SequenceOf.class);
	protected ASN1ObjectFactory factory = null;
	protected int fixedSize = -1;
	
	/**
	 * Construct variable size SEQUENCEOF ,with factory object.
	 * @param factoryObj : used to create ASN1Type object which used when decoding.
	 */
	public ASN1SequenceOf(ASN1ObjectFactory factoryObj){
		this(factoryObj,-1);
	}

	/**
	 * Construct fixed size SEQUENCEOF refType.
	 * @param factoryObj : used to create ASN1Type object which used when decoding.
	 */
	public ASN1SequenceOf(ASN1ObjectFactory factoryObj, int fixSize){
		super(ASN1Constants.TAG_SEQUENCEOF,-1);
		factory = factoryObj;
		fixedSize = fixSize;
	}
	
	/**
	 * Construct variable size SEQUENCEOF with initValue.
	 * @param refType
	 */
	public ASN1SequenceOf(ASN1Type[] initValue){
		super(ASN1Constants.TAG_SEQUENCEOF,-1);
		members = initValue;
		if( null == initValue )
			throw new RuntimeException("Can not create variable size SEQUENCEOF with null inital value");
//		if( ! validateMembers() )
//			throw new RuntimeException("Type of member is not same.");
	}

	/**
	 * Construct fixed size SEQUENCEOF refType.
	 * @param refType
	 */
	public ASN1SequenceOf(ASN1Type[] initValue, int fixSize){
		super(ASN1Constants.TAG_SEQUENCEOF,-1);
		if(null == initValue || initValue.length != fixSize || fixSize<=0 )
			throw new RuntimeException("Can not create fix size SEQUENCEOF,illegal arguments:"+fixSize);
		members = initValue;
		fixedSize = fixSize;
		if( ! validateMembers() )
			throw new RuntimeException("Type of member is not same.");
	}
	
	public void setFactory(ASN1ObjectFactory factoryObj){
		factory = factoryObj;
	}
	
	public void setInitValue(ASN1Type[] initValue){
		if( fixedSize>0 && fixedSize != initValue.length )
			throw new RuntimeException("Fixed Size="+fixedSize+", initValue.len="+initValue.length);
		members = initValue;
//		if( ! validateMembers() )
//			throw new RuntimeException("Type of member is not same.");
	}
	
	protected boolean validateMembers(){
		if( null == members || members.length == 0 )
			return true;
		int typeId = members[0].identifier();
		for(int i=1; i<members.length; i++){
			if( typeId != members[i].identifier() )
				return false;
		}
		return true;
	}

	@Override
	public void encodeLength(EncodeStream output) throws IOException{
		if( isAxdrCodec() ){
			if( fixedSize>0 )
				return;
			//Variable size SEQUENCEOF encodes LENGTH field as to BITSTRING LENGTH filed.
			//First byte>0, it is the length value(<128).
			int varSize = null == members ? 0 : members.length;
			if( varSize<128 ){
				output.write(varSize);
				return;
			}
			//First byte 0x80 | (number of bytes for unsigned LENGTH)
			byte[] lenVal = BigInteger.valueOf( varSize ).toByteArray();
			output.write(0x80 | lenVal.length );
			output.write(lenVal);
			return;
		}
		super.encodeLength(output);
	}

	@Override
	public boolean decodeLength(DecodeStream input) throws IOException {
		if( isAxdrCodec() ){
			if( fixedSize>0 ){ //fixed length
				decodeState = DecodeState.DECODE_VALUE;
				return true;
			}
			//Variable size SEQUENCEOF
			if( input.available() == 0 )
				return false;
			int posSaved = input.position();
			int aByte = input.read();
			if( aByte>=0 && aByte< 127 ){
				//Actual size of SEQUENCEOF
				length = aByte;
				decodeState = DecodeState.DECODE_VALUE;
				return true;
			}
			int lenCount = aByte & 0x7F;
			if( lenCount > input.available() ){
				input.position(posSaved);
				if( log.isDebugEnabled() )
					log.debug("Decode variable-length SEQUENCEOF, insuficient buf, need="+lenCount+",avail="+input.available());
				return false;
			}
			byte[] lenBytes = new byte[lenCount];
			input.read(lenBytes);
			if(lenCount==1){
				length = Integer.parseInt(HexDump.toHex(lenBytes[0]),16);
			}else{
				if(lenBytes[0]<0){
					throw new RuntimeException("length's first byte is below zero.");
				}
				length = (new BigInteger(lenBytes)).intValue() & 0x7FFFFFFF;
			}
			decodeState = DecodeState.DECODE_VALUE;
			return true;
		}
		return super.decodeLength(input);
	}
	
	private boolean decodeAXDRContent(DecodeStream input) throws IOException{
		if( null == members ){
			int size = fixedSize>0 ? fixedSize : length;
			if(size>10000)
				throw new RuntimeException("decodeSeqence to large,unhandle it .size="+size);
			members = new ASN1Type[size];
			Arrays.fill(members, null);
		}
		boolean result = true;
		int i = 0;
		for( ; result && i< members.length; i++ ){
			if( null == members[i] )
				members[i] = factory.create();
			if( members[i].isDecodeDone() )
				continue;
			result = result && (members[i].decode(input));
		}
		if( ! result ){
			String msg = "SEQUENCEOF.AXDR.decode exp: size="+members.length+",decoded Index="+i+",input remain="+input.available();
			log.error(msg);
			input.position(0);
			log.warn("SEQUENCEOF.content="+input.toString());
			if( i>0 ){
				members = Arrays.copyOf(members, i);
			}
			//throw new IOException(msg);
		}
		decodeState = DecodeState.DECODE_DONE;
		return true;
	}
	
	@Override
	public boolean decodeContent(DecodeStream input) throws IOException{
		if( this.isAxdrCodec() )
			return decodeAXDRContent(input);
		//BER CODEC...
		return super.decodeContent(input);
	}

	@Override
	protected void onDecodeConstructedComplete(DecodeStream _input) throws IOException {
		DecodeStream input = _input;
		if(null != value && value.length>0 )
			input = DecodeStream.wrap(ByteBuffer.wrap(value));
		ArrayList<ASN1Type> items = new ArrayList<ASN1Type>();
		while( input.available()>0 ){
			ASN1Type item = factory.create();
			if( item.decode(input) )
				items.add(item);
			else
				break;
		}
		members = items.toArray(new ASN1Type[items.size()]);
	}

}
