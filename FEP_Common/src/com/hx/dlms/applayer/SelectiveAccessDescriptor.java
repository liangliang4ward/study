package com.hx.dlms.applayer;

import java.io.IOException;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.ASN1UnsignedInt8;
import com.hx.dlms.DlmsData;

public class SelectiveAccessDescriptor extends ASN1Sequence {
	private static final long serialVersionUID = -7746309400835204861L;
	private ASN1UnsignedInt8 accessSelector = new ASN1UnsignedInt8();
	private DlmsData parameter = new DlmsData();
	
	public SelectiveAccessDescriptor(){
		this.setAxdrCodec();
		members = new ASN1Type[]{ accessSelector,parameter };
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
	}
	
	public void setParameter(int selector, DlmsData data){
		accessSelector.setValue(selector);  
		parameter.assignValue(data) ;
	}
	/**
	 * 老表规约用的到.
	 */
	public void setParameter(DlmsData data){ //老表规约设置
		parameter.assignValue(data) ;
		parameter.isEncodeTag = false;  //对于老表，这里设置为false
	}

	public final ASN1UnsignedInt8 getAccessSelector() {
		return accessSelector;
	}

	public final DlmsData getParameter() {
		return parameter;
	}
	
	/**
	 * 根据选择时间段读取时Access-selector=0x01(range_descriptor)，range_descriptor对应的Access-parameters的定义为：
Structure {
	Restricting_object:  capture_object_definition
	From_value  date_time
	To_value:    date_time
	Selected_values:  array capture_object_definition
}
Capture_object_definition ::=structure {
	Class_id:       u16
	Logical_name   octet-string
	Attribute_index:  integer
	Data_index      u16
}

	 */
	public final void selectByPeriodOfTime(String timeFrom, String timeTo){
		accessSelector.setValue(1);
		//TIME object desc.
		CaptureObjectDefinition capObj = new CaptureObjectDefinition(8,"0.0.1.0.0.255",2);
		DlmsData[] structItems = new DlmsData[] { capObj,new DlmsData(),new DlmsData(),new DlmsData() };
		structItems[1].setDlmsDateTime(timeFrom);
		structItems[2].setDlmsDateTime(timeTo);
		try {
			structItems[3].setArray((DlmsData[])null);
		} catch (IOException e) {
		}
		parameter.setStructure(new ASN1SequenceOf(structItems));
	}
	
	public final void selectByPeriodOfTime(String timeFrom, String timeTo,boolean isOldProtocol){
		if(isOldProtocol){
			accessSelector.setValue(1);
			//TIME object desc.
			CaptureObjectDefinition capObj = new CaptureObjectDefinition(8,"0.0.1.0.0.255",2);
			DlmsData[] structItems = new DlmsData[] { capObj,new DlmsData(),new DlmsData(),new DlmsData() };
			structItems[1].setOldDlmsDateTime(timeFrom);
			structItems[2].setOldDlmsDateTime(timeTo);
			ASN1SequenceOf struct = new ASN1SequenceOf(structItems);
			struct.isEncodeLength = false;
			parameter.setStructure(struct);
		}else{
			selectByPeriodOfTime(timeFrom, timeTo);
		}

	}
	/**
当读取详细事件记录时，可以认为是根据索引读Access-selector=0x02(entry_descriptor)，entry_descriptor对应的
access-parameters的定义为： Structure {
							From_entry 起始索引/起始行  u32
							To_entry 结束索引/结束行    u32
							From_selected_value 起始列  u16
							To_selected_value 结束列   u16
}
	 * @param fromIndex
	 * @param toIndex
	 */
	public final void selectByIndex(int fromIndex, int toIndex){
		accessSelector.setValue(2);
		DlmsData[] structItems = new DlmsData[] { new DlmsData(),new DlmsData(),new DlmsData(),new DlmsData() };
		structItems[0].setDoubleLongUnsigned(fromIndex);
		structItems[1].setDoubleLongUnsigned(toIndex);
		structItems[2].setUnsignedLong(0);
		structItems[3].setUnsignedLong(0);
		parameter.setStructure(new ASN1SequenceOf(structItems));
	}
	
	@Override
	public String toString(){
		if( null==value )
			return " <SelectiveAccessDescriptor value=\"not present\"/>";
		StringBuilder sb = new StringBuilder(64);
		sb.append(" <SelectiveAccessDescriptor selector=\"");
		sb.append(accessSelector.getUnsignedInt8());
		sb.append("\" param=\"");
		sb.append(parameter).append("\"/>");
		return sb.toString();
	}
}
