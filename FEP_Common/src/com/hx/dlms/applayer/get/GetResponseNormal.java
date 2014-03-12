/**
 * Get-Response-Normal ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	result Get-Data-Result
}
 */
package com.hx.dlms.applayer.get;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.applayer.DataAccessResult;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class GetResponseNormal extends ASN1Sequence {
	private static final long serialVersionUID = 7272799714724496878L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected GetDataResult result = new GetDataResult();
	
	public GetResponseNormal(){
		TagAdjunct adjunct = TagAdjunct.contextSpecificImplicit(1);
		adjunct.axdrCodec(true);
		this.setTagAdjunct(adjunct);
		members = new ASN1Type[]{ invokeIdPriority,result };
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
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
	
	public void setResult(DlmsData data){
		result.setData(data);
	}
	
	public final void setAccessResult(DataAccessResult enumResult){
		result.setAccessResult(enumResult);
	}
	
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<GetResponseNormal value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<GetResponseNormal invokeId=\"");
		sb.append(getInvokeId()).append("\" confirm=\"").append(this.isConfirmed());
		sb.append("\" priority=\"");
		if( this.isPriorityHigh() )
			sb.append("high\" \r\n\t");
		else
			sb.append("normal\"\r\n\t");
		sb.append(result);
		sb.append("\r\n</GetResponseNormal>");
		return sb.toString();
	}

	public final GetDataResult getResult() {
		return result;
	}
}
