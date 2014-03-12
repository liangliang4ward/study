package com.hx.dlms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ASN1Choice extends ASN1Type{
	private static final long serialVersionUID = -4548931920225154283L;
	/**
	 * Choice is a C Union structure. It contains collection of types.
	 * Each member can be distinguished by ASN1 identifier(Tag value). 
	 */
	protected Map<Integer,ASN1Type> membersMap = new HashMap<Integer,ASN1Type>();
	/**
	 * selectedObject has two kind of usage:
	 * 1. ASN1Choice type uses it to encode object for sending;
	 * 2. When receiving inputs, ASN1Choice use 'selectdObject' to store decoded object.
	 */
	protected ASN1Type selectedObject = null;

	public ASN1Choice(){
		super(ASN1Constants.TAG_CHOICE);
		this.forceEncodeTag(true);
	}
	
	public ASN1Choice( ASN1Type[] initMembers){
		this();
		setInitMembers(initMembers);
	}

	@Override
	public ASN1Type codec(int myCodec) {
		super.codec(myCodec);
		Iterator<ASN1Type> iter = membersMap.values().iterator();
		while(iter.hasNext())
			iter.next().codec(myCodec);
		return this;
	}
	
	public void setInitMembers(ASN1Type[] initMembers){
		if(null == initMembers || initMembers.length == 0){
			throw new IllegalArgumentException("ASN1Choice contains NO MEMBER!!");
		}
		membersMap.clear();
		for(int i=0; i<initMembers.length; i++){
			addMember(initMembers[i]);
		}
	}
	
	public void addMember(ASN1Type member){
		if( member.identifier() != 0 || null != member.getTagAdjunct() ){
			if( membersMap.containsKey(member.identifier())){
				throw new RuntimeException("ASN1Choice contain duplicated type. type="+member.identifier());
			}
			membersMap.put(member.identifier(), member);
		}
		else if( member instanceof ASN1Choice)
			membersMap.putAll(((ASN1Choice) member).membersMap);
		else
			throw new RuntimeException("ASN1Type.identifier == 0. Invalid argument.");
		member.codec(codec());
		member.forceEncodeTag(true); //A-XDR codec, Choice must encode each choice's TAG.
	}
	
	public void addAndReplace(ASN1Type member){
		if( member.identifier() != 0 ){
			membersMap.put(member.identifier(), member);
		}
		else if( member instanceof ASN1Choice){
			Iterator<ASN1Type> iter = ((ASN1Choice) member).membersMap.values().iterator();
			while( iter.hasNext() ){
				addAndReplace(iter.next());
			}
		}
		else
			throw new RuntimeException("ASN1Type.identifier == 0. Invalid argument.");
		member.codec(codec());
		member.forceEncodeTag(true); //A-XDR codec, Choice must encode each choice's TAG.
	}
	
	/**
	 * This object's MUST be type of Choice members.
	 * @param objToEncode
	 */
	public void choose(ASN1Type objToEncode){
		if( ! membersMap.containsKey(objToEncode.identifier()))
			throw new RuntimeException("Choice can not encode object which type is not member of Choice.");
		selectedObject = objToEncode;
		selectedObject.codec(codec());
		selectedObject.forceEncodeTag(true);
	}
	
	public ASN1Type getDecodedObject(){
		if( this.decodeState != DecodeState.DECODE_DONE )
			return null;
		return selectedObject;
	}

	@Override
	public boolean decodeTag(DecodeStream input) throws IOException {
		int targetTag = ASN1Type.detectTagValue(input);
		if( targetTag <0 )
			return false;
		int posSaved = input.position();
		selectedObject = membersMap.get(targetTag);
		if( null == selectedObject )
			throw new RuntimeException("ASN1Choice decodeTag exception: no match choice of type");
		if( !selectedObject.decode(input) ){
			input.position(posSaved);
			return false;
		}
		tagValue = targetTag;
		decodeState = DecodeState.DECODE_DONE;
		return true;
	}
	
	@Override
	public void encode(EncodeStream output) throws IOException{
		if( null != selectedObject ){
			selectedObject.onPrepareContent();
		}
/*		if( isAxdrCodec() ){
			encodeAxdr(output);
			return;
		}
*/		if( null == value && null != defaultValue )
			return;
		if( null == selectedObject ){
			if( isOptional() )
				return;
			throw new IOException("Choice not set selected member.");
		}

		if( null != adjunct ){
			adjunct.encodeTag(output);
			if( adjunct.isExplicit() ){
				value = selectedObject.encode();
				if( null== value )
					value = new byte[0];
				doEncodeLength(value.length,output);
				doEncodeContent(value,output);
			}
			else{
				selectedObject.encode(output);
				//selectedObject.encodeLength(output);
				//selectedObject.encodeContent(output);
			}
		}
		else{
			selectedObject.encode(output);
		}
	}
}
