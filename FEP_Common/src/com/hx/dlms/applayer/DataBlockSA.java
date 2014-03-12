/**
DataBlock-SA ::= SEQUENCE -- SA == DataBlock for the SET-request, ACTION-request and ACTION-response
{
	last-block BOOLEAN,
	block-number Unsigned32,
	raw-data OCTET STRING
}
 */
package com.hx.dlms.applayer;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1Boolean;
import com.hx.dlms.ASN1Integer;
import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 * DataBlock for the SET-request, ACTION-request and ACTION-response
 */
public class DataBlockSA extends ASN1Sequence {
	private static final long serialVersionUID = 1897054804010740394L;
	protected ASN1Boolean lastBlock = new ASN1Boolean();
	protected ASN1Integer blockNumber = new ASN1Integer();
	protected ASN1OctetString rawData = new ASN1OctetString();
	
	public DataBlockSA(){
		blockNumber.fixedLength(4);

		//Construct the SEQUENCE.
		members = new ASN1Type[]{ lastBlock, blockNumber, rawData };
	}
	
	public void setBlock(boolean isLastBlock, int blockNum, byte[] data){
		setLastBlock(isLastBlock);
		setBlockNumber(blockNum);
		rawData.setValue(data);
	}
	
	public void setFirstBlock(byte[] data){
		setLastBlock(false);
		setBlockNumber(1);
		rawData.setValue(data);
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
	
	public void setRawData(byte[] rawData){
		this.rawData.setValue(rawData);
	}
	
	public byte[] getRawData(){
		return rawData.getValue();
	}
	
	@Override
	public String toString(){
		if( !isDecodeDone() )
			return "<DataBlockSA value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<DataBlockSA lastBlock=\"");
		sb.append(isLastBlock()).append("\" blockNumber=\"");
		sb.append(getBlockNumber()).append("\">");
		byte[] raw = getRawData();
		sb.append("\r\n\t\t<RawData>");
		if( null != raw && raw.length>0 ){
			sb.append(HexDump.hexDumpCompact(raw, 0,raw.length));
		}
		sb.append("</RawData>");
		sb.append("\r\n\t</DataBlockSA>");
		return sb.toString();
	}
}
