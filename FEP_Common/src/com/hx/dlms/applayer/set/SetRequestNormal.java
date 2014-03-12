/**
Set-Request-Normal ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	cosem-attribute-descriptor Cosem-Attribute-Descriptor,
	access-selection Selective-Access-Descriptor OPTIONAL,
	value Data
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.applayer.CosemAttributeDescriptor;
import com.hx.dlms.applayer.InvokeIdPriority;
import com.hx.dlms.applayer.SelectiveAccessDescriptor;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetRequestNormal extends ASN1Sequence {
	private static final long serialVersionUID = -5131046186551145329L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected CosemAttributeDescriptor attributeDescriptor = new CosemAttributeDescriptor();
	protected SelectiveAccessDescriptor selectiveAccess = new SelectiveAccessDescriptor();
	protected DlmsData parameter = new DlmsData();
	
	public SetRequestNormal(){
		super();

		selectiveAccess.setOptional(true);
		members = new ASN1Type[]{ invokeIdPriority,attributeDescriptor,selectiveAccess, parameter };
	}
	
	public SetRequestNormal(int frameSeq, int classId,byte[] obis, int attributeId, DlmsData data){
		this();
		invokeIdPriority.setInvokeId(frameSeq);
		attributeDescriptor.setValues(classId, obis, attributeId);
		parameter.assignValue(data);
	}
	
	public void setInvokeId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
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

	public void setParameter(DlmsData data){
		parameter.assignValue(data);
	}
	
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<SetRequestNormal value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<SetRequestNormal");
		sb.append(this.invokeIdPriority);
		sb.append(">\r\n\t");
		sb.append(this.attributeDescriptor);
		sb.append("\r\n\t");
		sb.append(this.selectiveAccess);
		sb.append("\r\n\t");
		sb.append("<parameter>");
		sb.append("\r\n\t\t");
		sb.append(this.parameter);
		sb.append("\r\n\t");
		sb.append("</parameter>");
		sb.append("\r\n</SetRequestNormal>");
		return sb.toString();
	}
}
