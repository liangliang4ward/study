package cn.hexing.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.GatewayException;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageEncodings;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.crypto.AESKey;
import org.smslib.modem.SerialModemGateway;

import cn.hexing.sms.processor.DefaultSmsMsgProcessor;
import cn.hexing.sms.processor.SmsMsgProcessor;

public class SmsService {

	private static SmsService instance;
	
	private Service smsService;
	
	/**消息处理器*/
	private SmsMsgProcessor processor = new DefaultSmsMsgProcessor();

	/**
	 * formate: simNum#gateId#comNum#baud#factory#SMS_centor;
	 **/
	private String smsInfos;
	
	private SmsService(){
	}
	
	
	
	public synchronized static SmsService getInstance(){
		if (instance == null)
			instance = new SmsService();
		return instance;
	}
	
	public void stopService() throws Exception{
		smsService.stopService();
	}
	
	public void startService() throws Exception{
		
		smsService = Service.getInstance();
		
		InboundNotification inboundNotification = new InboundNotification();
		CallNotification callNotification = new CallNotification();
		
		GatewayStatusNotification statusNotification = new GatewayStatusNotification();
		
		OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();
		
		smsService.setInboundMessageNotification(inboundNotification);
		
		smsService.setCallNotification(callNotification);
		
		smsService.setGatewayStatusNotification(statusNotification);
		
		smsService.setOrphanedMessageNotification(orphanedMessageNotification);
		
		OutboundNotification outboundNotification = new OutboundNotification();
		
		smsService.S.SERIAL_POLLING = true;
		
		smsService.setOutboundMessageNotification(outboundNotification);
		
		String[] infos=smsInfos.split(";");
		for(String info : infos){
			
			String[] infomation = info.split("#");
			
			SerialModemGateway gateway = new SerialModemGateway(infomation[1], infomation[2], Integer.parseInt(infomation[3]), infomation[4], "");
			
			gateway.setProtocol(Protocols.PDU);
			
			gateway.setInbound(true);
			
			gateway.setOutbound(true);
			
			gateway.setSimPin("0000");
			
			gateway.setSmscNumber(infomation[5]); //短信中心号码+8613800571500
													//当前短信猫sim卡号码
			smsService.getKeyManager().registerKey(infomation[0], new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
			
			smsService.addGateway(gateway);
		}
		
		
		smsService.startService();
		
		//启动读线程
		new WorkThread().start();
	}
	/**
	 * @param messageClasses  消息类，READ,UNREAD,ALL,读到的消息，直接删除了。
	 * @throws TimeoutException
	 * @throws GatewayException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public int readMessages(MessageClasses messageClasses,List<InboundMessage> msgList) throws Exception{
		return smsService.readMessages(msgList,messageClasses);
	}
	/**
	 * 批量删除消息
	 * @param msgs
	 */
	public void deleteMessages(List<InboundMessage> msgs) {
		for(InboundMessage msg:msgs){
			deleteMessage(msg);
		}
	}
	
	/**
	 * 删除一个消息
	 * @param msg
	 * @return
	 */
	public boolean deleteMessage(InboundMessage msg) {
		try {
			return this.smsService.deleteMessage(msg);
		} catch (TimeoutException e) {
		} catch (GatewayException e) {
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
		return false;
	}
	/**
	 * @param mobileNum 要发送的手机号码
	 * @param content	内容
	 * @return
	 * @throws TimeoutException
	 * @throws GatewayException
	 * @throws SMSLibException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean sendMessage(String mobileNum,String content) throws Exception{
		OutboundMessage message = new OutboundMessage(mobileNum,content);
		message.setEncoding(MessageEncodings.ENCUCS2);
		return smsService.sendMessage(message);
	}
	
	public class InboundNotification implements IInboundMessageNotification
	{
		public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg)
		{
//			if (msgType == MessageTypes.INBOUND) System.out.println(">>> New Inbound message detected from Gateway: " + gateway.getGatewayId());
//			else if (msgType == MessageTypes.STATUSREPORT) System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
		}
	}

	public class CallNotification implements ICallNotification
	{
		public void process(AGateway gateway, String callerId)
		{
//			System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
		}
	}

	public class GatewayStatusNotification implements IGatewayStatusNotification
	{
		public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus)
		{
			//System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}

	public class OrphanedMessageNotification implements IOrphanedMessageNotification
	{
		public boolean process(AGateway gateway, InboundMessage msg)
		{
			//System.out.println(">>> Orphaned message part detected from " + gateway.getGatewayId());
			//System.out.println(msg);
			// Since we are just testing, return FALSE and keep the orphaned message part.
			return false;
		}
	}
	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(AGateway gateway, OutboundMessage msg)
		{
			//System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
			//System.out.println(msg);
		}
	}
	
	class WorkThread extends Thread{

		@Override
		public void run() {
			while(true){
				
				try {
					List<InboundMessage> msgs = new ArrayList<InboundMessage>(); 
					readMessages(MessageClasses.ALL,msgs);
					for(InboundMessage msg:msgs){
						processor.process(msg);
						deleteMessage(msg);
					}
					Thread.sleep(1000);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		
	}

	public final SmsMsgProcessor getProcessor() {
		return processor;
	}



	public final void setProcessor(SmsMsgProcessor processor) {
		this.processor = processor;
	}



	public final String getSmsInfos() {
		return smsInfos;
	}



	public final void setSmsInfos(String smsInfos) {
		this.smsInfos = smsInfos;
	}
}
