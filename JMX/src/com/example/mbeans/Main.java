/* Main.java - main class for Hello World example.  Create the
   HelloWorld MBean, register it, then wait forever (or until the
   program is interrupted).  */

package com.example.mbeans;

import java.lang.management.*;
import javax.management.*;

import com.sun.jdmk.comm.HtmlAdaptorServer;

public class Main {
    /* For simplicity, we declare "throws Exception".  Real programs
       will usually want finer-grained exception handling.  */
    public static void main(String[] args) throws Exception {
	// Get the Platform MBean Server
	MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

	// Construct the ObjectName for the MBean we will register
	ObjectName name = new ObjectName("com.example.mbeans:type=Hello");
	ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");   
    HtmlAdaptorServer adapter = new HtmlAdaptorServer();   
	// Create the Hello World MBean
	Hello mbean = new Hello();

	// Register the Hello World MBean
	mbs.registerMBean(mbean, name);
	mbs.registerMBean(adapter, adapterName);
	adapter.start();
	// Wait forever
	System.out.println("Waiting forever...");
	Thread.sleep(Long.MAX_VALUE);
    }
}
