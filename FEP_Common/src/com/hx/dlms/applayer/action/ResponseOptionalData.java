/**
Action-Response-With-Optional-Data ::= SEQUENCE
{
	result Action-Result,
	return-parameters Get-Data-Result OPTIONAL
}
 */
package com.hx.dlms.applayer.action;

import com.hx.dlms.ASN1Enum;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DlmsData;
import com.hx.dlms.applayer.ActionResult;
import com.hx.dlms.applayer.get.GetDataResult;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class ResponseOptionalData extends ASN1Sequence {
	private static final long serialVersionUID = 1865674242202473152L;
	protected ASN1Enum actionResult = new ASN1Enum();
	protected GetDataResult returnParameters = new GetDataResult();
	
	public ResponseOptionalData(){
		returnParameters.setOptional(true);
		members = new ASN1Type[]{ actionResult,returnParameters };
	}
	
	public void setActionResult(ActionResult result){
		actionResult.setValue(result.toInt());
	}
	
	public ActionResult getActionResult(){
		return ActionResult.parseResult(actionResult.getEnumValue());
	}
	
	public final ASN1Enum getActionResultEnum(){
		return actionResult;
	}
	
	public GetDataResult getReturnParameters(){
		return returnParameters;
	}

	public void setReturnData(DlmsData returnData){
		returnParameters.setData(returnData);
	}
}
