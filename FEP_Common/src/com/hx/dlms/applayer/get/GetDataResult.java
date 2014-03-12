package com.hx.dlms.applayer.get;

import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Enum;
import com.hx.dlms.DlmsData;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.applayer.DataAccessResult;
/**
 * Get-Data-Result ::= CHOICE
{
	data [0] Data,
	data-access-result [1] IMPLICIT Data-Access-Result
}
 */
public final class GetDataResult extends ASN1Choice {
	private static final long serialVersionUID = 2291918439367600809L;
	private DlmsData data = new DlmsData();
	private ASN1Enum accessResult = new ASN1Enum();
	
	public GetDataResult(){
		TagAdjunct adjunct = TagAdjunct.contextSpecificExplicit(0);
		adjunct.axdrCodec(true);
		data.setTagAdjunct(adjunct);
		adjunct = TagAdjunct.contextSpecificImplicit(1);
		adjunct.axdrCodec(true);
		accessResult.setTagAdjunct(adjunct);
		
		data.codec(this.codec());
		this.addMember(data);
		accessResult.codec(this.codec());
		this.addMember(accessResult);
	}
	
	public DataAccessResult getAccessResult(){
		int result = 250;
		if( accessResult.isDecodeDone() )
			result = accessResult.getUnsignedInt8();
		else
			result = accessResult.getInitValue();
		return DataAccessResult.parseResult(result);
	}

	public final DlmsData getData() {
		return data;
	}

	public final void setData(DlmsData mydata) {
		this.data = mydata;
		TagAdjunct adjunct = TagAdjunct.contextSpecificExplicit(0);
		adjunct.axdrCodec(true);
		data.setTagAdjunct(adjunct);
		data.codec(this.codec());
		this.addAndReplace(data);
		this.choose(data);
	}
	
	public final void setAccessResult(DataAccessResult enumResult){
		accessResult.setValue(enumResult.toInt());
		this.choose(accessResult);
	}
	
	@Override
	public String toString(){
		if( !this.isDecodeDone() )
			return "<GetDataResult value=\"EMPTY\"/>";
		StringBuilder sb = new StringBuilder(256);
		if( data.isDecodeDone() ){
			sb.append(data);
		}
		else{
			sb.append("<AccessResult result=\"");
			sb.append(getAccessResult());
			sb.append("\"/>");
		}
		return sb.toString();
	}
}
