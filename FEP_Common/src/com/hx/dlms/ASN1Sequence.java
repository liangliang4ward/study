package com.hx.dlms;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * ASN.1 SEQUENCE type.
 * Sequence type contain 1 or more type.
 * If a type is optional, any type behind it must unique. 
 * @author hbao
 *
 */
public class ASN1Sequence extends ASN1Type {
	private static final long serialVersionUID = 7682083975430928180L;
	private static final Logger log = Logger.getLogger(ASN1Sequence.class);
	protected ASN1Type[] members = null;
	protected boolean optionalMembers = false;
	
	public ASN1Sequence( ){
		super(CLASS_UNIVERSAL,TAG_SEQUENCE,PC_CONSTRUCTED,0,false);
	}
	
	public ASN1Sequence(int tagNumber){
		super(CLASS_UNIVERSAL,tagNumber,PC_CONSTRUCTED,0,false);
	}
	
	/**
	 * Used by SequenceOf type
	 * @param tagNumber
	 * @param fixedLen
	 */
	public ASN1Sequence(int tagNumber,int fixedLen){
		super(CLASS_UNIVERSAL,tagNumber,PC_CONSTRUCTED,fixedLen,false);
	}
	
	public ASN1Sequence(ASN1Type[] types){
		this();
		
		if( null== types || types.length == 0 )
			throw new RuntimeException("SEQUENCE contains no type.");
		//Validation types.
		int optIndex = -1;
		for(int i=0; i<types.length; i++ ){
			if( optIndex>=0 ){
				//Make sure that any type must unique after optIndex
				for(int j=optIndex; j<i; j++ ){
					if( types[j].identifier() == types[i].identifier() )
						throw new RuntimeException("Sequence contain duplicated id:"+types[i].identifier());
				}
			}
			else if( types[i].isOptional() ){
				optIndex = i;
			}
		}
		members = types;
	}

	@Override
	public ASN1Type codec(int myCodec) {
		super.codec(myCodec);
		if( null != members ){
			for(int i=0; i<members.length; i++ ){
				if( null != members[i] )
					members[i].codec(myCodec);
			}
		}
		return this;
	}

	protected byte[] encodeMembers() throws IOException{
		if( null == members || members.length == 0 )
			return null;
		EncodeStream tmpEncoder = new EncodeStream();
		for(int i=0; i<members.length; i++)
			members[i].encode(tmpEncoder);
		byte[] result = tmpEncoder.dump();
		if( result.length == 0 )
			return null;
		return result;
	}
	
	@Override
	public void encode(EncodeStream output) throws IOException{
		if( null == value )
			value = encodeMembers();
		super.encode(output);
	}
	
	@Override
	public boolean decodeLength(DecodeStream input) throws IOException{
		if( isAxdrCodec() ){
			decodeState = DecodeState.DECODE_VALUE;
			return true;
		}
		return super.decodeLength(input);
	}

	private void decodeSequenceMembers(DecodeStream _input) throws IOException {
		//It is called when content is ready.
		DecodeStream input = _input;
		if(null != value && value.length>0 )
			input = DecodeStream.wrap(ByteBuffer.wrap(value));
		boolean decodeMember = true;
		int i=0;
		for(; i<members.length && input.available()>0 && decodeMember; i++){
			ASN1Type t = members[i];
			decodeMember = t.decode(input);
		}
		if( i != members.length && !optionalMembers ){
			String msg = "SEQUENCE decode error: at i="+i+",remain data="+input.available();
			log.error(msg);
			//鉴于有些表做的不规范，此处不抛出异常。
			//throw new IOException(msg);
		}
	}
	
	@Override
	protected void onDecodeConstructedComplete(DecodeStream input) throws IOException {
		decodeSequenceMembers(input);
	}

	public final ASN1Type[] getMembers() {
		return members;
	}
	
	public final void setOptionalMembers(boolean optMembers){
		optionalMembers = optMembers;
	}
	
	public final boolean isOptionalMembers(){
		return optionalMembers;
	}
}
