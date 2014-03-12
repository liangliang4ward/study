package com.gshine.rmitalker.client;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.*;

import com.gshine.rmitalker.common.User;
import com.gshine.rmitalker.server.TalkerServer;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author g.shine
 * @version 1.0
 */

public class JQQClient extends JFrame {
	TalkerServer server;
	ClientEndImpl client;
	
	String id;
	JPanel header;
	UserListPane content;
	JLabel head,title;
	
	public JQQClient(String id, TalkerServer ts,ClientEnd client) {
		this.server = ts;
		this.id = id;
		if(client instanceof ClientEndImpl){
			this.client=(ClientEndImpl)client;
		}
		setTitle("RmiTalker2009-(ID:"+id+")");

		if(Login.icon!=null){
			setIconImage(Login.icon);
		}
		Container container=getContentPane();
		container.setLayout(new BorderLayout());
		
		header=new JPanel(new FlowLayout(FlowLayout.LEFT));
		Color bgc=new Color(119,202,250);
		header.setBackground(bgc);
		String imageurl="resource/boy.jpg";
		User user;
		try {
			user = ts.getUserById(id);
			if(user.getSex().equals("ÄÐ")){
				imageurl="resource/boy.jpg";
			}else{
				imageurl="resource/girl.jpg";
			}
			imageurl=ClassLoader.getSystemResource(imageurl).toString().substring(6);
			head=new JLabel(new ImageIcon(imageurl));
			head.setBorder(BorderFactory.createEtchedBorder());
			header.add(head);
			title=new JLabel(user.getName()+"("+user.getId()+")");
			header.add(title);
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		content=new UserListPane(id,ts);
		this.client.setClient(this);
		container.add(header,BorderLayout.NORTH);
		container.add(content,BorderLayout.CENTER);
		
		
		this.setSize(300, 600);
		setPosition();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					server.logout(JQQClient.this.id);
				} catch (RemoteException e1) {
				//	e1.printStackTrace();
				}
				System.exit(0);
			}
		});
	}
	private void setPosition() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		setLocation((scrSize.width - thisSize.width),40);
	}
	public void updateUserlist(User[]users){
		content.updateUserlist(users);
	}
	public void addUser(User user){
		content.addUser(user);
	}
	public void quit(String message){
		JOptionPane.showMessageDialog(this, message);
		System.exit(0);
	}
	public void receiveMessage(String from,String message){
//		Conversation converstion=new Conversation(from,id,message,this.server);
//		converstion.setVisible(true);
		content.receiveMessage(from, message);
	}
	public void receiveOfflineMessage(){
		try {
			this.server.getOfflineMsg(id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws HeadlessException {
		// JQQClient JQQClient1 = new JQQClient();
	}
}