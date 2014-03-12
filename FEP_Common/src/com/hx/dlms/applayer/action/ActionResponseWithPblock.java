/**
Action-Response-With-Pblock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	pblock DataBlock-SA
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.DataBlockSA;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ActionResponseWithPblock extends ASN1Sequence {
	private static final long serialVersionUID = 6938684867635112728L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected DataBlockSA pblock = new DataBlockSA();

	public ActionResponseWithPblock(){
		super();
		members = new ASN1Type[]{ invokeIdPriority,pblock };
		setInvokeId(1);
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
	
	public void setBlock(boolean isLastBlock, int blockNum, byte[] data){
		pblock.setBlock(isLastBlock,blockNum,data);
	}
}
