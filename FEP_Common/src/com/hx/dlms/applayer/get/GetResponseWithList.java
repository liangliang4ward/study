package com.hx.dlms.applayer.get;

import com.hx.dlms.ASN1ObjectFactory;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.applayer.InvokeIdPriority;

public class GetResponseWithList extends ASN1Sequence {
	private static final long serialVersionUID = -6831157467509222792L;

	private static final ASN1ObjectFactory dataFactory = new ASN1ObjectFactory(){
		public ASN1Type create() {
			return new GetDataResult();
		}
	};
	
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1SequenceOf result = new ASN1SequenceOf(dataFactory);
	
	public GetResponseWithList(){
		TagAdjunct adjunct = TagAdjunct.contextSpecificImplicit(3);
		adjunct.axdrCodec(true);
		this.setTagAdjunct(adjunct);
		members = new ASN1Type[]{ invokeIdPriority,result };
		
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
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
	
	public GetDataResult[] getResultList(){
		ASN1Type[] resultArray = result.getMembers();
		if( ! result.isDecodeDone() || null == resultArray )
			return null;
		GetDataResult[] ret = new GetDataResult[resultArray.length];
		for(int i=0; i<ret.length; i++ ){
			ret[i] = (GetDataResult)resultArray[i];
		}
		return ret;
	}

	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<GetResponseWithList value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<GetResponseWithList invokeId=\"");
		sb.append(getInvokeId()).append("\" confirm=\"").append(this.isConfirmed());
		sb.append("\" priority=\"");
		if( this.isPriorityHigh() )
			sb.append("high\">\r\n\t");
		else
			sb.append("normal\">\r\n\t");
		GetDataResult[] myResult = getResultList();
		sb.append("<list>");
		for( int i=0; null != myResult && i<myResult.length; i++ ){
			sb.append("\r\n\t\t");
			sb.append(myResult[i]);
		}
		sb.append("\r\n\t</list>");
		sb.append("\r\n</GetResponseWithList>");
		return sb.toString();
	}
}
