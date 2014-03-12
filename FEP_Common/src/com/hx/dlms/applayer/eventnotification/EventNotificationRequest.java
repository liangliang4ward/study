/**
 * EventNotificationRequest ::= SEQUENCE
 {
 	time OCTET STRING OPTIONAL,
	cosem-attribute-descriptor Cosem-Attribute-Descriptor,
	attribute-value Data
 }
 */
package com.hx.dlms.applayer.eventnotification;

import java.util.Date;

import com.hx.dlms.ASN1Constants;
import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.DlmsDateTime;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.applayer.CosemAttributeDescriptor;

public class EventNotificationRequest extends ASN1Sequence {
	private static final long serialVersionUID = 6529638639614326072L;
	private final ASN1OctetString time = new ASN1OctetString();
	private final CosemAttributeDescriptor attributeDescriptor = new CosemAttributeDescriptor();
	private final DlmsData attributeValue = new DlmsData();

	public EventNotificationRequest(){
		this.forceEncodeTag(true);
		TagAdjunct adjunct = TagAdjunct.contextSpecificImplicit( 194 );
		adjunct.axdrCodec(true);
		this.setTagAdjunct(adjunct);	 // [194] IMPLICIT EventNotificationRequest
		
		members = new ASN1Type[]{ time ,attributeDescriptor, attributeValue };
		time.setOptional(true);
		time.fixedLength(12);
		
		codec(ASN1Constants.AXDR_CODEC);
	}

	public final ASN1OctetString getOctetTime() {
		return time;
	}
	
	public String getDateTime(){
		if( !time.isDecodeDone() || null == time.getValue() )
			return "";
		byte[] date = time.getValue();
		DlmsDateTime dateTime = new DlmsDateTime();
		dateTime.setDlmsDataValue(date, 0);
		return dateTime.toString();
	}
	
	public final Date getTime(){
		if( !time.isDecodeDone() || null == time.getValue() )
			return null;
		byte[] tm = time.getValue();
		DlmsDateTime datetime = new DlmsDateTime();
		datetime.setDlmsDataValue(tm, 0);
		return datetime.getDate();
	}

	public final CosemAttributeDescriptor getAttributeDescriptor() {
		return attributeDescriptor;
	}

	public final DlmsData getAttributeValue() {
		return attributeValue;
	}

}
