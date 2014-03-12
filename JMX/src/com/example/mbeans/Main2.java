package com.example.mbeans;

import java.io.IOException;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

public class Main2 {
	public static void main(String[] args) throws IOException {
		System.setProperty("java.net.preferIPv4Stack","true");
		MBeanServer server = MBeanServerFactory.createMBeanServer();
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/server");
		JMXConnectorServer s = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
		s.start();
	}
}
