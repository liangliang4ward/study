/**
Action-Request-With-First-Pblock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	cosem-method-descriptor Cosem-Method-Descriptor,
	pblock DataBlock-SA
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.CosemMethodDescriptor;
import com.hx.dlms.applayer.DataBlockSA;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 * Action-Request-With-First-Pblock
 */
public class ActionRequestFirstPblock extends ASN1Sequence {
	private static final long serialVersionUID = 8582989554700459291L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected CosemMethodDescriptor methodDescriptor = new CosemMethodDescriptor();
	protected DataBlockSA pblock = new DataBlockSA();

	public ActionRequestFirstPblock(){
		members = new ASN1Type[]{ invokeIdPriority,methodDescriptor,pblock };
	}
	
	public void setBlock(byte[] data){
		pblock.setFirstBlock(data);
	}
	
	public void setInvokedId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
	}
	
	public void setMethod( int classId,byte[] obis, int method ){
		methodDescriptor.setValues(classId, obis, method);
	}
	
	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}

}
