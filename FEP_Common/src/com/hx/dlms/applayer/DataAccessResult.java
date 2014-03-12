package com.hx.dlms.applayer;

import java.io.Serializable;


public enum DataAccessResult implements Serializable {
	NOT_APPLICABLE("N/A",-1),
	SUCCESS("success",0),
	HARDWARE_FAULT("hardware-fault",1),
	TEMP_FAILURE("temporary-failure",2),
	READ_WRITE_DENY("read-write-denied",3),
	OBJECT_UNDEFINED("object-undefined",4),
	CLASS_INCONSISTENT("object-class-inconsistent",9),
	OBJECT_UNAVAILABLE("object-unavailable",11),
	TYPE_UNMATCH("type-unmatched",12),
	ACCESS_VIOLATED("scope-of-access-violated",13),
	BLOCK_UNAVAILABLE("data-block-unavailable",14),
	GET_ABORTED("long-get-aborted",15),
	NO_GET_INPROGRESS("no-long-get-in-progress",16),
	SET_ABORTED("long-set-aborted",17),
	NO_SET_INPROGRESS("no-long-set-in-progress",18),
	BLOCK_NUMBER_INVALID("data-block-number-invalid",19),
	OTHER_REASON("other-reason",250);

	private String desc = null;
	private int result = 255;
	
	private DataAccessResult(String name,int value){
		desc = name;
		result = value;
	}
	
	public int toInt(){
		return this.result;
	}

	@Override
	public String toString() {
		return desc;
	}
	
	public static final DataAccessResult parseResult(int resultValue){
		DataAccessResult ret = NOT_APPLICABLE;
		switch(resultValue){
		case -1: ret = NOT_APPLICABLE; break;
		case 0:	ret = SUCCESS; break;
		case 1:	ret = HARDWARE_FAULT; break;
		case 2:	ret = TEMP_FAILURE; break;
		case 3:	ret = READ_WRITE_DENY; break;
		case 4:	ret = OBJECT_UNDEFINED; break;
		case 9:	ret = CLASS_INCONSISTENT; break;
		case 11:	ret = OBJECT_UNAVAILABLE; break;
		case 12:	ret = TYPE_UNMATCH; break;
		case 13:	ret = ACCESS_VIOLATED; break;
		case 14:	ret = BLOCK_UNAVAILABLE; break;
		case 15:	ret = GET_ABORTED; break;
		case 16:	ret = NO_GET_INPROGRESS; break;
		case 17:	ret = SET_ABORTED; break;
		case 18:	ret = NO_SET_INPROGRESS; break;
		case 19:	ret = BLOCK_NUMBER_INVALID; break;
		case 250:	ret = OTHER_REASON; break;
		}
		return ret;
	}
}
