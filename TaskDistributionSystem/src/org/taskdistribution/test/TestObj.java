package org.taskdistribution.test;

import org.taskdistribution.Server.Request;


public class TestObj {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		Class<Request> clazz = Request.class;
		clazz.newInstance();
	}
}
