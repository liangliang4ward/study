package com.hx.dlms.applayer.ex;

import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;

/**
 * 
 * @author gaoll
 *
 * @time 2013-5-11 ÉÏÎç09:59:53
 *
 * @info
 *  HexingEx-Request-Transparent  :: = octet-string
 */
public class HexingExRequestTransparent extends ASN1Sequence{

	
	private DlmsData transparentValue =new DlmsData();
	
	public HexingExRequestTransparent (){
		super();
		members = new ASN1Type[]{ transparentValue };
	}

	public HexingExRequestTransparent(DlmsData data){
		this();
		setTransparentValue(data);
	}
	
	public void setTransparentValue(DlmsData data){
		this.transparentValue.assignValue(data);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1697619023851292090L;
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<HexingExRequestTransparent/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<HexingExRequestTransparent \n\tvalue=\"");
		sb.append(this.transparentValue);
		sb.append("\">");
		sb.append("\r\n</HexingExRequestTransparent>");
		return sb.toString();
	}
	
	
}
