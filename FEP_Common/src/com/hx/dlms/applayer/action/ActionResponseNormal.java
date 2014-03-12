/**
Action-Response-Normal ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	single-response Action-Response-With-Optional-Data
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.applayer.ActionResult;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ActionResponseNormal extends ASN1Sequence {
	private static final long serialVersionUID = -4812075791245140110L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ResponseOptionalData respWithOptionalData = new ResponseOptionalData();
	
	public ActionResponseNormal(){
		super();

		members = new ASN1Type[]{ invokeIdPriority,respWithOptionalData };
	}
	
	public void setActionResult(ActionResult result){
		respWithOptionalData.setActionResult(result);
	}
	
	public void setActionResult(ActionResult result,DlmsData returnData){
		respWithOptionalData.setActionResult(result);
		respWithOptionalData.setReturnData(returnData);
	}
	
	public int getInvokeId(){
		return invokeIdPriority.getInvokeId();
	}
	
	public void setInvokedId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
	}

	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}

	public final ResponseOptionalData getRespWithOptionalData() {
		return respWithOptionalData;
	}
}
