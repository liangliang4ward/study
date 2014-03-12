package com.gshine.rmitalker.common;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @Title:
 * @Description:
 * @Copyright: Copyright (c) 2008
 * @Company: dlut
 * @author g.shine
 * @version 1.0
 */

public class DBManager {
	private static String driver = "sun.jdbc.odbc.JdbcOdbcDriver";

	private static String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=";

	private static String dbfilename = "rmitalker.mdb";

	private Connection con = null;

	private Statement stmt = null;

	private ResultSet rs = null;

	static {
		try {
			InputStream is= ClassLoader.getSystemResourceAsStream("dbconfig.properties");
			Properties property = new Properties();
			property.load(is);
			String tempdriver = property.getProperty("driver");
			String tempurl = property.getProperty("url");
			String tempdbfilename = property.getProperty("dbfilename");
		//	System.out.println(tempdriver+","+tempurl+tempdbfilename);
			is.close();
			if (tempdriver != null) {
				driver = tempdriver;
			}
			if (tempurl != null)
				url = tempurl;
			if (tempdbfilename != null)
				dbfilename = tempdbfilename;
			dbfilename=ClassLoader.getSystemResource(dbfilename).toString().substring(6);
		//	System.out.println(driver+","+url+dbfilename);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Class.forName(driver);
		//	System.out.println("database driver is ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DBManager() {
		con = getCon();
		if (con != null) {
			try {
				stmt = con.createStatement();
				//System.out.println("statement is ok");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//更新操作，返回影响的函数，失败返回-1
	public  int  update(String sql){
		int result=-1;
		try {
			result=stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//获取结果，用于只返回一个值的查询，失败返回null
	public Object getOnlyResult(String sql){
		Object o=null;
		try {
			rs=stmt.executeQuery(sql);
			if(rs.next()){
				o=rs.getObject(1);
			}else{
				o="";
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
	
	//获取结果，返回查询结果，没有结果则为null
	public String[][]getResults(String sql){
		String[][] result=null;
		ArrayList tempa=new ArrayList();
		try {
			rs=stmt.executeQuery(sql);
			ResultSetMetaData rsma=rs.getMetaData();
			int colcount=rsma.getColumnCount();
			while(rs.next()){
				String[]temprow=new String[colcount];
				for (int i = 0; i < temprow.length; i++) {
					temprow[i]=rs.getString(i+1);
				}
				tempa.add(temprow);
			}
			int rowcount=tempa.size();
			result=new String[rowcount][colcount];
			for (int i = 0; i < rowcount; i++) {
				result[i]=(String[])tempa.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	//获取连接，若失败则返回null
	private Connection getCon() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url + dbfilename);
		//	System.out.println("Connection can be get");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public PreparedStatement getPrepareStmt(String sql){
		PreparedStatement ps=null;
		try {
			 ps=con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}
	//关闭连接
	public void close(){
		if(con!=null){
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				con=null;
			}
		}
	}
//	public Connection getConnection() {
//		return this.con;
//	}
}