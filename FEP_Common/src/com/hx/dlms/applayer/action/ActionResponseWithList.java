/**
Action-Response-With-List ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	list-of-responses SEQUENCE OF Action-Response-With-Optional-Data
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1ObjectFactory;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ActionResponseWithList extends ASN1Sequence {
	private static final long serialVersionUID = 6688296146930385868L;

	private static final ASN1ObjectFactory responseFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new ResponseOptionalData();	}
	};
	
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1SequenceOf responseList = new ASN1SequenceOf(responseFactory);

	public ActionResponseWithList(){
		super();
		members = new ASN1Type[]{ invokeIdPriority,responseList };
		setInvokeId(1);
	}
	
	public ActionResponseWithList(ResponseOptionalData[] respList){
		this();
		responseList.setInitValue(respList);
	}
	
	public final void setResponseList(ResponseOptionalData[] respList){
		responseList.setInitValue(respList);
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

	public final ASN1SequenceOf getResponseList() {
		return responseList;
	}
}
