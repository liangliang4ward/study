/**
Set-Response ::= CHOICE
{
	set-response-normal [1] IMPLICIT Set-Response-Normal,
	set-response-datablock [2] IMPLICIT Set-Response-Datablock,
	set-response-last-datablock [3] IMPLICIT Set-Response-Last-Datablock,
	set-response-last-datablock-with-list [4] IMPLICIT Set-Response-Last-Datablock-With-List,
	set-response-with-list [5] IMPLICIT Set-Response-With-List
}
 */
package com.hx.dlms.applayer.set;

import java.io.IOException;
import java.util.HashMap;

import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.TagAdjunct;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetResponse extends ASN1Choice {
	private static final long serialVersionUID = -37444197551630365L;
	private SetResponseNormal normal = new SetResponseNormal();
	private SetResponseWithBlock withBlock = new SetResponseWithBlock();
	private SetResponseLastBlock lastBlock = new SetResponseLastBlock();
	private SetResponseWithListLastBlock withListLastBlock = new SetResponseWithListLastBlock();
	private SetResponseWithList withList = new SetResponseWithList();

	private HashMap<Class<? extends ASN1Type>,Integer> map = new HashMap<Class<? extends ASN1Type>,Integer>();

	public SetResponse(){
		this.setAxdrCodec();
		TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(197);
		myAdjunct.axdrCodec(true);
		this.setTagAdjunct(myAdjunct);	 // [197] IMPLICIT SET-Response
		
		addChoiceMember(normal,1);
		addChoiceMember(withBlock,2);
		addChoiceMember(lastBlock,3);
		addChoiceMember(withListLastBlock,4);
		addChoiceMember(withList,5);
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
	public String toString(){
		if( ! this.isDecodeDone() )
			return "SetResponse EMPTY";
		return selectedObject.toString();
	}

	public static void main(String[] args) {
		String respNormal = "C5018100";
		decodeTest(respNormal);
		
		String respList =   "C50581020000";
		decodeTest(respList);
		
		//set-response-with-block
		String respBlock1 = "C5028100000001";
		decodeTest(respBlock1);
		
		String respLastBlock = "C503810000000002";
		decodeTest(respLastBlock);
		
		String respLastBlockWithList = "C5048102000000000003";
		decodeTest(respLastBlockWithList);
	}
	
	private static void decodeTest(String str){
		DecodeStream decoder = DecodeStream.wrap(str);
		SetResponse resp = new SetResponse();
		try {
			resp.decode(decoder);
			System.out.println(resp);
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		decoder = null;
	}
}
