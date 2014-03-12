/**
Set-Response-Normal ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	result Data-Access-Result
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1Enum;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.applayer.DataAccessResult;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetResponseNormal extends ASN1Sequence {
	private static final long serialVersionUID = -8070327473046289054L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1Enum accessResult = new ASN1Enum();
	protected DlmsData optData = new DlmsData();

	public SetResponseNormal(){
//		optData.setOptional(true);
		super.setOptionalMembers(true);
		members = new ASN1Type[]{ invokeIdPriority,accessResult, optData };
	}
	
	public SetResponseNormal(int invokeId, DataAccessResult result){
		this();
		setInvokeId(invokeId);
		setAccessResult(result);
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
	
	public final void setAccessResult(DataAccessResult dataAccessResult){
		accessResult.setValue(dataAccessResult.toInt());
	}
	
	public final DataAccessResult getAccessResult(){
		return DataAccessResult.parseResult(accessResult.getEnumValue());
	}
	
	public final ASN1Enum getAccessResultEnum(){
		return accessResult;
	}
	
	public final DlmsData getOptionalData(){
		return optData;
	}
	
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<SetResponseNormal value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<SetResponseNormal").append(this.invokeIdPriority);
		sb.append(">\r\n\t");
		sb.append("<AccessResult result=\"");
		sb.append(getAccessResult());
		sb.append("\"/>");
		sb.append("\r\n</GetResponseNormal>");
		return sb.toString();
	}
}
