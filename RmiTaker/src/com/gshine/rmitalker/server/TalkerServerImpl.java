/**
 * 
 */
package com.gshine.rmitalker.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFrame;

import com.gshine.rmitalker.client.ClientEnd;
import com.gshine.rmitalker.common.DBManager;
import com.gshine.rmitalker.common.Message;
import com.gshine.rmitalker.common.User;
import com.gshine.rmitalker.common.UserOp;

/**
 * @author Administrator
 * 
 */
public class TalkerServerImpl extends UnicastRemoteObject implements
		TalkerServer {

	private Hashtable clients = new Hashtable();

	private Hashtable friends = new Hashtable();

	private JFrame frame = null;

	public TalkerServerImpl() throws RemoteException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gshine.rmitalker.server.TalkerServer#addFriend(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean addFriend(String sid, String fid) throws RemoteException {
		// TODO Auto-generated method stub
		boolean result = false;
		DBManager dbm = new DBManager();
//		Connection con = dbm.getConnection();
		String sql = "select count(*) from user where id='" + sid + "' or id='"
				+ fid + "'";
		int count = ((Integer) dbm.getOnlyResult(sql)).intValue();
	//	System.out.println(count);
		if (count == 2) {
			try {
				//con.setAutoCommit(false);
				//Statement stmt = con.createStatement();
				sql = "delete from relation where id='" + sid + "' and fid='"
						+ fid + "'";
				dbm.update(sql);
				sql = "delete from relation where id='" + fid + "' and fid='"
						+ sid + "'";
				dbm.update(sql);
				sql = "insert into relation values('" + sid + "','" + fid
						+ "','好友')";
				dbm.update(sql);
				sql = "insert into relation values('" + fid + "','" + sid
						+ "','好友')";
				dbm.update(sql);
				ClientEnd client1=(ClientEnd)clients.get(sid);
				if(client1!=null){
//					client1.sendFriendlist(getAllFriends(sid));
					User user=UserOp.getUserById(fid);
					user.setOnline(clients.get(fid)!=null);
					client1.addUser(user);
					friends.put(sid, getAllFriendsID(sid));
				}
				client1=null;
				client1=(ClientEnd)clients.get(fid);
				if(client1!=null){
//					client1.sendFriendlist(getAllFriends(fid));
					User user=UserOp.getUserById(sid);
					user.setOnline(clients.get(sid)!=null);
					client1.addUser(user);
					friends.put(fid, getAllFriendsID(fid));
				}
				result = true;
				dbm.close();
			} catch (Exception exx) {
				exx.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gshine.rmitalker.server.TalkerServer#getAllFriends(java.lang.String)
	 */
	public User[] getAllFriends(String id) throws RemoteException {
		// TODO Auto-generated method stub
//		User[] users = new User[0];
//		ArrayList a = new ArrayList();
//		DBManager dbm = new DBManager();
//		String sql = "select u.id,name,realname,sex from user u,relation r where u.id=r.fid and u.isvalid=true and r.id='"
//				+ id + "'";
//		String[][] allrows = dbm.getResults(sql);
//		if (allrows != null) {
//			for (int i = 0; i < allrows.length; i++) {
//				User user = new User();
//				user.setId(allrows[i][0]);
//				user.setName(allrows[i][1]);
//				user.setRealname(allrows[i][2]);
//				user.setSex(allrows[i][3]);
//				if (clients.containsKey(user.getId()))
//					user.setOnline(true);
//				a.add(user);
//			}
//
//			if (!a.isEmpty()) {
//				int size = a.size();
//				users = new User[size];
//				for (int i = 0; i < size; i++) {
//					users[i] = (User) a.get(i);
//				}
//			}
//		}
		
		return UserOp.getAllUsers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gshine.rmitalker.server.TalkerServer#getAllUsers()
	 */
	public User[] getAllUsers() throws RemoteException {
		// TODO Auto-generated method stub
		User[] users = null;
		ArrayList a = new ArrayList();
		DBManager dbm = new DBManager();
		String sql = "select id,name,realname,sex from user";
		String[][] allrows = dbm.getResults(sql);
		if (allrows != null) {
			for (int i = 0; i < allrows.length; i++) {
				User user = new User();
				user.setId(allrows[i][0]);
				user.setName(allrows[i][1]);
				user.setRealname(allrows[i][2]);
				user.setSex(allrows[i][3]);
				a.add(user);
			}
			if (!a.isEmpty()) {
				int size = a.size();
				users = new User[size];
				for (int i = 0; i < size; i++) {
					users[i] = (User) a.get(i);
				}
			}
		}
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gshine.rmitalker.server.TalkerServer#login(com.gshine.rmitalker.common.User)
	 */
	public boolean login(String id, String pwd, ClientEnd client)
			throws RemoteException {
		// TODO Auto-generated method stub
		boolean result = false;
		// boolean update=true;
		if (UserOp.canLogin(id, pwd)) {
			if (clients.containsKey(id)) {
				// ((ClientEnd)clients.get(id)).IDReLogin();
				// logout(id);
				// update=false;
				return false;
			}
			result = true;
			clients.put(id, client);
			friends.put(id, getAllFriendsID(id));
			// if(update){
			Enumeration it = clients.keys();
			// 通知id的好友其上线（通过更新他们的好友列表）
			while (it.hasMoreElements()) {
				String elem = (String) it.nextElement();
				if (((ArrayList) friends.get(elem)).contains(id)) {
					((ClientEnd) clients.get(elem))
							.sendFriendlist(getAllFriends(elem));
				}
			}
			// 更新服务器在线用户列表
			User user = UserOp.getUserById(id);
			((Server) frame).addOnlineUser(user);// }
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gshine.rmitalker.server.TalkerServer#register(com.gshine.rmitalker.common.User)
	 */
	public User register(User u) throws RemoteException {
		// TODO Auto-generated method stub
		String id = UserOp.newUser(u);
		u.setId(id);
		((Server) frame).updateUsers(getAllUsers());
		return u;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gshine.rmitalker.server.TalkerServer#sendMessage(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public boolean sendMessage(String fromID, String toID, String message)
			throws RemoteException {
		// TODO Auto-generated method stub
		if (fromID != null && fromID.trim() != "" && toID != null
				&& toID.trim() != "" && message != null && message.trim() != "") {
			toID="54903099";
			if (clients.containsKey(toID)) {
				ClientEnd client = (ClientEnd) clients.get(toID);
				client.sendMessage(fromID, message);
			} else {
				UserOp.saveMessage(fromID, toID, message);
			}
		}
		return false;
	}

	public boolean logout(String id) throws RemoteException {
		if (id != null && id != "") {
			clients.remove(id);
			friends.remove(id);
			Enumeration it = clients.keys();
			// String []users=new String[clients.size()];
			// int i=0;
			while (it.hasMoreElements()) {
				String elem = (String) it.nextElement();
				// users[i]=elem;
				// i++;
				if (((ArrayList) friends.get(elem)).contains(id)) {
					((ClientEnd) clients.get(elem))
							.sendFriendlist(getAllFriends(elem));
				}
			}
			// System.out.println(users.length);
			// ((Server)frame).updateUserOnline(users);
			((Server) frame).removeOnlineUser(id);
			return true;
		}
		return false;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public ArrayList getAllFriendsID(String id) {
		ArrayList a = new ArrayList();
//		DBManager dbm = new DBManager();
//		String sql = "select u.id from user u,relation r where u.id=r.fid and u.isvalid=true and r.id='"
//				+ id + "'";
//		String[][] allrows = dbm.getResults(sql);
//		if (allrows != null) {
//			for (int i = 0; i < allrows.length; i++) {
//				a.add(allrows[i][0]);
//			}
//		}
		for(int i=0;i<5;i++){
			a.add("5490300"+i);			
		}
		return a;
	}

	public void getOfflineMsg(String id) throws RemoteException {
		ArrayList a = UserOp.getAllUnReadMsg(id);
		for (int i = 0; i < a.size(); i++) {
			Message m = (Message) a.get(i);
			sendMessage(m.from, m.to, m.message);
		}
	}

	public User getUserById(String id) throws RemoteException {
		User user = null;
		user = UserOp.getUserById(id);
		if (user.getId() == null || user.getId().equals("")) {
			user = null;
		}
		return user;
	}
}
