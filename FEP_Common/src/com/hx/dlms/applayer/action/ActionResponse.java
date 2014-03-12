/**
Action-Response ::= CHOICE
{
	action-response-normal [1] IMPLICIT Action-Response-Normal,
	action-response-with-pblock [2] IMPLICIT Action-Response-With-Pblock,
	action-response-with-list [3] IMPLICIT Action-Response-With-List,
	action-response-next-pblock [4] IMPLICIT Action-Response-Next-Pblock
}
 */
package com.hx.dlms.applayer.action;

import java.util.HashMap;

import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.TagAdjunct;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ActionResponse extends ASN1Choice {
	private static final long serialVersionUID = -2014844157036163245L;
	private ActionResponseNormal normal = new ActionResponseNormal();
	private ActionResponseWithPblock withPblock = new ActionResponseWithPblock();
	private ActionResponseWithList withList = new ActionResponseWithList();
	private ActionResponseNextPblock nextPblock = new ActionResponseNextPblock();

	private HashMap<Class<? extends ASN1Type>,Integer> map = new HashMap<Class<? extends ASN1Type>,Integer>();
	
	public ActionResponse(){
		this.setAxdrCodec();
		TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(199);
		myAdjunct.axdrCodec(true);
		this.setTagAdjunct(myAdjunct);	 // [199] IMPLICIT ACTION-Response,
		
		addChoiceMember(normal,1);
		addChoiceMember(withPblock,2);
		addChoiceMember(withList,3);
		addChoiceMember(nextPblock,4);
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
}
