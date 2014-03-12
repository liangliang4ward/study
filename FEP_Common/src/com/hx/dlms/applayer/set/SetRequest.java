/**
Set-Request ::= CHOICE
{
	set-request-normal [1] IMPLICIT Set-Request-Normal,
	set-request-with-first-datablock [2] IMPLICIT Set-Request-With-First-Datablock,
	set-request-with-datablock [3] IMPLICIT Set-Request-With-Datablock,
	set-request-with-list [4] IMPLICIT Set-Request-With-List,
	set-request-with-list-and-first-datablock [5] IMPLICIT Set-Request-With-List-And-First-Datablock
}
 */
package com.hx.dlms.applayer.set;

import java.io.IOException;
import java.util.HashMap;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.DlmsData;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.applayer.CosemAttributeDescriptorSelection;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetRequest extends ASN1Choice {
	private static final long serialVersionUID = 2740370887073203748L;
	private SetRequestNormal normal = new SetRequestNormal();
	private SetRequestFirstBlock firstBlock = new SetRequestFirstBlock();
	private SetRequestNextBlock next = new SetRequestNextBlock();
	private SetRequestWithList withList = new SetRequestWithList();
	private SetRequestWithListFirstBlock withListFirstBlock = new SetRequestWithListFirstBlock();
	
	private HashMap<Class<? extends ASN1Type>,Integer> map = new HashMap<Class<? extends ASN1Type>,Integer>();
	
	public SetRequest(){
		this.setAxdrCodec();
		TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(193);
		myAdjunct.axdrCodec(true);
		this.setTagAdjunct(myAdjunct);	 // [193] IMPLICIT SET-Request

		addChoiceMember(normal,1);
		addChoiceMember(firstBlock,2);
		addChoiceMember(next,3);
		addChoiceMember(withList,4);
		addChoiceMember(withListFirstBlock,5);
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
			return "SetResponse EMPTY. Not decoded.";
		return selectedObject.toString();
	}

	public static void main(String[] args) {
		//NORMAL
		SetRequestNormal reqNormal = new SetRequestNormal();
		reqNormal.setInvokeId(1);
		reqNormal.setPriorityHigh(true);
		reqNormal.setAttribute(1, HexDump.toByteBuffer("0000800000FF").array(), 2);
		DlmsData param = new DlmsData();
		String octStr = "0102030405060708091011121314151617181920212223242526272829303132333435363738394041424344454647484950";
		param.setOctetString(HexDump.toByteBuffer(octStr).array());
		reqNormal.setParameter(param);
		
		SetRequest req = new SetRequest();
		req.choose(reqNormal);
		try {
			byte[] codes = req.encode();
			System.out.println("req normal encode: "+HexDump.hexDumpCompact(codes, 0, codes.length));

			req = new SetRequest();
			DecodeStream decoder = DecodeStream.wrap(codes);
			req.decode(decoder);
			System.out.println(req);
			decoder = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		//WITH-LIST
		SetRequestWithList reqList = new SetRequestWithList();
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
		DlmsData[] valList = new DlmsData[2];
		
		valList[0] = new DlmsData();
		String val0Str = "0102030405060708091011121314151617181920212223242526272829303132333435363738394041424344454647484950";
		valList[0].setOctetString(HexDump.toByteBuffer(val0Str).array());
		
		valList[1] = new DlmsData();
		String val1Str = new String(HexDump.toByteBuffer("303030").array());
		valList[1].setVisiableString(val1Str);
		reqList.setValueList(valList);
		try {
			req = new SetRequest();
			req.choose(reqList);
			byte[] codes = req.encode();
//should: C104810200010000800000FF020000010000800100FF020002093201020304050607080910111213141516171819202122232425262728293031323334353637383940414243444546474849500A03303030
//result: 
			System.out.println();
			System.out.println("req list encode: "+HexDump.hexDumpCompact(codes, 0, codes.length));

			req = new SetRequest();
			DecodeStream decoder = DecodeStream.wrap(codes);
			req.decode(decoder);
			System.out.println(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//SET-REQUEST-WITH-FIRST-BLOCK
		SetRequestFirstBlock reqFirstBlock = new SetRequestFirstBlock();
		reqFirstBlock.setInvokeId(1);
		reqFirstBlock.setPriorityHigh(true);
		reqFirstBlock.setAttribute(1, HexDump.toByteBuffer("0000800000FF").array(), 2);
		String firstBlockStr = "093201020304050607080910111213141516171819";
		reqFirstBlock.setDataBlock(false, 1, HexDump.toByteBuffer(firstBlockStr).array());
		
		try {
			req = new SetRequest();
			req.choose(reqFirstBlock);
			byte[] codes = req.encode();
			System.out.println();
			System.out.println("first block encode: "+HexDump.hexDumpCompact(codes, 0, codes.length));

			req = new SetRequest();
			DecodeStream decoder = DecodeStream.wrap(codes);
			req.decode(decoder);
			System.out.println(req);
			decoder = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		//SET-REQUEST-NEXT-BLOCK
		SetRequestNextBlock reqNextBlock = new SetRequestNextBlock();
		reqNextBlock.setInvokeId(1);
		reqNextBlock.setPriorityHigh(true);
		String nextBlockStr = "20212223242526272829303132333435363738394041424344454647484950";
		reqNextBlock.setDataBlock(true, 2, HexDump.toByteBuffer(nextBlockStr).array());
		
		try {
			req = new SetRequest();
			req.choose(reqNextBlock);
			byte[] codes = req.encode();
			System.out.println();
			System.out.println("next block encode: "+HexDump.hexDumpCompact(codes, 0, codes.length));

			req = new SetRequest();
			DecodeStream decoder = DecodeStream.wrap(codes);
			req.decode(decoder);
			System.out.println(req);
			decoder = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
