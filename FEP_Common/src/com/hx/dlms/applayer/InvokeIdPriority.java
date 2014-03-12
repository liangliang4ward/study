/**
 * The client is allowed to send several requests before receiving the response to the 
 * previous ones. Therefore ¨C to be able to identify which response corresponds to 
 * each request ¨C it is necessary to include a reference in the request.
 * 
 * The value of this parameter is assigned by the client so that each request carries 
 * a different Invoke_Id. The server shall copy the Invoke_Id into the corresponding response.
 * 
 */
package com.hx.dlms.applayer;

import com.hx.dlms.ASN1UnsignedInt8;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 * 
 * bit0-bit3 is invoke id( sequence number); 
 * bit4-bit5: reserved; 
 * bit6: service class --- 0 = Unconfirmed, 1 = Confirmed
 * bit7: priority  --- 0 = normal, 1 = high
 */
public class InvokeIdPriority extends ASN1UnsignedInt8 {
	private static final long serialVersionUID = 3280754627323106515L;

	public InvokeIdPriority(){
		super();
		this.setAxdrCodec();
	}
	
	public void assignValue(InvokeIdPriority id){
		if( null != id.value )
			initValue = id.getUnsignedInt8();
		else
			initValue = id.initValue;
	}
	
	public int getInvokeId(){
		if( this.isDecodeDone() ){
			return getUnsignedInt8() & 0x0F;
		}
		else if( null != initValue )
			return initValue.intValue() & 0x0F;
		else
			return 0;
	}
	
	public void setInvokeId(int invokeId){
		if( null != initValue ){
			initValue = (initValue.intValue() & 0xF0 ) | ( 0x0F & invokeId );
		}
		else
			initValue = 0x0F & invokeId;
	}
	
	public boolean isConfirmed(){
		if( this.isDecodeDone() ){
			return (getUnsignedInt8() & 0x40) != 0;
		}
		else if( null != initValue )
			return (initValue.intValue() & 0x40) != 0;
		else
			return false;
	}
	
	public void setConfirmed(){
		setConfirmed(true);
	}

	public void setConfirmed(boolean confirmed){
		int confirm = confirmed ? 0x40 : 0;
		if( null != initValue ){
			initValue = (initValue.intValue() & 0xBF ) | confirm;
		}
		else
			initValue = confirm;
	}
	
	public boolean isPriorityHigh(){
		if( this.isDecodeDone() ){
			return (getUnsignedInt8() & 0x80) != 0;
		}
		else if( null != initValue )
			return (initValue.intValue() & 0x80) != 0;
		else
			return false;
	}
	
	public void setPriorityHigh(){
		setPriorityHigh(true);
	}

	public void setPriorityHigh(boolean priorityHigh){
		int high = priorityHigh ? 0x80 : 0;
		if( null != initValue ){
			initValue = (initValue.intValue() & 0x7F ) | high;
		}
		else
			initValue = high;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(64);
		sb.append(" invokeId='");
		sb.append(getInvokeId()).append("' confirm='");
		sb.append(isConfirmed()).append("' priority='");
		if( isPriorityHigh() )
			sb.append("high' ");
		else
			sb.append("normal' ");
		return sb.toString();
	}
}
