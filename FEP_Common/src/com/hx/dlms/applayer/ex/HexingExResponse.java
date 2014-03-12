package com.hx.dlms.applayer.ex;

import java.io.IOException;
import java.util.HashMap;

import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.TagAdjunct;

/**
 * 
 * 	HexingEx-Response :: =CHOICE
	{
    	hexingex-response-transparent    [0] IMPLICIT HexingEx-Response-Transparent
	}
 * @author gaoll
 *
 * @time 2013-5-11 ÉÏÎç09:59:33
 *
 * @info
 */
public class HexingExResponse extends ASN1Choice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HexingExResponseTransparent hexingExResponseTransparent = new HexingExResponseTransparent();
	
	private HashMap<Class<? extends ASN1Type>,Integer> map = new HashMap<Class<? extends ASN1Type>,Integer>();

	public HexingExResponse(){
		this.setAxdrCodec();
		TagAdjunct adjunct = TagAdjunct.contextSpecificImplicit(247);
		adjunct.axdrCodec(true);
		this.setTagAdjunct(adjunct);	 // [247] IMPLICIT HexingEx-Response
		this.addChoiceMember(hexingExResponseTransparent, 0);
	}
	
	
	private void addChoiceMember(ASN1Type choiceType,int tagNo){
		if( choiceType.getTagAdjunct() == null ){
			TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(tagNo);
			myAdjunct.axdrCodec(true);
			choiceType.setTagAdjunct(myAdjunct);
			map.put(choiceType.getClass(), tagNo);
		}
		else
			map.put(choiceType.getClass(), choiceType.getTagAdjunct().identifier() );

		this.addMember(choiceType);
	}

	
	@Override
	public void choose(ASN1Type objToEncode){
		if( null == objToEncode.getTagAdjunct() ){
			TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(map.get(objToEncode.getClass()));
			myAdjunct.axdrCodec(true);
			objToEncode.setTagAdjunct(myAdjunct);
		}
		super.choose(objToEncode);
	}
	
	@Override
	public String toString(){
		if( ! this.isDecodeDone() )
			return "HexingExResponse EMPTY. Not decoded.";
		return selectedObject.toString();
	}
	
	public static void main(String[] args) {
		DecodeStream decoder = DecodeStream.wrap("F60009026816");
		HexingExResponse resp = new HexingExResponse();
		try {
			resp.decode(decoder);
			HexingExResponseTransparent d=(HexingExResponseTransparent) resp.getDecodedObject();
			System.out.println(d.getData().getStringValue());
		
			System.out.println(resp);
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		decoder = null;
	}
	
}
