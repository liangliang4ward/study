package com.hx.dlms.aa;

import java.io.IOException;
import java.util.Random;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1BitString;
import com.hx.dlms.ASN1Constants;
import com.hx.dlms.ASN1Int8;
import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1String;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.EncodeStream;
import com.hx.dlms.TagAdjunct;

public class AarqApdu extends ASN1Sequence {
	private static final long serialVersionUID = 7942087350381840389L;
	public static enum CipherMechanism{ NO_SECURITY, LLS,HLS_2,HLS_MD5,HLS_SHA1,HLS_GMAC,BENGAL ,UNKNOW };
	
	private ASN1BitString protocolVersion = new ASN1BitString();
	private ApplicationContextName contextName = ApplicationContextName.LN_NO_CIPHERING;
	//System Title: 3 bytes Manufacturer ID,(This is the same as used in the leading octets of the Logical Device Name of server logical devices)
	//5 bytes device manufacturing number.
/**
 * The use of the Called_AP_Title, Called_AE_Qualifier, Called_AP_Invocation _Identifier,
 *  Called AE_Invocation_Identifier, Calling_AE_Qualifier, Calling_AP_Invocation _Identifier 
 *  and Calling_AE_Invocation_Identifier parameters is optional.
 *  Their use is not specified in this companion specification.
 */
	private ASN1OctetString calledApTitle = new ASN1OctetString();	//Not used in DLMS
	private ASN1Int8 calledAeQualifier = new ASN1Int8();	//Not used in DLMS
	private ASN1Int8 calledApInvocationId = new ASN1Int8();	//Not used in DLMS
	private ASN1Int8 calledAeInvocationId = new ASN1Int8();	//Not used in DLMS
	
	//The Calling_AP_Title parameter is conditional. When the Application_Context_Name indicates an application context using ciphering, 
	//it shall carry the client system title specified in 9.2.4.8.3.4.6
	private ASN1OctetString callingApTitle = new ASN1OctetString(); //Conditional used when ciphering

	private ASN1Int8 callingAeQualifier = new ASN1Int8();	//Not used in DLMS
	private ASN1Int8 callingApInvocationId = new ASN1Int8();	//Not used in DLMS
	private ASN1Int8 callingAeInvocationId = new ASN1Int8();	//Not used in DLMS

/**
 * The ACSE_Requirements parameter is optional. It is used to select the optional authentication 
 * functional unit of the A-Associate service for the association. See 9.4.2.1.
	The presence of the ACSE_requirements parameter depends on the security mechanism used:
1) in the case of Lowest Level security, it shall not be present; only the Kernel functional unit is used;
2) in the case of Low Level security (LLS) it shall be present in the .request primitive 
	and it may be present in the .response service primitive;
3) in the case of High Level Security (HLS), it shall be present both 
    in the .request and the .response service primitives.
 */
	private ASN1BitString acseRequirements = new ASN1BitString();

	private AuthenticationMechanismName mechanismName = AuthenticationMechanismName.NO_AUTHENTICATION;
/**
 * The Calling_Authentication_Value parameter and the Responding_Authentication_Value 
 * parameters are conditional. They are present only, if the authentication functional 
 * unit has been selected. They hold the client authentication value / server authentication 
 * value respectively, appropriate for the Security_Mechanism_Name.
 */
	private AuthenticationValue callingAuthenticationValue = new AuthenticationValue();
//The Implementation_Information parameter is optional. Its use is not specified in this companion specification.
	private ASN1String implementationData = ASN1String.VisibleString();

	private boolean initiated = false;

	/**
	 * The user-information field shall carry an InitiateRequest APDU encoded in A-XDR, and then
	 * encoding the resulting OCTET STRING in BER
	 */
	private ASN1OctetString userInformation = new ASN1OctetString();
	
	public AarqApdu(){
		super(ASN1Constants.TAG_SEQUENCE);
		this.setBerCodec();
		this.setTagAdjunct(TagAdjunct.applicationImplicit(0));	 // [APPLICATION 0] IMPLICIT SEQUENCE
		members = new ASN1Type[15];
		members[0] = protocolVersion;
		members[1] = contextName;
		members[2] = calledApTitle;
		members[3] = calledAeQualifier;
		members[4] = calledApInvocationId;
		members[5] = calledAeInvocationId;

		members[6] = callingApTitle;
		members[7] = callingAeQualifier;
		members[8] = callingApInvocationId;
		members[9] = callingAeInvocationId;

		members[10] = acseRequirements;
		members[11] = mechanismName;
		members[12] = callingAuthenticationValue;
		members[13] = implementationData;
		members[14] = userInformation;
	}
	
	public final void setInitiateRequest(InitiateRequest req) throws IOException{
		userInformation.setValue(req.encode());
	}
	
	public final void setInitiateRequest( byte[] initiateRequest ){
		userInformation.setValue(initiateRequest);
	}

	public final byte[] getInitiateRequest(){
		return userInformation.getValue();
	}
	
	public final void setCallingApTitle( byte[] sysTitle ){
		callingApTitle.setValue(sysTitle);
	}
	
	@Override
	public void encode(EncodeStream output) throws IOException{
		initiate();
		super.encode(output);
	}
	
	@Override
	public boolean decode(DecodeStream input) throws IOException{
		initiate();
		return super.decode(input);
	}

	/**
	 * All members prepare to encode.
	 */
	protected void initiate(){
		if( initiated )
			return;
		//Remember that contextName and mechanismName can be changed when using ciphering.
		members[1] = contextName;
		members[11] = mechanismName;
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}

		protocolVersion.setDefaultValue(new Integer(0)).setTagAdjunct(TagAdjunct.contextSpecificImplicit(0));
		contextName.setTagAdjunct(TagAdjunct.contextSpecificExplicit(1));
		calledApTitle.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(2));
		calledAeQualifier.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(3));
		calledApInvocationId.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(4));
		calledAeInvocationId.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(5));
		
		callingApTitle.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(6));
		callingAeQualifier.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(7));
		callingApInvocationId.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(8));
		callingAeInvocationId.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(9));
		
		acseRequirements.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificImplicit(10));
		mechanismName.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificImplicit(11));
		callingAuthenticationValue.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(12));
		
		implementationData.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificImplicit(29));
		
		userInformation.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(30));
		initiated = true;
	}

	public final void setContextName(ApplicationContextName contextName) {
		this.contextName = contextName;
	}

	public final void setMechanismName(AuthenticationMechanismName mechanismName) {
		this.mechanismName = mechanismName;
	}

	public final AuthenticationValue getCallingAuthenticationValue() {
		return callingAuthenticationValue;
	}
	
	public void createRandomAuthenticationValue(){
		byte[] authValue = new byte[16];
		Random random = new Random(System.currentTimeMillis());
		random.nextBytes(authValue);
		this.callingAuthenticationValue.setAuthValue(authValue);
	}
	
	public void createRandomAuthenticationValueWith8Bytes(){
		byte[] authValue = new byte[8];
		Random random = new Random(System.currentTimeMillis());
		random.nextBytes(authValue);
		this.callingAuthenticationValue.setAuthValue(authValue);
	}

	public final ASN1BitString getAcseRequirements() {
		return acseRequirements;
	}

	public final ASN1OctetString getCallingApTitle() {
		return callingApTitle;
	}
	
	public static final AarqApdu create( CipherMechanism aaMechanism ) throws IOException{
		InitiateRequest req = new InitiateRequest();
		req.getMaxRecvPduSize().setValue(0x04B0);
		req.getProposedConformance().setInitValue(new byte[]{0,(byte)0x7E,(byte)0x1f});
		AarqApdu aarq = new AarqApdu();
		aarq.setInitiateRequest(req);

		switch(aaMechanism){
		case HLS_2:
			aarq.acseRequirements.setInitValue(new boolean[]{ true });
			aarq.mechanismName = AuthenticationMechanismName.HLS_2;
			aarq.createRandomAuthenticationValue();
			break;
		case HLS_GMAC:
			aarq.acseRequirements.setInitValue(new boolean[]{ true });
			aarq.contextName = ApplicationContextName.LN_WITH_CIPHERING;
			aarq.mechanismName = AuthenticationMechanismName.HLS_GMAC;
			aarq.createRandomAuthenticationValue();
			break;
		case HLS_MD5:
			aarq.acseRequirements.setInitValue(new boolean[]{ true });
			aarq.mechanismName = AuthenticationMechanismName.HLS_MD5;
			aarq.createRandomAuthenticationValue();
			break;
		case HLS_SHA1:
			aarq.acseRequirements.setInitValue(new boolean[]{ true });
			aarq.mechanismName = AuthenticationMechanismName.HLS_SHA1;
			aarq.createRandomAuthenticationValue();
			break;
		case LLS:
			aarq.acseRequirements.setInitValue(new boolean[]{ true });
			aarq.mechanismName = AuthenticationMechanismName.LLS;
			break;
		case NO_SECURITY:
			break;
		case UNKNOW:
		default:
			break;
		}
		aarq.contextName.codec(aarq.codec());
		aarq.mechanismName.codec(aarq.codec());
		return aarq;
	}
	public static void main(String[] args){
		InitiateRequest req = new InitiateRequest();
		req.getMaxRecvPduSize().setValue(0x04B0);
		req.getProposedConformance().setInitValue(new byte[]{0,(byte)0x7E,(byte)0x1f});
		AarqApdu aarq = new AarqApdu();
		try {
			aarq.setInitiateRequest(req);
			byte[] encodes = aarq.encode();
			System.out.println("AARQ="+HexDump.hexDump(encodes, 0, encodes.length));
			//output should: 60 1D A1 09 06 07 60 85 74 05 08 01 01 BE 10 04 0E 01 00 00 00 06 5F 1F 04 00 00 7E 1F 04 B0
			// now is: 		 60 1D A1 09 06 07 60 85 74 05 08 01 01 BE 10 04 0E 01 00 00 00 06 5F 1F 04 00 00 7E 1F 04 B0
			aarq = new AarqApdu();
			DecodeStream input = DecodeStream.wrap(encodes);
			aarq.decode(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
