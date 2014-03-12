package com.example.mbeans;

import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class RemoteClient {
	MBeanServerConnection connection = null;

	public RemoteClient() throws Exception {
		JMXServiceURL url = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://localhost:9999/server");
		JMXConnector connector = JMXConnectorFactory.connect(url);
		connection = connector.getMBeanServerConnection();
	}

	void deploy() throws Exception {
		connection.createMBean("com.example.mbeans.Hello", new ObjectName(
				"com.example.mbeans:type=Worker,number=1"));
		connection.createMBean("com.example.mbeans.Hello", new ObjectName(
				"com.example.mbeans:type=Worker,number=2"));
		connection.createMBean("com.example.mbeans.Hello", new ObjectName(
				"com.example.mbeans:type=Worker,number=3"));
	}

	void lookup() throws Exception {
		ObjectName on = new ObjectName("com.wisely.jmx:*");
		Set set = connection.queryNames(on, null);
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			ObjectName bean = (ObjectName) iter.next();
			System.out.println("deployed..=" + bean.toString());
		}
	}

	void checkRunning() throws Exception {
		ObjectName on = new ObjectName("com.wisely.jmx:*");
		QueryExp exp = Query.eq(Query.attr("Running"), Query.value(true));
		Set set = connection.queryNames(on, exp);
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			ObjectName bean = (ObjectName) iter.next();
			System.out.println("running.. MBean =" + bean.toString());
		}
	}

	void remove() throws Exception {
		ObjectName on = new ObjectName("com.wisely.jmx:*");
		Set set = connection.queryNames(on, null);
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			ObjectName bean = (ObjectName) iter.next();
			System.out.println("removing..=" + bean.toString());
			connection.unregisterMBean(bean);
		}
	}

	public void addMonitor() throws Exception {
		connection.createMBean("javax.management.monitor.StringMonitor",
				new ObjectName("wrox.ch12.jmx:name=WorkMonitor"));
	}

	public static void main(String[] args) throws Exception {
		RemoteClient rc = new RemoteClient();
		rc.remove();
		rc.deploy();
		rc.lookup();
		rc.checkRunning();
		rc.addMonitor();
	}
}
