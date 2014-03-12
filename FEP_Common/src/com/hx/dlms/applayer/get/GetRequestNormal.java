/**
 * 
 */
package com.hx.dlms.applayer.get;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.CosemAttributeDescriptor;
import com.hx.dlms.applayer.InvokeIdPriority;
import com.hx.dlms.applayer.SelectiveAccessDescriptor;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class GetRequestNormal extends ASN1Sequence {
	private static final long serialVersionUID = 6975020070394650312L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected CosemAttributeDescriptor attributeDescriptor = new CosemAttributeDescriptor();
	protected SelectiveAccessDescriptor selectiveAccess = new SelectiveAccessDescriptor();
	
	public SelectiveAccessDescriptor getSelectiveAccess() {
		return selectiveAccess;
	}

	public GetRequestNormal(){
		members = new ASN1Type[]{ invokeIdPriority,attributeDescriptor,selectiveAccess };
		selectiveAccess.setOptional(true);
	}
	
	public GetRequestNormal(int frameSeq, int classId,byte[] obis, int attributeId ){
		this();
		invokeIdPriority.setInvokeId(frameSeq);
		attributeDescriptor.setValues(classId, obis, attributeId);
	}
	
	public void setInvokeId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
	}
	
	public int getInvokeId(){
		return this.invokeIdPriority.getInvokeId();
	}
	
	public void setConfirmed(boolean confirmed){
		invokeIdPriority.setConfirmed(confirmed);
	}
	
	public boolean isConfirmed(){
		return invokeIdPriority.isConfirmed();
	}
	
	public void setAttribute( int classId,byte[] obis, int attributeId ){
		attributeDescriptor.setValues(classId, obis, attributeId);
	}
	
	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}

	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<GetRequestNormal/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<GetRequestNormal invokeId=\"");
		sb.append(getInvokeId()).append("\" confirm=\"").append(this.isConfirmed());
		sb.append("\" priority=\"");
		if( this.isPriorityHigh() )
			sb.append("high\"> \r\n\t");
		else
			sb.append("normal\"> \r\n\t");

		sb.append(attributeDescriptor);
		sb.append("\r\n\t");
		sb.append(selectiveAccess);
		sb.append("\r\n</GetResponseNormal>");
		return sb.toString();
	}
}
