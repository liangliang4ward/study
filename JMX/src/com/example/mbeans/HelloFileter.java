package com.example.mbeans;

import javax.management.Notification;
import javax.management.NotificationFilter;

public class HelloFileter implements NotificationFilter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5621845407364147046L;

	@Override
	public boolean isNotificationEnabled(Notification notification) {
		
		return true;
	}

}
