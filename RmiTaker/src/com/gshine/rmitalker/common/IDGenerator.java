/**
 * 
 */
package com.gshine.rmitalker.common;

/**
 * @author gshine
 * 
 */
public class IDGenerator {
	private static String ID;

	static {
		try {
			DBManager dbm = new DBManager();
			String sql = "select count(*) from user";
			String maxID = dbm.getOnlyResult(sql).toString();
			ID = String.valueOf(10000 + Integer.parseInt(maxID) - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized String getNewID() {
		ID = String.valueOf(Long.parseLong(ID) + 1);
		return ID;
	}

	public static void main(String[] args) {
		System.out.println(getNewID());
		System.out.println(getNewID());
	}
}
