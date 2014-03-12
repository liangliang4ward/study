/**
Set-Request-With-Datablock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	datablock DataBlock-SA
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.DataBlockSA;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetRequestNextBlock extends ASN1Sequence {
	private static final long serialVersionUID = 5627818249597856127L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected DataBlockSA dataBlock = new DataBlockSA();

	public SetRequestNextBlock(){
		super();
		members = new ASN1Type[]{ invokeIdPriority,dataBlock };
	}
	
	public SetRequestNextBlock(int frameSeq,boolean lastBlock,int blockNum,byte[] rawData){
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
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}

	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public final void setDataBlock(boolean lastBlock,int blockNum,byte[] rawData){
		dataBlock.setLastBlock(lastBlock);
		dataBlock.setBlockNumber(blockNum);
		dataBlock.setRawData(rawData);
	}
	
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<SetRequestNextBlock value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<SetRequestNextBlock");
		sb.append(this.invokeIdPriority);
		sb.append(">\r\n\t");
		sb.append(this.dataBlock);
		sb.append("\r\n</SetRequestNextBlock>");
		return sb.toString();
	}
}
