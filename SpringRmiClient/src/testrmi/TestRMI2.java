package testrmi;

import leon.rmi.iface.IUserDao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestRMI2 {
	public static void main(String[] args) {
		 ApplicationContext ctx = new ClassPathXmlApplicationContext("springbean.xml");
		 IUserDao userDao = (IUserDao) ctx.getBean("userDaoProxy");
		 System.out.println(userDao.getUserList());
		 System.out.println(userDao.sum(145, 487));
	}
}