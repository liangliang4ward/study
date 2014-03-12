/**
Set-Response-Datablock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	block-number Unsigned32
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1Integer;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetResponseWithBlock extends ASN1Sequence {
	private static final long serialVersionUID = -8545383672421481925L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1Integer blockNumber = new ASN1Integer();

	public SetResponseWithBlock(){
		super();
		blockNumber.fixedLength(4);
		members = new ASN1Type[]{ invokeIdPriority,blockNumber };
	}
	
	public SetResponseWithBlock(int invokeId, int blockNum){
		this();
		setInvokeId(invokeId);
		setBlockNumber(blockNum);
	}
	
	public int getInvokeId(){
		return this.invokeIdPriority.getInvokeId();
	}
	
	public void setInvokeId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
	}
	
	public void setConfirmed(boolean confirmed){
		invokeIdPriority.setConfirmed(confirmed);
	}
	
	public boolean isConfirmed(){
		return invokeIdPriority.isConfirmed();
	}
	
	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}
	
	public final void setBlockNumber(int blockNum){
		blockNumber.setValue(blockNum);
	}
	
	public final int getBlockNumber(){
		return blockNumber.getInt();
	}
	
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<SetResponseWithBlock value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<SetResponseWithBlock").append(this.invokeIdPriority);
		sb.append(">\r\n\t");
		sb.append("<block-number value=\"");
		sb.append(getBlockNumber());
		sb.append("\"/>");
		sb.append("\r\n</SetResponseWithBlock>");
		return sb.toString();
	}
}
