/**
 *  Cosem-Attribute-Descriptor-With-Selection ::= SEQUENCE
	{
		cosem-attribute-descriptor Cosem-Attribute-Descriptor,
		access-selection Selective-Access-Descriptor OPTIONAL
	}
 */
package com.hx.dlms.applayer;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;

public class CosemAttributeDescriptorSelection extends ASN1Sequence {
	private static final long serialVersionUID = -6200744998988366540L;
	private CosemAttributeDescriptor attributeDescriptor = new CosemAttributeDescriptor();
	private SelectiveAccessDescriptor selectiveAccess = new SelectiveAccessDescriptor();

	public CosemAttributeDescriptorSelection(){
		members = new ASN1Type[]{ attributeDescriptor,selectiveAccess };
		selectiveAccess.setOptional(true);
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
	}
	
	public CosemAttributeDescriptorSelection(int classID,byte[] obis,int attributeID){
		attributeDescriptor.setValues(classID, obis, attributeID);
	}
	
	public CosemAttributeDescriptorSelection(int classID,byte[] obis,int attributeID,int selector, DlmsData data ){
		attributeDescriptor.setValues(classID, obis, attributeID);
		selectiveAccess.setParameter(selector, data);
	}

	public final void setAttribute(int classID,byte[] obis,int attributeID){
		attributeDescriptor.setValues(classID, obis, attributeID);
	}
	
	public final void setSelector(int selector, DlmsData data){
		selectiveAccess.setParameter(selector, data);
	}

	public final CosemAttributeDescriptor getAttributeDescriptor() {
		return attributeDescriptor;
	}

	public final SelectiveAccessDescriptor getSelectiveAccess() {
		return selectiveAccess;
	}

	@Override
	public String toString() {
		if( !isDecodeDone() )
			return "\r\n\t\t<AttributeDescriptorSelection value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("\r\n\t\t<AttributeDescriptorSelection>");
		sb.append("\r\n\t\t\t").append(attributeDescriptor);
		sb.append("\r\n\t\t\t").append(selectiveAccess);
		sb.append("\r\n\t\t</AttributeDescriptorSelection>");
		return sb.toString();
	}
}
