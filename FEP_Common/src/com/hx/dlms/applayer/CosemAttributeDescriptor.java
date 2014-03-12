/**
 * 	Cosem-Attribute-Descriptor ::= SEQUENCE
	{
		class-id Cosem-Class-Id,
		instance-id Cosem-Object-Instance-Id,
		attribute-id Cosem-Object-Attribute-Id
	}
 */
package com.hx.dlms.applayer;

import com.hx.dlms.ASN1Int8;
import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.ASN1UnsignedInt16;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class CosemAttributeDescriptor extends ASN1Sequence {
	private static final long serialVersionUID = -7614978604723705732L;
	private ASN1UnsignedInt16 classId = new ASN1UnsignedInt16();
	private ASN1OctetString instanceId = new ASN1OctetString(6);
	private ASN1Int8 attributeId = new ASN1Int8();
	
	public CosemAttributeDescriptor(){
		this.setAxdrCodec();
		members = new ASN1Type[]{ classId,instanceId,attributeId };
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
	}
	
	public CosemAttributeDescriptor(int classID,byte[] obis,int attributeID ){
		this();
		setValues(classID,obis,attributeID);
	}
	
	public CosemAttributeDescriptor(int classID,String obisString,int attributeID ){
		this();
		setAttributeDescriptor(classID,obisString,attributeID);
	}

	public void setAttributeDescriptor(int classID,String obisString,int attributeID){
		setValues(classID,obisString,attributeID);
	}
	
	public void setValues(int classID,byte[] obis,int attributeID){
		setClassId(classID);
		setInstanceId(obis);
		setAttributeId(attributeID);
	}
	
	public void setValues(int classID,String obisString,int attributeID){
		byte[] obis = CosemObis.parseObis(obisString);
		setValues(classID,obis,attributeID);
	}
	
	public void setClassId(int classID ){
		classId.setValue(classID);
	}
	
	public int getClassId(){
		if( classId.isDecodeDone() )
			return classId.getUnsignedInt16();
		return -1;
	}
	
	public void setInstanceId(byte[] instanceID ){
		instanceId.setValue(instanceID);
	}
	
	public void setInstanceId(String instanceID ){
		instanceId.setValue(CosemObis.parseObis(instanceID));
	}
	
	public byte[] getInstanceId(){
		if( instanceId.isDecodeDone() )
			return instanceId.getValue();
		return null;
	}
	
	public String getInstanceIdAsString(){
		byte[] iid = getInstanceId();
		if( null == iid )
			return "";
		return CosemObis.OidToString(iid);
	}

	public void setAttributeId(int attrId ){
		attributeId.setValue(attrId);
	}
	
	public int getAttributeId(){
		if( attributeId.isDecodeDone() )
			return attributeId.getUnsignedInt8();
		return -1;
	}

	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "CosemAttributeDescriptor EMPTY";
		StringBuilder sb = new StringBuilder(512);
		sb.append(" <attributeDesc classid=\"");
		sb.append(getClassId()).append("\" instanceid=\"");
		sb.append(CosemObis.OidToString(getInstanceId()));
		sb.append("\" attributeid=\"").append(getAttributeId());
		sb.append("\"/>");
		return sb.toString();
	}
}
