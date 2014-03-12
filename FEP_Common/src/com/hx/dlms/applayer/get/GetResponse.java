package com.hx.dlms.applayer.get;

import java.io.IOException;

import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.TagAdjunct;
/**
 * Get-Response ::= CHOICE
{
	get-response-normal [1] IMPLICIT Get-Response-Normal,
	get-response-with-datablock [2] IMPLICIT Get-Response-With-Datablock,
	get-response-with-list [3] IMPLICIT Get-Response-With-List
}
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class GetResponse extends ASN1Choice {
	private static final long serialVersionUID = 4537798261122970033L;
	private GetResponseNormal normalResponse = new GetResponseNormal();
	private GetResponseWithBlock blockResponse = new GetResponseWithBlock();
	private GetResponseWithList listResponse = new GetResponseWithList();
	
	public GetResponse(){
		this.setAxdrCodec();
		TagAdjunct adjunct = TagAdjunct.contextSpecificImplicit(196);
		adjunct.axdrCodec(true);
		this.setTagAdjunct(adjunct);	 // [196] IMPLICIT GET-Response

		normalResponse.codec(this.codec());
		this.addMember(normalResponse);
		blockResponse.codec(this.codec());
		this.addMember(blockResponse);
		listResponse.codec(this.codec());
		this.addMember(listResponse);
	}
	
	public void setResponse(ASN1Type resp){
		this.choose(resp);
	}
	
	@Override
	public String toString(){
		if( ! this.isDecodeDone() )
			return "GetResponse EMPTY";
		return selectedObject.toString();
	}
	
	/**
C40181
00
0932
01020304050607080910111213141516
17181920212223242526272829303132
33343536373839404142434445464748
4950
<GetResponse>
    <GetResponsenormal>
		<InvokeIdAndPriority Value="81" />
    	<Result>
			<Data>
				<OctetString Value="01020304050607080910111213141516
				17181920212223242526272829303132333435363738394041424344454647484950" />
			</Data>
		</Result>
    </GetResponsenormal>
</GetResponse>
	 */
	public static void main(String[] args) {
		//String respNormal = "C401810009320102030405060708091011121314151617181920212223242526272829303132333435363738394041424344454647484950";
		String respNormal = "C40181000600000000";
		decodeTest(respNormal);
		String respList =   "C40381020009320102030405060708091011121314151617181920212223242526272829303132333435363738394041424344454647484950000A03303030";
		decodeTest(respList);
		
		//block response
		String respBlock1 = "C402810000000001001E093201020304050607080910111213141516171819202122232425262728";
		decodeTest(respBlock1);
		
		String respBlock2 = "C402810100000002001629303132333435363738394041424344454647484950";
		decodeTest(respBlock2);
	}
	
	private static void decodeTest(String str){
		DecodeStream decoder = DecodeStream.wrap(str);
		GetResponse resp = new GetResponse();
		try {
			resp.decode(decoder);
			System.out.println(resp);
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		decoder = null;
	}
}
