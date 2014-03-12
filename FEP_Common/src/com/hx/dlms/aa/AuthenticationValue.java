/**
 * Authentication-value ::= CHOICE
 * {
 * 		charstring [0] IMPLICIT GraphicString,
 * 		bitstring [1] IMPLICIT BIT STRING,
 * 		external [2] IMPLICIT EXTERNAL,
 * 		other [3] IMPLICIT SEQUENCE
 * 		{
 * 			other-mechanism-name Mechanism-name,
 * 			other-mechanism-value ANY DEFINED BY other-mechanism-name
 * 		}
 * }
 */
package com.hx.dlms.aa;

import java.util.HashMap;

import com.hx.dlms.ASN1BitString;
import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.TagAdjunct;
/**
 * 
 * @author hbao
 *
 */
public class AuthenticationValue extends ASN1Choice {
	private static final long serialVersionUID = 564024030904954020L;
	private ASN1OctetString octetString = new ASN1OctetString();
	private ASN1BitString bitString = new ASN1BitString();
	private HashMap<Class<? extends ASN1Type>,Integer> map = new HashMap<Class<? extends ASN1Type>,Integer>();

	public AuthenticationValue(){
		addChoiceMember(octetString,0);
		addChoiceMember(bitString,1);
	}
	
	public AuthenticationValue(byte[] val){
		this();
		setAuthValue(val);
	}
	
	public void setAuthValue(byte[] val){
		octetString.setValue(val);
		choose(octetString);
	}
	
	public byte[] getAuthValue(){
		if( null != selectedObject ){
			if( selectedObject.identifier() == 0 ){
				return octetString.getValue();
			}
			else{
				byte[] bitsVal = bitString.getValue();
				byte[] result = new byte[bitsVal.length-1];
				for(int i=0; i<result.length; i++ )
					result[i] = bitsVal[i+1];
				return result;
			}
		}
		return null;
	}

	private void addChoiceMember(ASN1Type choiceType,int tagNo){
		if( choiceType.getTagAdjunct() == null ){
			TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(tagNo);
			myAdjunct.axdrCodec(false);
			choiceType.setTagAdjunct(myAdjunct);
			map.put(choiceType.getClass(), tagNo);
		}
		else
			map.put(choiceType.getClass(), choiceType.getTagAdjunct().identifier() );

		this.addMember(choiceType);
	}
/*
	@Override
	protected void onDecodeContentComplete(DecodeStream _input) throws IOException{
		DecodeStream input = _input;
		if(null != value && value.length>0 )
			input = DecodeStream.wrap(ByteBuffer.wrap(value));
		input.mark();
		try{
			switch(tagValue){
			case 0:
				selectedObject = new ASN1OctetString();
				selectedObject.setTagAdjunct(TagAdjunct.contextSpecificImplicit(0));
				break;
			case 1:
				selectedObject = new ASN1BitString();
				selectedObject.setTagAdjunct(TagAdjunct.contextSpecificImplicit(1));
				break;
			case 2:
				throw new RuntimeException("AuthentionValue is EXTERNAL, not supported yet.");
			case 3:
				throw new RuntimeException("AuthentionValue is OTHER mechanism, not supported yet.");
			default:
				throw new RuntimeException("AuthentionValue is unknow type, tag="+this.identifier());
			}
			if( null == selectedObject )
				throw new RuntimeException("ASN1Choice decodeTag exception: no match choice of type");
//			selectedObject.assignTag(choice);
			selectedObject.decode(input);
			//selectedObject.decodeLength(input);
			//selectedObject.onDecodeContent(input);
		}catch(IOException e){
			input.reset();
			throw e;
		}
	}

	@Override
	public final void encode(EncodeStream output) throws IOException {
		if( null == selectedObject ){
			if( isOptional() )
				return;
			throw new RuntimeException("AuthenticationValue of ASN1Choice encode exception: The encode-object is null.");
		}
		value = selectedObject.encode();
		super.encode(output);
	}
*/
}
