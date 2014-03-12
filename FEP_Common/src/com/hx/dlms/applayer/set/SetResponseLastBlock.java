/**
Set-Response-Last-Datablock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	result Data-Access-Result,
	block-number Unsigned32
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1Enum;
import com.hx.dlms.ASN1Integer;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.DataAccessResult;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetResponseLastBlock extends ASN1Sequence {
	private static final long serialVersionUID = -6821168135419716422L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1Integer blockNumber = new ASN1Integer();
	protected ASN1Enum accessResult = new ASN1Enum();

	public SetResponseLastBlock(){
		super();
		
		blockNumber.fixedLength(4);
		members = new ASN1Type[]{ invokeIdPriority,accessResult,blockNumber };
	}
	
	public SetResponseLastBlock(int invokeId, DataAccessResult result,int blockNum){
		this();
		setInvokeId(invokeId);
		setAccessResult(result);
		setBlockNumber(blockNum);
	}

	public int getInvokeId(){
		return this.invokeIdPriority.getInvokeId();
	}
	
	public void setInvokeId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
	}
	
	public void setConfirmed(boolean confirmed){
		invokeIdPriority.setConfirmed(confirmed);
	}
	
	public boolean isConfirmed(){
		return invokeIdPriority.isConfirmed();
	}
	
	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}
	
	public final void setAccessResult(DataAccessResult dataAccessResult){
		accessResult.setValue(dataAccessResult.toInt());
	}
	
	public final DataAccessResult getAccessResult(){
		return DataAccessResult.parseResult(accessResult.getEnumValue());
	}
	
	public final ASN1Enum getAccessResultEnum(){
		return accessResult;
	}
	
	public final void setBlockNumber(int blockNum){
		blockNumber.setValue(blockNum);
	}
	
	public final int getBlockNumber(){
		return blockNumber.getInt();
	}
		
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<SetResponseLastBlock value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<SetResponseLastBlock").append(this.invokeIdPriority);
		sb.append(">\r\n\t");
		sb.append("<block-number value=\"");
		sb.append(getBlockNumber());
		sb.append("\"/>");
		sb.append("\r\n\t<AccessResult result=\"");
		sb.append(getAccessResult());
		sb.append("\"/>");
		sb.append("\r\n</SetResponseLastBlock>");
		return sb.toString();
	}
}
