/**
Set-Response-With-List ::= SEQUENCE
{
	invoke-id-and-priority Invoke-Id-And-Priority,
	result SEQUENCE OF Data-Access-Result
}
 */
package com.hx.dlms.applayer.set;

import com.hx.dlms.ASN1Enum;
import com.hx.dlms.ASN1ObjectFactory;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1SequenceOf;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.applayer.DataAccessResult;
import com.hx.dlms.applayer.InvokeIdPriority;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class SetResponseWithList extends ASN1Sequence {
	private static final long serialVersionUID = -5855314754061932163L;

	private static final ASN1ObjectFactory resultFactory = new ASN1ObjectFactory(){
		public ASN1Type create() { return new ASN1Enum();	}
	};
	
	protected InvokeIdPriority invokeIdPriority = new InvokeIdPriority();
	protected ASN1SequenceOf resultList = new ASN1SequenceOf(resultFactory);
	
	public SetResponseWithList(){
		super();
		members = new ASN1Type[]{ invokeIdPriority,resultList };
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
	
	public final void setResultList(DataAccessResult[] results){
		if( null == results || results.length == 0 )
			return;
		ASN1Enum[] list = new ASN1Enum[results.length];
		for(int i=0; i<list.length; i++ ){
			list[i] = new ASN1Enum();
			list[i].setValue(results[i].toInt());
		}
		resultList.setInitValue(list);
	}
	
	public final DataAccessResult[] getResultList(){
		ASN1Type[] daResults = resultList.getMembers(); 
		if( null == daResults )
			return null;
		DataAccessResult[] list = new DataAccessResult[daResults.length];
		for(int i=0; i<list.length; i++ ){
			ASN1Enum aresult = (ASN1Enum)daResults[i];
			list[i] = DataAccessResult.parseResult(aresult.getEnumValue());
		}
		return list;
	}
	
	public final ASN1SequenceOf getResultListSequence(){
		return resultList;
	}

	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<SetResponseWithList value='EMPTY'/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<SetResponseWithList").append(this.invokeIdPriority);
		sb.append(">\r\n\t");
		
		DataAccessResult[] resultArray = getResultList();
		int quantity = null==resultArray ? 0 : resultArray.length;
		sb.append("<result-list quantity=\"").append(quantity).append("\">");
		for( int i=0; i<quantity; i++ ){
			sb.append("\r\n\t\t");
			sb.append(resultArray[i]);
		}
		sb.append("\r\n\t</result-list>");

		sb.append("\r\n</SetResponseWithList>");
		return sb.toString();
	}
}
