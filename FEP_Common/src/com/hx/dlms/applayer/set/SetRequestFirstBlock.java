/**
Set-Request-With-First-Datablock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	cosem-attribute-descriptor Cosem-Attribute-Descriptor,
	access-selection Selective-Access-Descriptor OPTIONAL,
	datablock DataBlock-SA
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.applayer.CosemAttributeDescriptor;
import com.hx.dlms.applayer.DataBlockSA;
import com.hx.dlms.applayer.InvokeIdPriority;
import com.hx.dlms.applayer.SelectiveAccessDescriptor;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 */

public class SetRequestFirstBlock extends ASN1Sequence {
	private static final long serialVersionUID = -2177617322357238554L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected CosemAttributeDescriptor attributeDescriptor = new CosemAttributeDescriptor();
	protected SelectiveAccessDescriptor selectiveAccess = new SelectiveAccessDescriptor();
	protected DataBlockSA dataBlock = new DataBlockSA();

	public SetRequestFirstBlock(){
		super();
		
		selectiveAccess.setOptional(true);
		members = new ASN1Type[]{ invokeIdPriority,attributeDescriptor,selectiveAccess, dataBlock };
	}
	
	public SetRequestFirstBlock( int frameSeq, int classID, byte[] obis, int attributeID, byte[] attributeRawData ){
		this();
		setInvokeId(frameSeq);
		setDataBlock(false,1,attributeRawData);
		attributeDescriptor.setValues(classID, obis, attributeID);
	}

	public void setAttribute( int classId,byte[] obis, int attributeId ){
		attributeDescriptor.setValues(classId, obis, attributeId);
	}

	public final void setSelectiveAccess(int selector,DlmsData data){
		selectiveAccess.setParameter(selector, data);
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
	
	public final void setDataBlock(boolean lastBlock,int blockNum,byte[] rawData){
		dataBlock.setLastBlock(lastBlock);
		dataBlock.setBlockNumber(blockNum);
		dataBlock.setRawData(rawData);
	}

	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<SetRequestFirstBlock value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<SetRequestFirstBlock");
		sb.append(this.invokeIdPriority);
		sb.append(">\r\n\t");
		sb.append(this.attributeDescriptor);
		sb.append("\r\n\t");
		sb.append(this.selectiveAccess);
		sb.append("\r\n\t");
		sb.append(this.dataBlock);
		sb.append("\r\n</SetRequestFirstBlock>");
		return sb.toString();
	}
}
