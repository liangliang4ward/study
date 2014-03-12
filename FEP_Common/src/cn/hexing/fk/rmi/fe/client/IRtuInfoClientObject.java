package cn.hexing.fk.rmi.fe.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import cn.hexing.fk.rmi.fe.model.RtuCommunicationInfo;

/**
 * 
 * @author gaoll
 *
 * @time 2013-9-6 下午02:50:15
 *
 * @info 服务端通过当前接口，告之客户端新消息
 */
public interface IRtuInfoClientObject extends Remote{

	public void rtuStateChange(RtuCommunicationInfo info)throws RemoteException;

	public void rtuMsgComing(RtuCommunicationInfo info)throws RemoteException;

}
