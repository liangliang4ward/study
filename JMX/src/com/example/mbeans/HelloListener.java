package com.example.mbeans;

import javax.management.Notification;
import javax.management.NotificationListener;

public class HelloListener implements NotificationListener{

	@Override
	public void handleNotification(Notification notification, Object handback) {
		System.out.println("I know that.");
	}

}
