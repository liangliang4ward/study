/**
 * 	Get-Request-Next ::= SEQUENCE
	{
		invoke-id-and-priority Invoke-Id-And-Priority,
		block-number Unsigned32
	}
 */
package com.hx.dlms.applayer.get;

import com.hx.dlms.ASN1Integer;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class GetRequestNext extends ASN1Sequence {
	private static final long serialVersionUID = 4295638020469361966L;
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1Integer blockNumber = new ASN1Integer();
	
	public GetRequestNext(){
		blockNumber.fixedLength(4);
		members = new ASN1Type[]{ invokeIdPriority,blockNumber };
	}
	
	public GetRequestNext(InvokeIdPriority id, int blockNum){
		this();
		invokeIdPriority.assignValue(id);
		blockNumber.setValue(blockNum);
	}
	
	public void setInvokeId(int frameSeq){
		invokeIdPriority.setInvokeId(frameSeq);
	}
	
	public int getInvokeId(){
		return this.invokeIdPriority.getInvokeId();
	}
	
	public void setConfirmed(boolean confirmed){
		invokeIdPriority.setConfirmed(confirmed);
	}
	
	public boolean isConfirmed(){
		return invokeIdPriority.isConfirmed();
	}
	
	public void setBlockNumber( int blockNum ){
		blockNumber.setValue(blockNum);
	}
	
	public int getBlockNumber(){
		return blockNumber.getInt();
	}
	
	public boolean isPriorityHigh(){
		return invokeIdPriority.isPriorityHigh();
	}
	
	public void setPriorityHigh(boolean priorityHigh){
		invokeIdPriority.setPriorityHigh(priorityHigh);
	}

	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<GetRequestNext/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<GetRequestNext invokeId=\"");
		sb.append(getInvokeId()).append("\" confirm=\"").append(this.isConfirmed());
		sb.append("\" priority=\"");
		if( this.isPriorityHigh() )
			sb.append("high\" blockNumber=\"");
		else
			sb.append("normal\" blockNumber=\"");
		
		sb.append(getBlockNumber()).append("\" />");
		return sb.toString();
	}
}
