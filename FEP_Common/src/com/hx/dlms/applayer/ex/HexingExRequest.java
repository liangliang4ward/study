package com.hx.dlms.applayer.ex;

import java.io.IOException;
import java.util.HashMap;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.DlmsData;
import com.hx.dlms.TagAdjunct;

/**
 *  HexingExRequest              [246] IMPLICIT HexingEx-Request
	HexingEx-Request :: =CHOICE
	{
    	hexingex-request-transparent    [0] IMPLICIT HexingEx-Request-Transparent
	}

 * @author gaoll
 *
 * @time 2013-5-11 ÉÏÎç09:41:29
 *
 * @info
 */
public class HexingExRequest extends ASN1Choice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HexingExRequestTransparent hexingExRequestTransparent = new HexingExRequestTransparent();
	
	private HashMap<Class<? extends ASN1Type>,Integer> map = new HashMap<Class<? extends ASN1Type>,Integer>();

	public HexingExRequest(){
		this.setAxdrCodec();
		TagAdjunct adjunct = TagAdjunct.contextSpecificImplicit(246);
		adjunct.axdrCodec(true);
		this.setTagAdjunct(adjunct);	 // [246] IMPLICIT HexingEx-Request
		this.addChoiceMember(hexingExRequestTransparent, 0);
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
			return "HexingExRequest EMPTY. Not decoded.";
		return selectedObject.toString();
	}
	
	public static void main(String[] args) throws IOException {
		HexingExRequest ex = new HexingExRequest();
		DlmsData dd = new DlmsData();
		dd.setOctetString(HexDump.toArray("6816"));
		HexingExRequestTransparent h = new HexingExRequestTransparent(dd);
		ex.choose(h);
		byte[] codes = ex.encode();
		System.out.println(HexDump.toHex(codes));
		ex = new HexingExRequest();
		DecodeStream decoder = DecodeStream.wrap(codes);
		ex.decode(decoder);
		System.out.println(ex);
	}
}
