package cn.hexing.fk.rmi.fe.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import cn.hexing.fk.rmi.fe.client.IRtuInfoClientObject;
import cn.hexing.fk.rmi.fe.model.RtuCommunicationInfo;
/**
 * 
 * @author gaoll
 *
 * @time 2013-9-6 下午02:48:51
 *
 * @info 终端信息客户端调用服务端接口
 */
public interface IRtuInfoServerObject extends Remote{

	
	/**
	 *	 提供客户端注册接口
	 * @param id   clientId
	 * @param client Client
	 * @return
	 */
	public boolean register(String id,IRtuInfoClientObject client)throws RemoteException;
	
	
	/**
	 *	获得当前终端工况信息 
	 * @param id    clientId
	 * @param rtuId 终端id
	 * @return
	 */
	public RtuCommunicationInfo pollRtuState(String id,String rtuId)throws RemoteException;
	

	/**
	 * 
	 * @param rtuId
	 * @param info
	 * @return
	 */
	public void pushRtuState(RtuCommunicationInfo info)throws RemoteException;
	
	/**
	 * 
	 * @param rtuId
	 * @param info
	 * @return
	 */
	public void pushMsg(RtuCommunicationInfo info) throws RemoteException;
	
	public void logout(String id) throws RemoteException;
	
	
	
	
	
	
	
	
	
}
