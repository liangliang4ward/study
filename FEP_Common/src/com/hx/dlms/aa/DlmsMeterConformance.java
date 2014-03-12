package com.hx.dlms.aa;

import java.io.Serializable;

import com.hx.dlms.ASN1BitString;

public class DlmsMeterConformance implements Serializable {
	private static final long serialVersionUID = -2759546138316786267L;

	protected int maxPduSize = 70;	//Default size is 512.
	
	protected boolean attribute0Set = false;
	protected boolean attribute0Get = false;
	protected boolean priorityMgmt = false;
	protected boolean blockGet = false;
	protected boolean blockSet = false;
	protected boolean blockAction = false;
	protected boolean multipleReference = false;
	protected boolean canGet = false;
	protected boolean canSet = false;
	protected boolean canAction = false;
	protected boolean selectiveAccess = false;
	protected boolean eventNotification = false;

	public DlmsMeterConformance(){
	}
	
	public DlmsMeterConformance(ASN1BitString bits){
		setConformance(bits);
	}
	
	protected void update(DlmsMeterConformance src){
		maxPduSize = src.maxPduSize;
		
		attribute0Set = src.attribute0Set;
		priorityMgmt = src.priorityMgmt;
		attribute0Get = src.attribute0Get;

		blockGet = src.blockGet;
		blockSet = src.blockSet;
		blockAction = src.blockAction;
		multipleReference = src.multipleReference;

		canGet = src.canGet;
		canSet = src.canSet;
		canAction = src.canAction;
		selectiveAccess = src.selectiveAccess;
		eventNotification = src.eventNotification;
	}
	
	public void setConformance(ASN1BitString bits){
		byte[] v = bits.getValue();
		if( null == v || v.length != 4 )
			return;
		attribute0Set = bits.getBitValue(8);
		priorityMgmt = bits.getBitValue(9);
		attribute0Get = bits.getBitValue(10);

		blockGet = bits.getBitValue(11);
		blockSet = bits.getBitValue(12);
		blockAction = bits.getBitValue(13);
		multipleReference = bits.getBitValue(14);

		canGet = true; //bits.getBitValue(19);
		canSet = true; //bits.getBitValue(20);
		canAction = true; //bits.getBitValue(23);
		selectiveAccess = bits.getBitValue(21);
		eventNotification = bits.getBitValue(22);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Meter Abilities: maxPduSize=").append(maxPduSize);
		if( attribute0Set )
			sb.append(", attribute0-supported-with-set");
		if( attribute0Get )
			sb.append(", attribute0-supported-with-get");
		if( priorityMgmt )
			sb.append(", priority-mgmt-supported");

		if( blockGet )
			sb.append(", block-transfer-with-get-or-read");
		if( blockSet )
			sb.append(", block-transfer-with-set-or-write");
		if( blockAction )
			sb.append(", block-transfer-with-action");

		if( multipleReference )
			sb.append(", multiple-references");
		if( selectiveAccess )
			sb.append(", selective-access");

		if( canGet )
			sb.append(", get");
		if( canSet )
			sb.append(", set");
		if( canAction )
			sb.append(", action");
		if( eventNotification )
			sb.append(", event-notification");
		return sb.toString();
	}
	
	public final int getMaxPduSize() {
		return maxPduSize;
	}
	
	public final void setMaxPduSize(int size){
		if( size>0 )
			maxPduSize = size;
	}
	
	public final boolean isAttribute0Set() {
		return attribute0Set;
	}
	public final boolean isAttribute0Get() {
		return attribute0Get;
	}
	public final boolean isPriorityMgmt() {
		return priorityMgmt;
	}
	public final boolean isBlockGet() {
		return blockGet;
	}
	public final boolean isBlockSet() {
		return blockSet;
	}
	public final boolean isBlockAction() {
		return blockAction;
	}
	public final boolean isMultipleReference() {
		return multipleReference;
	}
	public final boolean isCanGet() {
		return canGet;
	}
	public final boolean isCanSet() {
		return canSet;
	}
	public final boolean isCanAction() {
		return canAction;
	}
	public final boolean isSelectiveAccess() {
		return selectiveAccess;
	}
	public final boolean isEventNotification() {
		return eventNotification;
	}

}
