/**
Action-Request-With-List ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	cosem-method-descriptor-list SEQUENCE OF Cosem-Method-Descriptor,
	method-invocation-parameters SEQUENCE OF Data
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1ObjectFactory;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.applayer.CosemMethodDescriptor;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ActionRequestWithList extends ASN1Sequence {
	private static final long serialVersionUID = 5314888368110304309L;
	private static final ASN1ObjectFactory methodFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new CosemMethodDescriptor();	}
	};
	private static final ASN1ObjectFactory parameterFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new DlmsData();	}
	};
	
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1SequenceOf methodList = new ASN1SequenceOf(methodFactory);
	protected ASN1SequenceOf parameterList = new ASN1SequenceOf(parameterFactory);

	public ActionRequestWithList(){
		super();
		members = new ASN1Type[]{ invokeIdPriority,methodList,parameterList };
		setInvokeId(1);
	}
	
	public ActionRequestWithList(CosemMethodDescriptor[] methods,DlmsData[] params){
		this();
		methodList.setInitValue(methods);
		parameterList.setInitValue(params);
	}
	
	public void setMethodList(CosemMethodDescriptor[] methods){
		methodList.setInitValue(methods);
	}
	
	public void setParameterList(DlmsData[] params){
		parameterList.setInitValue(params);
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
}
