/**
Action-Request-With-List-And-First-Pblock ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	cosem-method-descriptor-list SEQUENCE OF Cosem-Method-Descriptor,
	pblock DataBlock-SA
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1ObjectFactory;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.CosemMethodDescriptor;
import com.hx.dlms.applayer.DataBlockSA;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ActionRequestWithListFirstPblock extends ASN1Sequence {
	private static final long serialVersionUID = 603129620298760826L;
	private static final ASN1ObjectFactory methodFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new CosemMethodDescriptor();	}
	};
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1SequenceOf methodList = new ASN1SequenceOf(methodFactory);
	protected DataBlockSA pblock = new DataBlockSA();

	public ActionRequestWithListFirstPblock(){
		super();
		members = new ASN1Type[]{ invokeIdPriority,methodList,pblock };
		setInvokeId(1);
	}
	
	public void setMethodList(CosemMethodDescriptor[] methods){
		methodList.setInitValue(methods);
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
	
	public void setBlock(byte[] data){
		pblock.setFirstBlock(data);
	}
}
