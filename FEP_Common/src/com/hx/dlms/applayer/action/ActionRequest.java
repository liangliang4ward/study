/**
Action-Request ::= CHOICE
{
	action-request-normal [1] IMPLICIT Action-Request-Normal,
	action-request-next-pblock [2] IMPLICIT Action-Request-Next-Pblock,
	action-request-with-list [3] IMPLICIT Action-Request-With-List,
	action-request-with-first-pblock [4] IMPLICIT Action-Request-With-First-Pblock,
	action-request-with-list-and-first-pblock [5] IMPLICIT Action-Request-With-List-And-First-Pblock,
	action-request-with-pblock [6] IMPLICIT Action-Request-With-Pblock
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
public class ActionRequest extends ASN1Choice {
	private static final long serialVersionUID = -5745085004678462443L;
	private ActionRequestNormal normal = new ActionRequestNormal();
	private ActionRequestNextPblock nextPblock = new ActionRequestNextPblock();
	private ActionRequestWithList list = new ActionRequestWithList();
	private ActionRequestFirstPblock withFirstPblock = new ActionRequestFirstPblock();
	private ActionRequestWithListFirstPblock listFirstPblock = new ActionRequestWithListFirstPblock();
	private ActionRequestWithNextPblock withNextPblock = new ActionRequestWithNextPblock();

	private HashMap<Class<? extends ASN1Type>,Integer> map = new HashMap<Class<? extends ASN1Type>,Integer>();

	public ActionRequest(){
		this.setAxdrCodec();
		TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(195);
		myAdjunct.axdrCodec(true);
		this.setTagAdjunct(myAdjunct);	 // [195] IMPLICIT SET-Request
		
		addChoiceMember(normal,1);
		addChoiceMember(nextPblock,2);
		addChoiceMember(list,3);
		addChoiceMember(withFirstPblock,4);
		addChoiceMember(listFirstPblock,5);
		addChoiceMember(withNextPblock,6);
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
