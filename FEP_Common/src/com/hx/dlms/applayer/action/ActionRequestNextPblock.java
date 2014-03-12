/**
Action-Request-Next-Pblock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	block-number Unsigned32
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1Integer;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ActionRequestNextPblock extends ASN1Sequence {
	private static final long serialVersionUID = 4436353882867309736L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1Integer blockNumber = new ASN1Integer();

	public ActionRequestNextPblock(){
		super();
		blockNumber.fixedLength(4);
		members = new ASN1Type[]{ invokeIdPriority,blockNumber };
	}
	
	public ActionRequestNextPblock(int frameSeq,int pblock){
		this();
		setInvokeId(frameSeq);
		setBlockNumber(pblock);
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
	
	public final void setBlockNumber(int blockNum){
		blockNumber.setValue(blockNum);
	}
	
	public final int getBlockNumber(){
		return blockNumber.getInt();
	}
}
