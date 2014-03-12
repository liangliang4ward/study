package com.gshine.rmitalker.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.gshine.rmitalker.common.User;
import com.gshine.rmitalker.common.UserOp;

public class Server extends JFrame {

	private Thread thread;
	JPanel p1, p2;

	JButton start, stop;
	JTextField txtIP;
	JTextField txtPort;
	JLabel lblMsg;
	JTabbedPane tb;
	String host="",port="1099";
	JTable usersOnline, usersAll;

	DefaultTableModel tm1, tm2;
	
	TalkerServerImpl server = null;
	
	public Server() {
		try{
			server=new TalkerServerImpl();
			host+=InetAddress.getLocalHost().getHostAddress();
		}catch(Exception e){
			e.printStackTrace();
		}
		this.setTitle("RmiTalker服务器 1.0");
//		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
//						ClassLoader.getSystemResource("a.jpg").toString()
//								.substring(6)));
		Container container = getContentPane();
		p1 = new JPanel();
		start = new JButton("开始");
		stop = new JButton("关闭");
		
		p1.add(start);
		p1.add(stop);
		p2 = new JPanel();
		tb = new JTabbedPane();
		p2.setLayout(new BorderLayout());
		p2.add(tb, BorderLayout.CENTER);
		String[] cols = { "ID", "昵称", "性别" };
		tm1 = new DefaultTableModel();
		tm1.setColumnIdentifiers(cols);
		tm2 = new DefaultTableModel();
		tm2.setColumnIdentifiers(cols);
		usersOnline = new JTable(tm1);
		usersAll = new JTable(tm2);
		JScrollPane sp = new JScrollPane(usersOnline);
		JScrollPane sp1 = new JScrollPane(usersAll);
		tb.addTab("在线用户", sp);
		tb.addTab("所有注册用户", sp1);
		JPanel p3=new JPanel(new FlowLayout(FlowLayout.LEFT));
		txtIP=new JTextField(host);
		txtIP.setEditable(false);
		txtPort=new JTextField("1099",4);
		lblMsg=new JLabel("");
		p3.add(new JLabel("服务器IP："));
		p3.add(txtIP);
		p3.add(new JLabel("端口号："));
		p3.add(txtPort);
		JButton btn1=new JButton("设置");
		btn1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Server.this.port=Server.this.txtPort.getText();
			}
		});
		p3.add(btn1);
		p3.add(lblMsg);
		txtIP.setForeground(Color.BLUE);
		txtPort.setForeground(Color.BLUE);
		lblMsg.setForeground(Color.RED);
		container.setLayout(new BorderLayout(8, 8));
		container.add(p1, BorderLayout.NORTH);
		container.add(p2, BorderLayout.CENTER);
		container.add(p3, BorderLayout.SOUTH);
		setSize(500, 650);
		setCenter();

//		stop.setEnabled(false);
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
//				stop.setEnabled(true);
				thread=new Listener(Server.this,Server.this.server);
				thread.start();
			}
		});
		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
//		updateUsers(UserOp.getAllUsers());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void updateUserOnline(String[] id) {
		if (id == null)
			return;

		int count = id.length;
		User user = null;
	//	System.out.println(tm1.getRowCount()+":"+count);
//		for (int i = 0; i < tm1.getRowCount(); i++) {
//			tm1.removeRow(i);
//		}
		tm1.getDataVector().clear();
		tm1.fireTableDataChanged();
		for (int i = 0; i < count; i++) {
			user = UserOp.getUserById(id[i]);
			String[] rowData = { user.getId(), user.getName(), user.getSex() };
			tm1.addRow(rowData);
		}
	}

	public void updateUsers(User[] users) {
		if (users == null)
			return;
		tm2.getDataVector().clear();
		tm2.fireTableDataChanged();
		for (int i = 0; i < users.length; i++) {
			String[] rowData = { users[i].getId(), users[i].getName(),
					users[i].getSex() };
			tm2.addRow(rowData);
		}
	}
	
	public void addOnlineUser(User user){
		if(user==null) return;
		String[]rowdata={user.getId(),user.getName(),user.getSex()};
		tm1.addRow(rowdata);
	}

	public void removeOnlineUser(String id){
		if(id==null||id=="") return;
		Vector v=tm1.getDataVector();
		int size=v.size();
		for(int i=0;i<size;i++){
			if(((Vector)v.elementAt(i)).elementAt(0).toString().equals(id)){
				v.remove(i);
				break;
			}
		}
		tm1.fireTableDataChanged();
	}
	private void setCenter() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		setLocation((scrSize.width - thisSize.width) / 2,
				(scrSize.height - thisSize.height) / 2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Server server = new Server();
		server.setVisible(true);
//		double s=0.0023;
//		DecimalFormat df=new DecimalFormat("########0.####");
//		System.out.println(df.format(s));
	}

}

class Listener extends Thread {
	public Listener(){
		
	}
	private Server frame;
	private TalkerServerImpl server;
	public Listener(Server frame,TalkerServerImpl server){
		this.frame=frame;
		this.server=server;
	}
	public void run() {
		try {
			if(server==null||frame==null) return;
			server.setFrame(frame);
			LocateRegistry.createRegistry(Integer.parseInt(frame.port));
			Naming.rebind("rmi://"+frame.host+":"+frame.port+"/server", server);
//			System.out.println("服务器正在运行.....");
			frame.lblMsg.setText("服务器正在运行……");
		} catch (Exception ex) {
			ex.printStackTrace();
			frame.lblMsg.setText("服务器未能成功启动！！");
		}
	}
	
}
