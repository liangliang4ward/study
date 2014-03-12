package com.gshine.rmitalker.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.gshine.rmitalker.common.User;
import com.gshine.rmitalker.server.TalkerServer;

public class UserListPane extends JScrollPane {

	JPanel contentPane;

	JList userlist;

	DefaultListModel model;

	String id;

	TalkerServer server;

	public UserListPane(String id, TalkerServer server) {
		this.id = id;
		this.server = server;

		Color bgc = new Color(119, 202, 250);

		contentPane = new JPanel(new BorderLayout());
		userlist = new JList();
		userlist.setCellRenderer(new MyCellRenderer());
		userlist.setBackground(Color.white);
		contentPane.add(userlist, BorderLayout.CENTER);

		JPanel setupPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setupPane.setBackground(bgc);
		JButton btnAdd = new JButton();
		String url = ClassLoader.getSystemResource("resource/search.jpg").toString()
				.substring(6);
		btnAdd.setIcon(new ImageIcon(url));
		btnAdd.setBorder(BorderFactory.createEmptyBorder());
		setupPane.add(btnAdd);
		contentPane.setBorder(BorderFactory.createLineBorder(bgc, 20));
		contentPane.add(setupPane, BorderLayout.SOUTH);
		this.add(contentPane);

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null, "test");
				SearchFriendFrm search = new SearchFriendFrm(UserListPane.this,
						UserListPane.this.id, UserListPane.this.server);
				search.setVisible(true);
			}
		});
		userlist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					JList users = (JList) me.getSource();
					int index = users.locationToIndex(me.getPoint());
					DefaultListModel model=(DefaultListModel)users.getModel();
					User user = (User) model.getElementAt(index);
					String msg = user.getMessage();
					Conversation conv;
					if (msg != null && msg.length() > 0) {
						conv = new Conversation(user.getId(),UserListPane.this.id,  msg, UserListPane.this.server);
						user.setMessage(null);
//						model.setElementAt(user, index)
					} else {
						conv = new Conversation(UserListPane.this.id, user
								.getId(), null, UserListPane.this.server);
					}

					conv.setVisible(true);
				}
			}
		});
		try {
			User[] users = server.getAllFriends(id);
			// userlist.setListData(users);
			model = new DefaultListModel();
			for (int i = 0; i < users.length; i++) {
				model.addElement(users[i]);
			}
			userlist.setModel(model);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "错误(取得好友列表):无法连接服务器");
			// e.printStackTrace();
		}
		getViewport().setView(contentPane);
	}

	public void updateUserlist(User[] users) {
		model.removeAllElements();
		for (int i = 0; i < users.length; i++) {
			model.addElement(users[i]);
		}
	}

	public void addUser(User user) {
		model.addElement(user);
	}

	public void receiveMessage(String from, String message) {
		User user = null;
		// System.out.println(from+":"+message);
		for (int i = 0; i < model.size(); i++) {
			user = (User) model.getElementAt(i);
			if (user.getId().equals(from)) {
				String s=user.getMessage();
				s=(s==null?"":s);
				s+=from+"说："+message+"\n\r";
				user.setMessage(s);
				model.remove(i);
				model.add(i, user);
				break;
			}
		}
		// System.out.println(user.getId()+":"+user.getMessage());
	}

}

class MyCellRenderer extends JLabel implements ListCellRenderer {
	static ImageIcon boyIcon = null;

	static ImageIcon boygray = null;

	static ImageIcon girlIcon = null;

	static ImageIcon girlgray = null;
	static {
		try {
			String imgurl = ClassLoader.getSystemResource("resource/boy1.jpg")
					.toString().substring(6);
			boyIcon = new ImageIcon(imgurl);
			imgurl = ClassLoader.getSystemResource("resource/girl1.jpg").toString()
					.substring(6);
			girlIcon = new ImageIcon(imgurl);
			imgurl = ClassLoader.getSystemResource("resource/boy2.jpg").toString()
					.substring(6);
			boygray = new ImageIcon(imgurl);
			imgurl = ClassLoader.getSystemResource("resource/girl2.jpg").toString()
					.substring(6);
			girlgray = new ImageIcon(imgurl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This is the only method defined by ListCellRenderer.
	// We just reconfigure the JLabel each time we're called.

	public Component getListCellRendererComponent(JList list, Object value, // value
																			// to
																			// display
			int index, // cell index
			boolean isSelected, // is the cell selected
			boolean cellHasFocus) // the list and the cell have the focus
	{
		User user = (User) value;
		String sex = "男";
		String name = user.getName();
		String id = user.getId();
		// System.out.println(user.getMessage()+"ok");
		
		boolean online = user.getOnline();
		setText(name + "(" + id + ")");
		ImageIcon img = null;
		if (sex.equals("男")) {
			if (online) {
				img = boyIcon;
			} else {
				img = boygray;
			}
		} else {
			if (online)
				img = girlIcon;
			else
				img = girlgray;
		}

		setIcon(img);
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		//	setAlignmentY(1);
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		if (user.getMessage() != null && user.getMessage().length() > 0) 
			setForeground(Color.ORANGE);
		setOpaque(true);
		return this;
	}
}
