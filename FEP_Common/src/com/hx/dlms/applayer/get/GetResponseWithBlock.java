package com.hx.dlms.applayer.get;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.applayer.DataAccessResult;
import com.hx.dlms.applayer.InvokeIdPriority;

public class GetResponseWithBlock extends ASN1Sequence {
	private static final long serialVersionUID = -8808981691459235454L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected DataBlockGet result = new DataBlockGet();
	
	public GetResponseWithBlock(){
		TagAdjunct adjunct = TagAdjunct.contextSpecificImplicit(2);
		adjunct.axdrCodec(true);
		this.setTagAdjunct(adjunct);
		members = new ASN1Type[]{ invokeIdPriority,result };
		
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
	}
	
	public GetResponseWithBlock(int frameSeq,boolean lastBlock,int blockNum,byte[] rawData){
		this();
		setInvokeId(frameSeq);
		setDataBlock(lastBlock,blockNum,rawData);
	}
	
	public final int getInvokeId(){
		return this.invokeIdPriority.getInvokeId();
	}
	
	public final void setInvokeId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
	}
	
	public final void setConfirmed(boolean confirmed){
		invokeIdPriority.setConfirmed(confirmed);
	}
	
	public final boolean isConfirmed(){
		return invokeIdPriority.isConfirmed();
	}
	
	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}
	
	public final void setDataBlock(boolean lastBlock,int blockNum,byte[] rawData){
		result.setLastBlock(lastBlock);
		result.setBlockNumber(blockNum);
		result.setRawData(rawData);
	}
	
	public final void setDataBlock(boolean lastBlock,int blockNum,DataAccessResult accessResult){
		result.setLastBlock(lastBlock);
		result.setBlockNumber(blockNum);
		result.setAccessResult(accessResult);
	}
	
	public final boolean isLastBlock(){
		return result.isLastBlock();
	}
	
	public final int getBlockNumber(){
		return result.getBlockNumber();
	}
	
	public final byte[] getRawData(){
		return result.getRawData();
	}
	
	public final DataAccessResult getAccessResult(){
		return result.getAccessResult();
	}
	
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<GetResponseWithBlock value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<GetResponseWithBlock invokeId=\"");
		sb.append(getInvokeId()).append("\" confirm=\"").append(this.isConfirmed());
		sb.append("\" priority=\"");
		if( this.isPriorityHigh() )
			sb.append("high\" \r\n\t");
		else
			sb.append("normal\"\r\n\t");
		sb.append(result);
		sb.append("\r\n</GetResponseWithBlock>");
		return sb.toString();
	}
}
