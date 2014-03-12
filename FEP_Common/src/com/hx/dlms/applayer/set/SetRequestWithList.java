/**
Set-Request-With-List ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	attribute-descriptor-list SEQUENCE OF Cosem-Attribute-Descriptor-With-Selection,
	value-list SEQUENCE OF Data
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1ObjectFactory;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.applayer.CosemAttributeDescriptorSelection;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetRequestWithList extends ASN1Sequence {
	private static final long serialVersionUID = -7217445439468891820L;
	private static final ASN1ObjectFactory attributeTypeFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new CosemAttributeDescriptorSelection();	}
	};
	private static final ASN1ObjectFactory valueTypeFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new DlmsData();	}
	};
	
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1SequenceOf attributeList = new ASN1SequenceOf(attributeTypeFactory);
	protected ASN1SequenceOf valueList = new ASN1SequenceOf(valueTypeFactory);

	public SetRequestWithList(){
		super();
		
		members = new ASN1Type[]{ invokeIdPriority,attributeList,valueList };
		setInvokeId(1);
	}
	
	public SetRequestWithList(CosemAttributeDescriptorSelection[] attributes,DlmsData[] attrValues){
		super();
		if( attributes.length != attrValues.length )
			throw new RuntimeException("attr.length != values.length");
		attributeList.setInitValue(attributes);
		valueList.setInitValue(attrValues);
	}
	
	public void setAttributeList(CosemAttributeDescriptorSelection[] attributes){
		attributeList.setInitValue(attributes);
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
	
	public void setValueList(DlmsData[] values){
		valueList.setInitValue(values);
	}
	
	public DlmsData[] getValueList(){
		ASN1Type[] resultArray = valueList.getMembers();
		if( ! valueList.isDecodeDone() || null == resultArray )
			return null;
		DlmsData[] ret = new DlmsData[resultArray.length];
		for(int i=0; i<ret.length; i++ ){
			ret[i] = (DlmsData)resultArray[i];
		}
		return ret;
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
	
	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}
	
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<SetRequestWithList value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<SetRequestWithList");
		sb.append(this.invokeIdPriority);
		sb.append(">\r\n\t");

		CosemAttributeDescriptorSelection[] myResult = getAttributeList();
		int qty = null==myResult ? 0 : myResult.length;
		sb.append("<attribute-list quantity=\"").append(qty).append("\">");
		for( int i=0; null != myResult && i<myResult.length; i++ ){
			sb.append(myResult[i]);
		}
		sb.append("\r\n\t</attribute-list>");
		
		DlmsData[] pdataList = getValueList();
		qty = null==pdataList ? 0 : pdataList.length;
		sb.append("\r\n\t<value-list quantity=\"").append(qty).append("\">");
		for( int i=0; null != pdataList && i<pdataList.length; i++ ){
			sb.append("\r\n\t\t");
			sb.append(pdataList[i]);
		}
		sb.append("\r\n\t</value-list>");
		
		sb.append("\r\n</SetRequestWithList>");
		return sb.toString();
	}

}
