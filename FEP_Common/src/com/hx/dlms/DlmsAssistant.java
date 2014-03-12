package com.hx.dlms;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.hx.dlms.ASN1OctetString;
import com.hx.dlms.ASN1Oid;
import com.hx.dlms.DecodeStream;
import com.hx.dlms.DlmsData;
import com.hx.dlms.TagAdjunct;
import com.hx.dlms.aa.AarqApdu;
import com.hx.dlms.aa.AarqApdu.CipherMechanism;
import com.hx.dlms.aa.DlmsContext;
import com.hx.dlms.applayer.action.ActionRequest;
import com.hx.dlms.applayer.action.ActionRequestNormal;
import com.hx.dlms.cipher.AESECB128;
import com.hx.dlms.cipher.Gcm128SoftCipher;
import com.hx.dlms.cipher.IDlmsCipher;

/**
 * 
 * @author gaoll
 *
 * @time 2013-4-8 上午11:26:57
 *
 * @info Dlms协作类，主要用于处理加密，解密，生成AARQ
 */
public class DlmsAssistant {
	public static enum OP_TYPE { OP_NA, OP_GET, OP_SET, OP_ACTION, OP_EVENT_NOTIFY, OP_CHANGE_KEY,OP_UPGRADE};

	private static DlmsAssistant instance = new DlmsAssistant();

	private final byte[] msSysTitle = new byte[]{ 0x48, 0x58, 0x45, 0x11, 0, 0, 0, 0 };

	
	private static final Logger log = Logger.getLogger(DlmsAssistant.class);	
	private IDlmsCipher cipher = Gcm128SoftCipher.getInstance();

	private DlmsAssistant() {
	}

	public static DlmsAssistant getInstance() {
		if (instance == null) {
			instance = new DlmsAssistant();
		}
		return instance;

	}

	public byte[] createAarqApdu(CipherMechanism aaMechanism, int _frameCount,
			DlmsContext context, byte[] msSysTitle) throws IOException {
		AarqApdu aarq = AarqApdu.create(aaMechanism);
		switch (aaMechanism) {
		case HLS_2:
			break;
		case HLS_GMAC: {
			// set SysTitle.
			aarq.setCallingApTitle(msSysTitle);
			ByteBuffer iv = ByteBuffer.allocate(12);
			iv.put(msSysTitle);
			iv.putInt(_frameCount);
			iv.flip();
			byte[] initRequest = aarq.getInitiateRequest();
			byte[] cipherInitRequest = cipher.encrypt(context, initRequest,
					iv.array());
			ByteBuffer cipherContext = ByteBuffer
					.allocate(cipherInitRequest.length + 5);
			// Security head + frameCounter + cipherText + authenticationTag
			cipherContext.put((byte) 0x30).putInt(_frameCount)
					.put(cipherInitRequest);
			cipherContext.flip();

			// glo-initiateRequest [33] IMPLICIT OCTET STRING,
			ASN1OctetString cipheredRequest = new ASN1OctetString();
			cipheredRequest.setBerCodec();
			TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(33);
			myAdjunct.axdrCodec(true);
			cipheredRequest.setTagAdjunct(myAdjunct);
			cipheredRequest.setValue(cipherContext.array());
			aarq.setInitiateRequest(cipheredRequest.encode());
		}
			break;
		case HLS_MD5:
			break;
		case HLS_SHA1:
			break;
		case LLS:
			break;
		case NO_SECURITY:
			break;
		default:
			break;
		}
		log.debug("app link send again:" + aaMechanism);
		return aarq.encode();
	}
	
	
	
	public byte[] cipher(OP_TYPE opType,byte[] plainApdu, DlmsContext context) throws Exception{

		int cipherTag = 0;
		switch( opType ){
		case OP_ACTION:
			cipherTag = 203;   //with global ciphering, glo-action-request
			break;
		case OP_GET:
			cipherTag = 200;   //with global ciphering, glo-get-request
			break;
		case OP_SET:
			cipherTag = 201;   //with global ciphering, glo-set-request
			break;
		case OP_EVENT_NOTIFY:
			cipherTag = 202;   //with global ciphering, glo-event-notification-request
			break;
		default:
			return plainApdu;
		}
		
		ASN1OctetString octs = new ASN1OctetString();
		TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(cipherTag);
		octs.forceEncodeTag(true);
		myAdjunct.axdrCodec(true);
		octs.setTagAdjunct(myAdjunct);
		
		ByteBuffer iv = ByteBuffer.allocate(12);
		iv.put(this.msSysTitle);
		int _frameCount = context.nextFrameCounter();
		iv.putInt( _frameCount );
		iv.flip();
		byte[] enc = cipher.encrypt(context, plainApdu, iv.array() );
		ByteBuffer encWithSH = ByteBuffer.allocate(enc.length+5);
		encWithSH.put((byte)0x30).putInt(_frameCount).put(enc);
		
		octs.setValue(encWithSH.array());
		
		return octs.encode();
	}
	
	
	public final byte[] makeInitVector( byte[]cipherText, int offset,byte[] meterSysTitle ){
		ByteBuffer iv = ByteBuffer.allocate(12);
		iv.put(meterSysTitle);
		iv.put(cipherText, offset, 4);
		return iv.array();
	}
	
	
	public byte[] decipher(ByteBuffer apdu,DlmsContext context,byte[] meterSysTitle) throws IOException{
		ASN1OctetString octs = new ASN1OctetString();
		TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(0xFF & apdu.get(0));
		octs.forceEncodeTag(true);
		myAdjunct.axdrCodec(true);
		octs.setTagAdjunct(myAdjunct);
		octs.decode(DecodeStream.wrap(apdu));
		byte[] val = octs.getValue();	// SH + C + T:  means security ctr + FC + cipher text + auth tag
		if( val[0] == 0x30 ){
			byte[] iv =makeInitVector(val, 1,meterSysTitle);
			byte[] cipherText = new byte[val.length-5];
			for(int i=0; i<cipherText.length; i++ )
				cipherText[i] = val[i+5];
			return cipher.decrypt(context, cipherText, iv );
		}
		return null;
	}
	
	public byte[] createStoC(CipherMechanism aaMechanism,byte[] authenticationValue,int _frameCount,DlmsContext context) throws IOException{
		//Action.request操作0-0:40.0.0.255的 method( 1 )，将服务端发送的随机数加密计算后的结果发送给服务端
		ActionRequest request = new ActionRequest();
		String obisString = "0.0.40.0.0.255";
		DlmsData param = new DlmsData();
		byte[] apdu = null;
		if( aaMechanism == CipherMechanism.HLS_2 ){
			//HLS使用AES-ECB对16字节随机数进行加密
			byte[] ctosParam = AESECB128.encrypt(authenticationValue);
			param.setOctetString(ctosParam);
			ActionRequestNormal reqNormal = new ActionRequestNormal(1,15,convertOBIS(obisString),1,param);
			request.choose(reqNormal);
			apdu = request.encode();
		}
		else if( aaMechanism == CipherMechanism.HLS_GMAC ){
			ByteBuffer iv = ByteBuffer.allocate(12);
			iv.put(msSysTitle);
			iv.putInt( _frameCount );
			iv.flip();
			
			byte[] authTag = cipher.auth(context, authenticationValue, iv.array() );
			ByteBuffer fstoc = ByteBuffer.allocate(authTag.length+5);
			//Security head + frameCounter + authenticationTag
			fstoc.put((byte)0x10).putInt(_frameCount).put(authTag);
			fstoc.flip();
			param.setOctetString(fstoc.array());
			
			ActionRequestNormal reqNormal = new ActionRequestNormal(1,15,convertOBIS(obisString),1,param);
			request.choose(reqNormal);
			byte[] plainText = request.encode();
			byte[] enc = cipher.encrypt(context, plainText, iv.array() );
			ByteBuffer actStoC = ByteBuffer.allocate(enc.length+5);
			actStoC.put((byte)0x30).putInt(_frameCount).put(enc);
			
			//203:	global ciphering, glo-action-request
			ASN1OctetString cipheredStoC = new ASN1OctetString();
			TagAdjunct myAdjunct = TagAdjunct.contextSpecificImplicit(203);
			myAdjunct.axdrCodec(true);
			cipheredStoC.forceEncodeTag(true);
			cipheredStoC.setTagAdjunct(myAdjunct);
			cipheredStoC.setValue(actStoC.array());
			apdu = cipheredStoC.encode();
		}
		else{
			throw new RuntimeException("CtoS, not supported mechanism:"+aaMechanism);
		}
		log.debug(" StoC send");
		return apdu;
	}	
	
	
	public  final byte[] convertOBIS(String obis){
		int[] intOids = ASN1Oid.parse(obis);
		if( null == intOids || intOids.length != 6 )
			throw new RuntimeException("Invalid OBIS:"+obis);
		byte[] ret = new byte[6];
		for(int i=0; i<ret.length; i++ )
			ret[i] = (byte) intOids[i];
		return ret;
	}
}
