package leon.rmi.impl;

import leon.rmi.iface.IUserDao;

public class UserDaoImpl implements IUserDao {

	@Override
	public String getUserList() {
		return "Hello,Get the user list from database!";
	}

	@Override
	public int sum(int a, int b) {
		return a+b;
	}
}