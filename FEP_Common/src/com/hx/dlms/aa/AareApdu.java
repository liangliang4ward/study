package com.hx.dlms.aa;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1BitString;
import com.hx.dlms.ASN1Choice;
import com.hx.dlms.ASN1Constants;
import com.hx.dlms.ASN1Int8;
import com.hx.dlms.ASN1Integer;
import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Sequence;
import com.hx.dlms.ASN1String;
import com.hx.dlms.ASN1Type;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.EncodeStream;
import com.hx.dlms.TagAdjunct;

public class AareApdu extends ASN1Sequence {
	private static final long serialVersionUID = 868794954099581213L;

	private static final Logger log = Logger.getLogger(AareApdu.class);
	
	private ASN1BitString protocolVersion = new ASN1BitString();
	private ApplicationContextName contextName = ApplicationContextName.createNoCipher();
	private ASN1Integer aaResult = new ASN1Integer();
	//result-source-diagnostic
	private ASN1Choice aaResultDiagnostic = new ASN1Choice();
	//responding-AP-title
	private ASN1OctetString respApTitle = new ASN1OctetString();	//Not used in DLMS
	private ASN1Int8 respAeQualifier = new ASN1Int8();	//Not used in DLMS
	private ASN1Int8 respApInvocationId = new ASN1Int8();	//Not used in DLMS
	private ASN1Int8 respAeInvocationId = new ASN1Int8();	//Not used in DLMS
	private ASN1BitString respAcseRequirements = new ASN1BitString();
	private AuthenticationMechanismName mechanismName = AuthenticationMechanismName.createNoAuthentication();
	private AuthenticationValue respAuthenticationValue = new AuthenticationValue();
	private ASN1String implementationData = ASN1String.VisibleString();
	private ASN1OctetString userInformation = new ASN1OctetString();
	
	//result diagnostic choices
	private ASN1Integer[] diagnostics = new ASN1Integer[2];
	//When received AARE, decode user-information into InitiateResponse
	private InitiateResponse negotiatedResponse = null;
	//When decode AARE, create DlmsContext for future communication using.
	
	private boolean initiated = false;
	
	public AareApdu(){
		super(ASN1Constants.TAG_SEQUENCE);
		this.setBerCodec();
		this.setTagAdjunct(TagAdjunct.applicationImplicit(1));	 // [APPLICATION 1] IMPLICIT SEQUENCE
		
		members = new ASN1Type[13];
		members[0] = protocolVersion;
		members[1] = contextName;
		members[2] = aaResult;
		diagnostics[0] =  new ASN1Integer();
		diagnostics[0].setTagAdjunct(TagAdjunct.contextSpecificExplicit(1));
		diagnostics[1] =  new ASN1Integer();
		diagnostics[1].setTagAdjunct(TagAdjunct.contextSpecificExplicit(2));
		aaResultDiagnostic.setInitMembers(diagnostics);
		members[3] = aaResultDiagnostic;
		members[4] = respApTitle;
		members[5] = respAeQualifier;
		members[6] = respApInvocationId;
		members[7] = respAeInvocationId;
		members[8] = respAcseRequirements;
		members[9] = mechanismName;
		members[10] = respAuthenticationValue;
		members[11] = implementationData;
		members[12] = userInformation;
	}
	
	public void setInitResponse(InitiateResponse resp) throws IOException{
		userInformation.setValue(resp.encode());
	}
	
	public final byte[] getUserInformation(){
		return userInformation.getValue();
	}
	
	public final void setDecryptedUserInfo(byte[] initRespValues){
		try {
			int offset = 0;
			int len = initRespValues.length;
			if( initRespValues[0] == 0x04 ){
				offset = 2;
				len -= 2;
			}
			negotiatedResponse = new InitiateResponse();
			negotiatedResponse.decode(DecodeStream.wrap(ByteBuffer.wrap(initRespValues,offset,len)));
		} catch (IOException e) {
			log.error("Decode negotiatedResponse exception:"+e.getLocalizedMessage());
			negotiatedResponse = null;
		}
	}
	
	public InitiateResponse getNegotiatedResponse(){
		return negotiatedResponse;
	}

	public int getNegotiatedPduSize(){
		int meterPduSize = 256;//Ä¬ÈÏÎª256×Ö½Ú
		if( null != negotiatedResponse ){
			meterPduSize = negotiatedResponse.getMaxRecvPduSize().getInt();
		}
		return meterPduSize;
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

	@Override
	protected void onDecodeContentComplete(DecodeStream input) throws IOException{
		if( null == userInformation.getValue() || mechanismName.isAuthenticationHLSGMAC() )
			return;
		try {
			negotiatedResponse = new InitiateResponse();
			negotiatedResponse.decode(DecodeStream.wrap(ByteBuffer.wrap(userInformation.getValue())));
		} catch (IOException e) {
			log.error("Decode negotiatedResponse exception:"+e.getLocalizedMessage());
			negotiatedResponse = null;
		}
	}

	/**
	 * All members prepare to encode.
	 */
	protected void initiate(){
		if( initiated )
			return;
		protocolVersion.setDefaultValue(new Integer(0)).setTagAdjunct(TagAdjunct.contextSpecificImplicit(0));
		contextName.setTagAdjunct(TagAdjunct.contextSpecificExplicit(1));
		aaResult.setTagAdjunct(TagAdjunct.contextSpecificExplicit(2));
		aaResultDiagnostic.setTagAdjunct(TagAdjunct.contextSpecificExplicit(3));
		respApTitle.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(4));
		respAeQualifier.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(5));
		
		respApInvocationId.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(6));
		respAeInvocationId.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(7));
		respAcseRequirements.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificImplicit(8));
		mechanismName.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificImplicit(9));
		respAuthenticationValue.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(10));
		
		implementationData.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificImplicit(29));
		userInformation.setOptional(true).setTagAdjunct(TagAdjunct.contextSpecificExplicit(30));

		//Remember that contextName and mechanismName can be changed when using ciphering.
		members[1] = contextName;
		members[9] = mechanismName;
		for(int i=0; i<members.length; i++ ){
			members[i].codec(this.codec());
		}
		initiated = true;
	}

	public final void setContextName(ApplicationContextName contextName) {
		this.contextName = contextName;
	}

	public final void setMechanismName(AuthenticationMechanismName mechanismName) {
		this.mechanismName = mechanismName;
	}
	
	public int getResultValue(){
		if( aaResult.isDecodeDone() )
			return aaResult.getInt8();
		else
			return -1;
	}
	
	public boolean isCipherEnabled(){
		return contextName.isCipherEnabled();
	}
	
	public boolean isAssociationAccepted(){
		return getResultValue() == 0;
	}
	
	public String getResultString(){
		if( ! aaResult.isDecodeDone() )
			return null;
		String str = "unknow result:"+getResultValue();
		switch( aaResult.getInt8() ){
		case 0:
			str = "accepted";
			break;
		case 1:
			str = "rejected-permanent";
			break;
		case 2:
			str = "rejected-transient";
			break;
		case -1:
			str = "NULL value";
			break;
		}
		return str;
	}
	
	public String getDiagnosticString(){
		StringBuilder sb = new StringBuilder(64);
		ASN1Integer diagnosticChoice = (ASN1Integer)aaResultDiagnostic.getDecodedObject();
		if( null == diagnosticChoice ){
			return "no diagnostic.";
		}
		sb.append("       Associate-source-diagnostic: ");
		int choiceTag = diagnosticChoice.identifier();
		int choiceValue = diagnosticChoice.getInt8();
		if( choiceTag == 1 ){
			sb.append(" source = acse-service-user, reason = ");
			switch( choiceValue ){
			case 0:
				sb.append("null"); break;
			case 1:
				sb.append("no-reason-given"); break;
			case 2:
				sb.append("application-context-name-not-supported"); break;
			case 11:
				sb.append("authentication-mechanism-name-not-recognised"); break;
			case 12:
				sb.append("authentication-mechanism-name-required"); break;
			case 13:
				sb.append("authentication-failure"); break;
			case 14:
				sb.append("authentication-required"); break;
			default:
				sb.append("unknow value:"+choiceValue);
			}
		}
		else if( choiceTag == 2 ){
			sb.append(" source = acse-service-provider, reason = ");
			switch( choiceValue ){
			case 0:
				sb.append("null"); break;
			case 1:
				sb.append("no-reason-given"); break;
			case 2:
				sb.append("no-common-acse-version"); break;
			default:
				sb.append("unknow value:"+choiceValue);
			}
		}
		else
			sb.append("Invalid diagnostic ChoiceTag="+choiceTag);
		return sb.toString();
	}
	
	@Override
	public String toString(){
		StringWriter writer = new StringWriter(1024);
		PrintWriter out = new PrintWriter(writer);
		out.print("AARE>> version=");
		if(null == protocolVersion.getValue() )
			out.println("default;");
		else
			out.println(this.protocolVersion);
		out.print("       contextName: ");
		out.println(contextName);
		out.print("       Association-result = ");
		out.println(getResultString());
		out.println(getDiagnosticString());
		
		out.print("       responding-AP-title: ");
		byte[] temp = this.respApTitle.getValue();
		if( temp == null )
			out.println("no present.");
		else
			out.println(HexDump.hexDump(temp, 0, temp.length));
		//respAcseRequirements
		out.print("       respAcseRequirements: ");
		temp = this.respAcseRequirements.getValue();
		if( temp == null )
			out.println("no present.");
		else
			out.println(this.respAcseRequirements);

		out.print("       mechanism-name: ");
		out.println(this.mechanismName);

		out.print("       responding-authentication-value: ");
		ASN1Type authValue = this.respAuthenticationValue.getDecodedObject();
		if( null == authValue )
			out.println("not present.");
		else{
			temp = authValue.getValue();
			if( temp == null )
				out.println("not present.");
			else
				out.println(HexDump.hexDump(temp, 0, temp.length));
		}

		out.print("       user-information: ");
		//this.userInformation.getValue();
		if( negotiatedResponse == null )
			out.println("not present.");
		else
			out.println( negotiatedResponse );
		
		return writer.toString();
	}

	public final AuthenticationMechanismName getMechanismName() {
		return mechanismName;
	}

	public final AuthenticationValue getRespAuthenticationValue() {
		return respAuthenticationValue;
	}

	public final void setRespAuthenticationValue(
			AuthenticationValue respAuthenticationValue) {
		this.respAuthenticationValue = respAuthenticationValue;
	}

	public static void main(String[] args) {
		InitiateResponse resp = new InitiateResponse();
		resp.getMaxRecvPduSize().setValue(0x01f4);
		resp.getConformance().setInitValue(new byte[]{0,(byte)0x50,(byte)0x1F});
		AareApdu aare = new AareApdu();
		try {
			aare.setInitResponse(resp);
			aare.aaResult.setValue(0);
			aare.diagnostics[0].setValue(0);
			aare.diagnostics[0].setBerCodec();
			aare.aaResultDiagnostic.choose(aare.diagnostics[0]);
			byte[] encodes = aare.encode();
			System.out.println("AARE="+HexDump.hexDump(encodes, 0, encodes.length));
			//output should: 61 29 A1 09 06 07 60 85 74 05 08 01 01 A2 03 02 01 00 A3 05 A1 03 02 01 00 BE 10 04 0E 08 00 06 5F 1F 04 00 00 50 1F 01 F4 00 07
			// now is: 		 61 29 A1 09 06 07 60 85 74 05 08 01 01 A2 03 02 01 00 A3 05 A1 03 02 01 00 BE 10 04 0E 08 00 06 5F 1F 04 00 00 50 1F 01 F4 00 07
			aare = new AareApdu();
//			String s = "6129A109060760857405080101A203020100A305A103020100BE10040E0800065F1F040000501F01F40007";
//			String s1 = "6141A109060760857405080101A203020101A305A10302010288020780890760857405080201AA0A80080000000000000000BE0F040D0800065F040000001001000007";
//			String s2 = "6141A109060760857405080101A2020101A3A1020202800785740502AA8030303030BE0408060400100007000000000000000000000000000000000000000000000000";
			String s3 = "6149A109060760857405080101A20302010005A102010002808907850502AA1280E68434B80933780A61BE0408060400100007000000000000000000000000000000000000000000000000";
			DecodeStream input = DecodeStream.wrap(s3);
			aare.decode(input);
			System.out.println(aare);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final ASN1Integer getAaResult() {
		return aaResult;
	}

	public final ASN1Choice getAaResultDiagnostic() {
		return aaResultDiagnostic;
	}

	public final ASN1Integer[] getDiagnostics() {
		return diagnostics;
	}

	public final byte[] getRespApTitle() {
		return respApTitle.getValue();
	}

	public final void setRespApTitle(byte[] respApTitleValue) {
		this.respApTitle.setValue(respApTitleValue);
	}
}
