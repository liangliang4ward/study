package com.gshine.rmitalker.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.gshine.rmitalker.common.User;
import com.gshine.rmitalker.server.TalkerServer;

public class SearchFriendFrm extends JFrame implements ActionListener {
	
	JTable users;
	DefaultTableModel tm;
	JTextField txtID;
	String[]colnames={"ID","昵称","性别"};
	TalkerServer server;
	String id;
	UserListPane scrollPane;
	public SearchFriendFrm(UserListPane p1,String id,TalkerServer server){
		setTitle("添加好友");
		if(Login.icon!=null){
			setIconImage(Login.icon);
		}
		Font font = new Font("宋", Font.PLAIN, 12);
		Color fcolor=new Color(13, 55, 85);
		Color c1=new Color(241, 250, 255);
		this.server=server;
		this.id=id;
		this.scrollPane=p1;
		Container container=getContentPane();
		container.setLayout(null);
		JPanel pane1=new JPanel(new BorderLayout());
		tm=new DefaultTableModel();
		tm.setColumnIdentifiers(colnames);
		users=new JTable(tm);
		users.setBackground(c1);
		pane1.add(new JScrollPane(users));
		pane1.setBounds(10,10,350,200);
		container.add(pane1);
		setSize(380,300);
		setResizable(false);
		txtID=new JTextField();
		txtID.setBounds(12,225,90,25);
		JButton btnQuit=new JButton("关闭");
		btnQuit.setFont(font);
		btnQuit.setForeground(fcolor);
		btnQuit.setBackground(c1);
		JButton btnSearch=new JButton("查找");
		btnSearch.setFont(font);
		btnSearch.setForeground(fcolor);
		btnSearch.setBackground(c1);
		btnSearch.setBounds(105,225,60,25);
		JButton btnAdd=new JButton("加为好友");
		btnAdd.setFont(font);
		btnAdd.setBackground(c1);
		btnAdd.setForeground(fcolor);
		btnAdd.setBounds(190,225,90,25);
		btnQuit.setBounds(290,225,60,25);
		container.add(txtID);
		container.add(btnSearch);
		container.add(btnQuit);
		container.add(btnAdd);
		container.setBackground(new Color(126,194,213));
		btnSearch.addActionListener(this);
		btnQuit.addActionListener(this);
		btnAdd.addActionListener(this);
		setCenter();
		if(server!=null){
			try {
				updateUsers(server.getAllUsers());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "暂时无法连接服务器");
				e.printStackTrace();
			}
		}
	}
	private void setCenter() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		setLocation((scrSize.width - thisSize.width) / 2,
				(scrSize.height - thisSize.height) / 2);
	}
	public void updateUsers(User[] users) {
		if (users == null)
			return;
		tm.getDataVector().clear();
		tm.fireTableDataChanged();
		for (int i = 0; i < users.length; i++) {
			if(users[i].getId().equals(this.id)) continue;
			String[] rowData = { users[i].getId(), users[i].getName(),
					users[i].getSex() };
			tm.addRow(rowData);
		}
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd=e.getActionCommand();
		if(cmd.equals("关闭")){
			this.dispose();
		}else if(cmd.equals("查找")){
			String id=txtID.getText();
			if(id.trim().equals("")){
				JOptionPane.showMessageDialog(this, "请输入查找的用户ID");
				return;
			}
			try{
				Long.parseLong(id);
			}catch(Exception ex){
				JOptionPane.showMessageDialog(this, "请输入要查找的ID号码");
				return;
			}
			
			if(id.equals(this.id)) return;
			User user=null;
			try {
				user = server.getUserById(id);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "无法连接服务器");
				e1.printStackTrace();
			}
			if(user!=null){
				updateUsers(new User[]{user});
			}
		}else if(cmd.equals("加为好友")){
			int row=users.getSelectedRow();
			if(row<0) return;
			String id=(String)tm.getValueAt(row, 0);
			try {
				server.addFriend(this.id, id);
				JOptionPane.showMessageDialog(this, "成功添加"+id+"为好友");
			//	scrollPane.updateUserlist(server.getAllFriends(this.id));
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public static void main(String[]args){
		//new SearchFriendFrm(null).setVisible(true);
	}
}
