/**
 * Refer to DLMS GREEN book 7th edition.pdf page206.
 * get-Request is [192] of APDU
Get-Request ::= CHOICE
{
	get-request-normal 		[1] IMPLICIT Get-Request-Normal,
	get-request-next 		[2] IMPLICIT Get-Request-Next,
	get-request-with-list 	[3] IMPLICIT Get-Request-With-List
}
 */
package com.hx.dlms.applayer.get;

import java.io.IOException;
import java.util.HashMap;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.applayer.CosemAttributeDescriptorSelection;

/**
 * @author Bao Hongwei
 *
 */
public class GetRequest extends ASN1Choice {
	private static final long serialVersionUID = 7494441977678692457L;
	private GetRequestNormal normal = new GetRequestNormal();
	private GetRequestNext next = new GetRequestNext();
	private GetRequestWithList withList = new GetRequestWithList();
	private HashMap<Class<? extends ASN1Type>,Integer> map = new HashMap<Class<? extends ASN1Type>,Integer>();
	
	public GetRequest(){
		this.setAxdrCodec();
		TagAdjunct adjunct = TagAdjunct.contextSpecificImplicit(192);
		adjunct.axdrCodec(true);
		this.setTagAdjunct(adjunct);	 // [192] IMPLICIT GET-Request

		addChoiceMember(normal,1);
		addChoiceMember(next,2);
		addChoiceMember(withList,3);
	}
	
	public GetRequest(ASN1Type reqObject){
		this();
		choose(reqObject);
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
			return "GetResponse EMPTY. Not decoded.";
		return selectedObject.toString();
	}

/**
C0018100010000800000FF0200
C00181
00010000800000FF0200
<GetRequest>
	<GetRequestNormal>
		<InvokeIdAndPriority Value="81" />
		<AttributeDescriptor>
			<ClassId Value="0001" />
			<InstanceId Value="0000800000FF" />
			<AttributeId Value="02" />
		</AttributeDescriptor>
	</GetRequestNormal>
</GetRequest> 	
 * 
 */
	
	public static void main(String[] args) {
		//NORMAL
		GetRequestNormal reqNormal = new GetRequestNormal();
		reqNormal.setInvokeId(1);
		reqNormal.setPriorityHigh(true);
		reqNormal.setAttribute(1, HexDump.toByteBuffer("0000800000FF").array(), 2);
		GetRequest req = new GetRequest();
		req.choose(reqNormal);
		try {
			byte[] codes = req.encode();
			System.out.println("encode: "+HexDump.hexDumpCompact(codes, 0, codes.length));

			req = new GetRequest();
			DecodeStream decoder = DecodeStream.wrap(codes);
			req.decode(decoder);
			System.out.println(req);
			decoder = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		//WITH-LIST
		GetRequestWithList reqList = new GetRequestWithList();
		reqList.setInvokeId(1);
		reqList.setPriorityHigh(true);
		CosemAttributeDescriptorSelection[] attrList = new CosemAttributeDescriptorSelection[2];
		for(int i=0; i<attrList.length; i++ )
			attrList[i] = new CosemAttributeDescriptorSelection();
		byte[] obis = HexDump.toByteBuffer("0000800000FF").array();
		attrList[0].setAttribute(1, obis,2);
		obis = HexDump.toByteBuffer("0000800100FF").array();
		attrList[1].setAttribute(1, obis,2);
		reqList.setAttributeList(attrList);
		try {
			req = new GetRequest();
			req.choose(reqList);
			byte[] codes = req.encode();
//should: C003810200010000800000FF020000010000800100FF0200
//result: C003810200010000800000FF020000010000800100FF0200
			System.out.println();
			System.out.println("encode: "+HexDump.hexDumpCompact(codes, 0, codes.length));

			req = new GetRequest();
			DecodeStream decoder = DecodeStream.wrap(codes);
			req.decode(decoder);
			System.out.println(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
