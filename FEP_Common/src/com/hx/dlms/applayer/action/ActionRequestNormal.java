/**
Action-Request-Normal ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	cosem-method-descriptor Cosem-Method-Descriptor,
	method-invocation-parameters Data OPTIONAL
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.applayer.CosemMethodDescriptor;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ActionRequestNormal extends ASN1Sequence {
	private static final long serialVersionUID = 5900779713497750011L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected CosemMethodDescriptor methodDescriptor = new CosemMethodDescriptor();
	protected DlmsData parameter = new DlmsData();

	public ActionRequestNormal(){
		super();

		parameter.setOptional(true);
		members = new ASN1Type[]{ invokeIdPriority,methodDescriptor,parameter };
	}
	
	public ActionRequestNormal(int frameSeq, int classId,byte[] obis, int method, DlmsData data){
		this();
		invokeIdPriority.setInvokeId(frameSeq);
		methodDescriptor.setValues(classId, obis, method);
		parameter.assignValue(data);
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

	public void setParameter(DlmsData data){
		parameter.assignValue(data);
	}
}
