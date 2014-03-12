/**
Set-Request-With-List-And-First-Datablock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	attribute-descriptor-list SEQUENCE OF Cosem-Attribute-Descriptor-With-Selection,
	datablock DataBlock-SA
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1ObjectFactory;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.CosemAttributeDescriptorSelection;
import com.hx.dlms.applayer.DataBlockSA;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetRequestWithListFirstBlock extends ASN1Sequence {
	private static final long serialVersionUID = 6435171592103102892L;
	private static final ASN1ObjectFactory attributeTypeFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new CosemAttributeDescriptorSelection();	}
	};
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1SequenceOf attributeList = new ASN1SequenceOf(attributeTypeFactory);
	protected DataBlockSA dataBlock = new DataBlockSA();

	public SetRequestWithListFirstBlock(){
		super();
		
		members = new ASN1Type[]{ invokeIdPriority,attributeList,dataBlock };
		setInvokeId(1);
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
	
	public final void setDataBlock(boolean lastBlock,int blockNum,byte[] rawData){
		dataBlock.setLastBlock(lastBlock);
		dataBlock.setBlockNumber(blockNum);
		dataBlock.setRawData(rawData);
	}
}
