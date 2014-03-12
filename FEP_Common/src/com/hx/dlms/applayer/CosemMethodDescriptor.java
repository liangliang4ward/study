/**
Cosem-Method-Descriptor ::= SEQUENCE
{
	class-id Cosem-Class-Id,
	instance-id Cosem-Object-Instance-Id,
	method-id Cosem-Object-Method-Id
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
public class CosemMethodDescriptor extends ASN1Sequence {
	private static final long serialVersionUID = -6400773888169810754L;
	private ASN1UnsignedInt16 classId = new ASN1UnsignedInt16();
	private ASN1OctetString instanceId = new ASN1OctetString(6);
	private ASN1Int8 methodId = new ASN1Int8();	//Cosem-Object-Method-Id ::= Integer8

	public CosemMethodDescriptor(){
		this.setAxdrCodec();
		members = new ASN1Type[]{ classId,instanceId,methodId };
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
	}
	
	public CosemMethodDescriptor(int classID,String obisString,int method){
		this();
		setValues(classID,obisString,method);
	}
	
	public CosemMethodDescriptor(int classID,byte[] obis,int method){
		this();
		setValues(classID,obis,method);
	}
	
	public void setValues(int classID,String obisString,int method){
		byte[] obis = CosemObis.parseObis(obisString);
		setValues(classID,obis,method);
	}
	
	public void setValues(int classID,byte[] obis,int method){
		setClassId(classID);
		setInstanceId(obis);
		setMethodId(method);
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

	public void setMethodId(int method ){
		methodId.setValue(method);
	}
	
	public int getMethodId(){
		if( methodId.isDecodeDone() )
			return methodId.getUnsignedInt8();
		return -1;
	}

}
