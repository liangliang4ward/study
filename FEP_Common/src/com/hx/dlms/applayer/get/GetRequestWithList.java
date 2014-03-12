/**
 * 	Get-Request-With-List ::= SEQUENCE
	{
		invoke-id-and-priority Invoke-Id-And-Priority,
		attribute-descriptor-list SEQUENCE OF Cosem-Attribute-Descriptor-With-Selection
	}
 */
package com.hx.dlms.applayer.get;

import com.hx.dlms.ASN1ObjectFactory;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.CosemAttributeDescriptorSelection;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class GetRequestWithList extends ASN1Sequence {
	private static final long serialVersionUID = -7311891281408729180L;

	private static final ASN1ObjectFactory attributeTypeFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new CosemAttributeDescriptorSelection();	}
	};
	
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1SequenceOf attributeList = new ASN1SequenceOf(attributeTypeFactory);

	public GetRequestWithList(){
		members = new ASN1Type[]{ invokeIdPriority,attributeList };
	}
	
	public final void setInvokeId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
	}
	
	public final int getInvokeId(){
		return this.invokeIdPriority.getInvokeId();
	}
	
	public final void setConfirmed(boolean confirmed){
		invokeIdPriority.setConfirmed(confirmed);
	}
	
	public final boolean isConfirmed(){
		return invokeIdPriority.isConfirmed();
	}
	
	public final boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public final void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}
	
	public final void setAttributeList( CosemAttributeDescriptorSelection[] list ){
		attributeList.setInitValue(list);
	}
	
	public final CosemAttributeDescriptorSelection[] getAttributeList(){
		ASN1Type[] resultArray = attributeList.getMembers();
		if( ! attributeList.isDecodeDone() || null == resultArray )
			return null;
		CosemAttributeDescriptorSelection[] ret = new CosemAttributeDescriptorSelection[resultArray.length];
		for(int i=0; i<ret.length; i++ ){
			ret[i] = (CosemAttributeDescriptorSelection)resultArray[i];
		}
		return ret;
	}

	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<GetRequestWithList/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<GetRequestWithList invokeId=\"");
		sb.append(getInvokeId()).append("\" confirm=\"").append(this.isConfirmed());
		sb.append("\" priority=\"");
		if( this.isPriorityHigh() )
			sb.append("high\"> \r\n\t");
		else
			sb.append("normal\"> \r\n\t");

		CosemAttributeDescriptorSelection[] myResult = getAttributeList();
		sb.append("<list>");
		for( int i=0; null != myResult && i<myResult.length; i++ ){
			if( i != 0 )
				sb.append("\r\n\t\t");
			sb.append(myResult[i]);
		}
		sb.append("\r\n\t</list>");
		sb.append("\r\n</GetRequestWithList>");
		return sb.toString();
	}
}
