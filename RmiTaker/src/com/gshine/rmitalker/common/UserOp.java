package com.gshine.rmitalker.common;

import java.sql.*;
import java.sql.Date;
import java.util.*;

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

public class UserOp {
	public UserOp() {
	}

	public static String newUser(User user) {
		String id = null;
		id = IDGenerator.getNewID();
		String sql = "";
		String name = user.getName();
		String realname = user.getRealname();
		String pwd = user.getPwd();
		String sex = user.getSex();
		if (id != null && id != "") {
			sql = "insert into user values(?,?,?,?,?,?)";
			DBManager dbm = new DBManager();
			PreparedStatement ps = dbm.getPrepareStmt(sql);
			if (ps != null) {
				try {
					ps.setString(1, id);
					ps.setString(2, name);
					ps.setString(3, realname);
					ps.setString(4, pwd);
					ps.setString(5, sex);
					ps.setBoolean(6, true);
					ps.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dbm.close();
		}
		return id;
	}

	public static User getUserById(String id) {
		User user = new User();
//		DBManager dbm = new DBManager();
//		if (id != null && id != "") {
//			String sql = "select * from user where ID='" + id
//					+ "' and isvalid=true";
//			String[][] rs = dbm.getResults(sql);
//			if (rs != null) {
//				user.setId(id);
//				user.setName(rs[0][1]);
//				user.setRealname(rs[0][2]);
//				user.setPwd(rs[0][3]);
//				user.setSex(rs[0][4]);
//			}
//			dbm.close();
//		}
		user.setId(id);
		user.setName("looking");
		user.setSex("ÄÐ");
		return user;
	}

	public static User[] getAllUsers() {
		ArrayList<User> list = new ArrayList<User>();
//		DBManager dbm = new DBManager();
//		String sql = "select id,name,realname,pwd,sex from user where isvalid=true";
//		String[][] rs = dbm.getResults(sql);
//		if(rs==null) return null;
//		int icount=rs.length;
//		for(int i=0;i<icount;i++){
//			User u=new User();
//			u.setId(rs[i][0]);
//			u.setName(rs[i][1]);
//			u.setRealname(rs[i][2]);
//			u.setPwd(rs[i][3]);
//			u.setSex(rs[i][4]);
//			list.add(u);
//		}
//		dbm.close();
		for(int i=0;i<5;i++){
		User u=new User();
		u.setId("5490300"+i);
		u.setName("Zombie-"+i);
		u.setRealname("Computer-"+i);
		list.add(u);
	}
		User[]users=new User[list.size()];
		for(int i=0;i<users.length;i++){
			users[i]=(User)list.get(i);
		}
		return users;
	}
	public static boolean canLogin(String id,String pwd){
		boolean result=true;
//		if(!(id==null||pwd==null||id.trim()==""||pwd.trim()=="")){
//			DBManager dbm=new DBManager();
//			String sql="select id from user where id='"+id+"' and pwd='"+pwd+"'";
//			String resultid=(String)dbm.getOnlyResult(sql);
//			if(resultid!=null&&resultid!="") result=true;
//			dbm.close();
//		}
		return result;
	}
	public static  boolean saveMessage(String fromId,String toId,String message){
		if(fromId!=null&&fromId.trim()!=""&&toId!=null&&toId.trim()!=""&&
			message!=null&&message.trim()!=""){
			DBManager dbm=new DBManager();
			java.sql.Date dt=new java.sql.Date(System.currentTimeMillis());
			//String sql="insert into message (from,to,message,date,isread) values ('";
			//sql+=fromId+"','"+toId+"','"+message+"',"+dt+",false)";
		//	dbm.update(sql);
			String sql="insert into message ([from],to,message,send_date,isread) values (?,?,?,?,false)";
			PreparedStatement ps=dbm.getPrepareStmt(sql);
			if(ps!=null){
				try {
					ps.setString(1, fromId);
					ps.setString(2, toId);
					ps.setString(3, message);
					ps.setDate(4, dt);
					ps.execute();
					ps.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dbm.close();
			return true;
		}
		return false;
	}
	
	public static ArrayList getAllUnReadMsg(String id){
		ArrayList a=new ArrayList();
		Message m = new Message();
		m.from="54903001";
		m.to="54903099";
		m.message="test";
		m.date = "2013-02-02";
		a.add(m);
//		DBManager dbm = new DBManager();
//		String sql = "select [from],to,message,send_date from message where isread=false and to='"+id+"' order by send_date";
//		String[][] rs = dbm.getResults(sql);
//		if(rs==null) return null;
//		int icount=rs.length;
//		for(int i=0;i<icount;i++){
//			Message m=new Message();
//			m.from=rs[i][0];
//			m.to=rs[i][1];
//			m.message=rs[i][2];
//			m.date=rs[i][3];
//			a.add(m);
//		}
//		sql="update message set isread=true where to='"+id+"'";
//		dbm.update(sql);
//		dbm.close();
		return a;
	}
	public static void main(String[]args){
		//saveMessage("10000", "10001", "hello");
	}
}