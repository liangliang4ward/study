/**
 * 
 */
package com.gshine.rmitalker.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.gshine.rmitalker.common.User;

/**
 * @author Administrator
 * 
 */
public interface ClientEnd extends Remote {

	// 接受好友列表
	public boolean sendFriendlist(User[] users) throws RemoteException;

	// 接受好友消息
	public boolean sendMessage(String id, String message)
			throws RemoteException;

	// 接受服务器的断开消息
	public void serverShutup() throws RemoteException;

	// 号码重复登录
	public void IDReLogin() throws RemoteException;

	// 添加好友到列表中
	public void addUser(User user) throws RemoteException;
}
