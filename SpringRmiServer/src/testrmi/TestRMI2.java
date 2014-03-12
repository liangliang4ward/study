package testrmi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.rmi.RmiServiceExporter;

public class TestRMI2 {
	public static void main(String[] args) {
		 ApplicationContext ctx = new ClassPathXmlApplicationContext("rmi.xml");
		 RmiServiceExporter service = (RmiServiceExporter) ctx.getBean("userSvcExporter");
		 System.out.println(service);
	}
}