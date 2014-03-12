/**
 * DlmsContext is created by AARE if AARE responses successful association.
 * The master-station manages this context and saves it to 
 */
package com.hx.dlms.aa;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.ASN1BitString;
import com.hx.dlms.aa.AarqApdu.CipherMechanism;
import com.hx.dlms.message.DlmsMessage;

public class DlmsContext extends DlmsMeterConformance{
	private static final long serialVersionUID = -8614513874164171510L;
	public static long sendInterval=0;
	
	public enum AAState { INIT, WAIT_AARE, WAIT_STOC, AA_OK };
	//DLMS context attributes.
	public String meterId = null;
	public String peerAddr = null;	// DLMS device ip@port string.
	public String subProtocol = null;
	public int meterType = 1;	// 1 single, 3 three phase, 0 : Data concentrator
	
	public boolean isRelay = false;
	//Application association mechanism
	public CipherMechanism aaMechanism = CipherMechanism.NO_SECURITY;
	public byte[] meterSysTitle = null;
	private int frameCounter = 1;
	public byte[] encryptKey = null;
	public byte[] authKey = null;
	public int keyVersion = 0;
	
	public int linkMode=IMessage.COMMUNICATION_TYPE_NORMAL;
	
	public ConcurrentHashMap<String,DlmsContext> subContexts = new ConcurrentHashMap<String, DlmsContext>();
	
	public int port = 31;//ͨѶ�˿ںţ������м��ٲ�,Ĭ��Ϊ31
	
	public String dcLogicAddr = null;
	public int measurePoint = 0 ;
	
	//Key-Change Parameter of type SecurityKeyParam
	public Object keyChangeRequest = null;
	
	//Temporary attribute definition:
	//ASN1OctetString or ASN1BitString
	public byte[] authenticationValue = null;
	
	//Context definition:
	public long lastAATime = 0;		//Application Association time
	public AAState aaState = AAState.INIT;
	private int invokeId = 0;	//current frame sequence number, range[1-0x0F]
//	public IMessage curDownMessage;  //Current down-link DLMS message.
	public AtomicBoolean waitReply = new AtomicBoolean(false);
	public long lastSendTime = 0;			   //Current down-link message latest send time
	public long lastRecvTime = 0;   		   //last recv msg time,use for dropped
	public int resendCount = 0;				   //Re-send DLMS message times.

	//WebReq event object put into list for two case: (1) waiting for AA.
	//(2) If reqDownMessages is not empty.
//	public List<Object> webReqList = Collections.synchronizedList(new LinkedList<Object>()); //DlmsEvent Object
	
	public String srcMeterId;
	
	public String phoneNum;
	//Current request encoded into multiple down-link messages.
//	public ArrayList<IMessage> reqDownMessages = new ArrayList<IMessage>();
//	public ArrayList<Object> blockReplys = new ArrayList<Object>();
	
	public DlmsContext( ){
		super();
		encryptKey = HexDump.toArray("00000000000000000000000000000000");
		authKey = encryptKey;
	}
	
	public DlmsContext(ASN1BitString conformance){
		this();
		super.setConformance(conformance);
	}
	
//	public void enableSend(){
//		this.curDownMessage = null;
//		waitReply.set(false);
//	}
//	
//	public void onRequestFinished(){
//		reqDownMessages = new ArrayList<IMessage>();
//		blockReplys.clear();
//		resendCount = 0;
//		enableSend();
//	}
//	
	public void loadEncryptionKeys(){
		//TODO: load soft-encryption keys from DB.
	}
	
	public void update(DlmsContext src){
		super.update(src);
		lastAATime = src.lastAATime;
		this.authenticationValue = src.authenticationValue;
		this.aaMechanism = src.aaMechanism;
	}
	
	public final int nextFrameCounter(){
		if(frameCounter>=99){
			return frameCounter=0;
		}else{
			return frameCounter++;
		}
	}
	
	public final byte[] makeInitVector( byte[]cipherText, int offset ){
		ByteBuffer iv = ByteBuffer.allocate(12);
		iv.put(this.meterSysTitle);
		iv.put(cipherText, offset, 4);
		return iv.array();
	}
	
	public void updateAARE(AareApdu aare){
		this.setConformance(aare.getNegotiatedResponse().getConformance());
		setMaxPduSize(aare.getNegotiatedPduSize());
//		setAuthenticationMechanismName(aare.getMechanismName());
		setAuthenticationValue(aare.getRespAuthenticationValue().getAuthValue());
		this.meterSysTitle = aare.getRespApTitle();
	}
	
	public void reset(){
		aaState = AAState.INIT;
//		this.aaMechanism = CipherMechanism.NO_SECURITY;
	}
	
	public void setAuthenticationMechanismName(AuthenticationMechanismName mechanism){
		switch(mechanism.getMechanismNameId()){
		case 0:
			aaMechanism = CipherMechanism.NO_SECURITY; break;
		case 1:
			aaMechanism = CipherMechanism.LLS; break;
		case 2:
			aaMechanism = CipherMechanism.HLS_2; break;
		case 3:
			aaMechanism = CipherMechanism.HLS_MD5; break;
		case 4:
			aaMechanism = CipherMechanism.HLS_SHA1; break;
		case 5:
			aaMechanism = CipherMechanism.HLS_GMAC; break;
		default:
			aaMechanism = CipherMechanism.UNKNOW; break;
		}
	}

	public final String getMeterId() {
		return meterId;
	}

	public final void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public final CipherMechanism getCipherMechanism() {
		return aaMechanism;
	}

	public final byte[] getAuthenticationValue() {
		return authenticationValue;
	}

	public final void setAuthenticationValue(byte[] authValue) {
		this.authenticationValue = authValue;
	}
	
	public final int getNextInvokeId(){
		invokeId++;
		if( invokeId> 0x0F )
			invokeId = 1;
		return invokeId;
	}
	
	public final int getNextInvokeId(String meterId){
		if(this.isRelay){
			return this.subContexts.get(meterId).getNextInvokeId();
		}else{
			return this.getNextInvokeId();
		}
	}


	private ConcurrentHashMap<Integer,Object> historyEvents = new ConcurrentHashMap<Integer, Object>();

	public void putHistoryEvent(int invokeId,String meterId,Object event){
		getHistoryEvents(meterId).put(invokeId, event);
	}
	
	public ConcurrentHashMap<Integer,Object> getHistoryEvents(String meterId){
		if(this.isRelay){
			return this.subContexts.get(meterId).historyEvents;
		}else{
			return historyEvents;
		}
	}
}
