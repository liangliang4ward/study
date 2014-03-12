package com.gshine.rmitalker.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.gshine.rmitalker.client.ClientEnd;
import com.gshine.rmitalker.common.User;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: dlut
 * </p>
 * 
 * @author g.shine
 * @version 1.0
 */

public interface TalkerServer extends Remote {

	//登录，输入参数中的User对象里面只用到id和pwd,同时向服务器注册，便于服务器回叫，向客户端传信息
	public boolean login(String id,String pwd,ClientEnd client)throws RemoteException;

	// 注册新用户，输入的User中不含有ID号，服务器返回带有ID号的User对象
	public User register(User u)throws RemoteException;
	
	//返回所有用户ID,便于用户加好友
	public User[] getAllUsers()throws RemoteException;
	
	//获取好友列表
	public User[] getAllFriends(String id)throws RemoteException;
	
	//添加好友
	public boolean addFriend(String sid,String fid)throws RemoteException;
	
	//发送消息
	public boolean sendMessage(String fromID,String toID,String message)throws RemoteException;
	
	//注销登录
	public boolean logout(String id)throws RemoteException;
	
	//获取离线消息
	public void getOfflineMsg(String id)throws RemoteException;
	
	//根据ID获得用户信息
	public User getUserById(String id)throws RemoteException;
}