/**
Capture_object_definition ::=structure {
	Class_id:       u16
	Logical_name   octet-string
	Attribute_index:  integer
	Data_index      u16
}

 */
package com.hx.dlms.applayer;

import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.DlmsData;

/**
 * @author Bao Hongwei
 *
 */
public class CaptureObjectDefinition extends DlmsData {
	private static final long serialVersionUID = -3540449318408042474L;

	public CaptureObjectDefinition(int clsId,String obisStr,int attrId){
		this(clsId,obisStr,attrId,0);
	}
	
	public CaptureObjectDefinition(int clsId,String obisStr,int attrId, int dataIndex){
		DlmsData[] structItems = new DlmsData[] { new DlmsData(),new DlmsData(),new DlmsData(),new DlmsData() };
		structItems[0].setUnsignedLong(clsId);
		structItems[1].setOctetString(CosemObis.parseObis(obisStr));
		structItems[2].setDlmsInteger((byte)attrId);
		structItems[3].setUnsignedLong(dataIndex);
		ASN1SequenceOf myStruct = new ASN1SequenceOf(structItems);
		this.setStructure(myStruct);
	}
}
