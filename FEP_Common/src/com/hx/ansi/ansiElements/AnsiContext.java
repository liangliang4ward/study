package com.hx.ansi.ansiElements;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.ansiElements.basicTable.Table0;
import com.hx.ansi.ansiElements.ansiElements.basicTable.Table1;
import com.hx.ansi.ansiElements.ansiElements.basicTable.Table3;
import com.hx.ansi.ansiElements.ansiElements.basicTable.Table5;
import com.hx.ansi.ansiElements.ansiElements.basicTable.Table7;
import com.hx.ansi.ansiElements.ansiElements.basicTable.Table8;
import com.hx.ansi.ansiElements.ansiElements.dataTable.Table11;
import com.hx.ansi.ansiElements.ansiElements.dataTable.Table12;
import com.hx.ansi.ansiElements.ansiElements.dataTable.Table13;
import com.hx.ansi.ansiElements.ansiElements.dataTable.Table15;
import com.hx.ansi.ansiElements.ansiElements.dataTable.Table16;
import com.hx.ansi.ansiElements.ansiElements.loadTable.Table61;
import com.hx.ansi.ansiElements.ansiElements.loadTable.Table62;
import com.hx.ansi.ansiElements.ansiElements.loadTable.Table63;
import com.hx.ansi.ansiElements.ansiElements.registerTable.Table21;
import com.hx.ansi.ansiElements.ansiElements.registerTable.Table22;
import com.hx.ansi.ansiElements.ansiElements.registerTable.Table27;
import com.hx.ansi.ansiElements.ansiElements.touTable.Table51;
import com.hx.ansi.element.UserInformationElement;
import com.hx.ansi.element.UserInformationElement.EPSEM_CONTROL;


/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-17 下午01:40:46
 * @version 1.0 
 */

public class AnsiContext implements Serializable{
	private static final long serialVersionUID = -8614513123156L;
	public enum AAState { IDLE, OFF_LINE, SESSION, SESSIONLESS };//application state can be Idle,off-line,session,sessionless
	//ANSI context attributes.
	public String meterId = null;
	public String peerAddr = null;	// peerAddr is IP:port
	public String localAddr=null; //localpeer is IP:port
	public String acseTitle=null;  //peerAddr--meterAddr--acseTitle
	public String subProtocol = null;
	public int meterType = 1;	// 1 single, 3 three phase, 0 : Data concentrator
	
	public boolean isRelay = false;
	//Application association mechanism
	 //加密模式   NO_SECURITY(read:0x80,write:0x92),
	 //      SECURITY_MODE_1(read:0x84,write:0x96),
	 //      SECURITY_MODE_2(read:0x88,write:0x9A)
	 //      UNKONW (0)
	public EPSEM_CONTROL  epsem_control = EPSEM_CONTROL.NO_SECURITY;
	public byte[] meterSysTitle = null;
	private int frameCounter = 1;
	public byte[] encryptKey = null;
	public byte[] authKey = null;
	public int keyVersion = 0;
	//基本数据Table放到context里面，不用每次都读取。
	public Table0 table0=null;
	public Table1 table1=null;
	public Table3 table3=null;
	public Table5 table5=null;
	public Table7 table7=null;
	public Table8 table8=null;
	public Table11 table11=null;
	public Table12 table12=null;
	public Table13 table13=null;
	public Table15 table15=null;
	public Table16 table16=null;
	public Table21 table21=null;
	public Table22 table22=null;
	public Table27 table27=null;
	
	public Table51 table51=null;
	
	public Table61 table61=null;
	public Table62 table62=null;
	public Table63 table63=null;
	
	public int port = 31;//通讯端口号，用于中继召测,默认为31
	public String dcLogicAddr = null;
	public int measurePoint = 0 ;

	//Key-Change Parameter of type SecurityKeyParam
	public Object keyChangeRequest = null;
	
	//Temporary attribute definition:
	//ASN1OctetString or ASN1BitString
	public byte[] authenticationValue = null;
	
	//Context definition:
	public long lastAATime = 0;		//Application Association time
	public AAState aaState = AAState.IDLE;
	private int invokeId = 0;	//current frame sequence number, range[1-0x0F]
	public IMessage curDownMessage = null;  //Current down-link DLMS message.
	public AtomicBoolean waitReply = new AtomicBoolean(false);
	public long lastSendTime = 0;			   //Current down-link message latest send time
	public int resendCount = 0;				   //Re-send ANSI message times.
	public int timeOut=60;
	//WebReq event object put into list for two case: (1) waiting for AA.
	//(2) If reqDownMessages is not empty.
	public List<Object> webReqList = Collections.synchronizedList(new LinkedList<Object>()); //AnsiEvent Object
	
	public String srcMeterId;
	
	//Current request encoded into multiple down-link messages.
	public ArrayList<IMessage> reqDownMessages = new ArrayList<IMessage>();
	public ArrayList<Object> blockReplys = new ArrayList<Object>();
	
	public String flag;
	
	/*
	 * 0 read all--30
	 * 1 write all--40
	 * 2 offsetRead--3F 
	 * 3 offsetWrite--4F
	 */
	private int commandType=-1;
	
	public AnsiContext( ){
		super();
		encryptKey = HexDump.toArray("00000000000000000000000000000000");
		authKey = encryptKey;
	}
	
	public void SetReqDownMessages(int i) {
		
	}
	
	
	public void enableSend(){
		this.curDownMessage = null;
		waitReply.set(false);
	}
	
	public void onRequestFinished(){
		reqDownMessages.clear();
		blockReplys.clear();
		resendCount = 0;
		enableSend();
	}
	
	public void loadEncryptionKeys(){
		//TODO: load soft-encryption keys from DB.
	}
	
	public void update(AnsiContext context){
		lastAATime = context.lastAATime;
		this.authenticationValue = context.authenticationValue;
		this.epsem_control = context.epsem_control;
	}
	
	public final int nextFrameCounter(){
		return frameCounter++;
	}
	
	public final byte[] makeInitVector( byte[]cipherText, int offset ){
		ByteBuffer iv = ByteBuffer.allocate(12);
		iv.put(this.meterSysTitle);
		iv.put(cipherText, offset, 4);
		return iv.array();
	}
	
	
	public void reset(){
		aaState = AAState.IDLE;
	}
	
	public void setepsem_control(UserInformationElement userInf){
		
		
		
	}

	public final String getMeterId() {
		return meterId;
	}

	public final void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public final EPSEM_CONTROL getepsem_control() {
		return epsem_control;
	}

	public final byte[] getAuthenticationValue() {
		return authenticationValue;
	}

	public final void setAuthenticationValue(byte[] authValue) {
		this.authenticationValue = authValue;
	}
	
	public final int getNextInvokeId(){
		invokeId++;
		if( invokeId> 0x7F )
			invokeId = 1;
		return invokeId;
	}


	public int getCommandType() {
		return commandType;
	}


	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}
}
