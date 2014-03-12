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
 *  HexingEx-Response-Transparent  :: = octet-string
 */
public class HexingExResponseTransparent extends ASN1Sequence{

	
	private DlmsData transparentValue =new DlmsData();
	
	public HexingExResponseTransparent (){
		super();
		members = new ASN1Type[]{ transparentValue };
	}

	public HexingExResponseTransparent(DlmsData data){
		this();
		setTransparentValue(data);
	}
	
	public void setTransparentValue(DlmsData data){
		this.transparentValue.assignValue(data);
	}
	
	public DlmsData getData(){
		return transparentValue;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1697619023851292090L;
	@Override
	public final String toString(){
		if( !isDecodeDone() )
			return "<HexingExResponseTransparent/>";
		StringBuilder sb = new StringBuilder(512);
		sb.append("<HexingExResponseTransparent \n\tvalue=\"");
		sb.append(this.transparentValue);
		sb.append("\">");
		sb.append("\r\n</HexingExResponseTransparent>");
		return sb.toString();
	}
	
	
}
