/**
 * DataBlock-G ::= SEQUENCE -- G == DataBlock for the GET-response
{
	last-block BOOLEAN,
	block-number Unsigned32,
	result CHOICE
	{
		raw-data 			[0] IMPLICIT OCTET STRING,
		data-access-result 	[1] IMPLICIT Data-Access-Result
	}
}
 */
package com.hx.dlms.applayer.get;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1Boolean;
import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Enum;
import com.hx.dlms.ASN1Integer;
import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.applayer.DataAccessResult;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class DataBlockGet extends ASN1Sequence {
	private static final long serialVersionUID = -1413510005929506349L;
	protected ASN1Boolean lastBlock = new ASN1Boolean();
	protected ASN1Integer blockNumber = new ASN1Integer();
	protected ASN1Choice resultChoice = new ASN1Choice();
	
	//resultChoice members:
	private ASN1OctetString rawData = new ASN1OctetString();
	private ASN1Enum accessResult = new ASN1Enum();
	
	public DataBlockGet(){
		//Construct resultChoice.
		TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(0);
		myAdjunct.axdrCodec(true);
		rawData.setTagAdjunct(myAdjunct);
		myAdjunct = TagAdjunct.contextSpecificImplicit(1);
		myAdjunct.axdrCodec(true);
		accessResult.setTagAdjunct(myAdjunct);
		
		resultChoice.addMember(rawData);
		resultChoice.addMember(accessResult);
		
		blockNumber.fixedLength(4);
		//Construct the SEQUENCE.
		members = new ASN1Type[]{ lastBlock, blockNumber, resultChoice };
		
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
	}
	
	public boolean isLastBlock(){
		return lastBlock.getBoolValue();
	}
	
	public void setLastBlock(boolean last ){
		lastBlock.setBoolValue(last);
	}
	
	public int getBlockNumber(){
		return blockNumber.getInt();
	}
	
	public void setBlockNumber(int blockNum ){
		blockNumber.setValue(blockNum);
	}
	
	public void setRawData(byte[] dataBlock ){
		rawData.setValue(dataBlock);
		resultChoice.choose(rawData);
	}
	
	public byte[] getRawData(){
		return rawData.getValue();
	}
	
	public void setAccessResult(DataAccessResult dataAccessResult){
		accessResult.setValue(dataAccessResult.toInt());
		resultChoice.choose(accessResult);
	}
	
	public DataAccessResult getAccessResult(){
		if( accessResult.isDecodeDone() )
			return DataAccessResult.parseResult(accessResult.getEnumValue());
		else
			return DataAccessResult.NOT_APPLICABLE;
	}
	
	@Override
	public String toString(){
		if( !isDecodeDone() )
			return "<DataBlockGet value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<DataBlockGet lastBlock=\"");
		sb.append(isLastBlock()).append("\" blockNumber=\"");
		sb.append(getBlockNumber()).append("\">\r\n\t\t");
		DataAccessResult access = getAccessResult();
		byte[] raw = getRawData();
		if( null != raw && raw.length>0 ){
			sb.append("<RawData>").append(HexDump.hexDumpCompact(raw, 0,raw.length));
			sb.append("</RawData>");
		}
		else{
			sb.append("<AccessResult>");
			sb.append(access);
			sb.append("</AccessResult>");
		}
		sb.append("\r\n\t</DataBlockGet>");
		return sb.toString();
	}
}
