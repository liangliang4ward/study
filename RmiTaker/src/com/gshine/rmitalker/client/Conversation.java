package com.gshine.rmitalker.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import com.gshine.rmitalker.server.TalkerServer;

public class Conversation extends JFrame implements ActionListener {

	private JPanel msgPane;

	private JPanel buttonPane;

	private JTextArea txtMsg;

	private JButton btnOK;

	private JButton btnQuit;

	private String from;

	private String to;

	private String msg;

	private TalkerServer server;

	private boolean send = false;

	public Conversation(String from, String to, String msg, TalkerServer server) {
		this.from = from;
		this.to = to;
		this.server = server;
		this.msg = msg;

		if (msg == null)
			send = true;
		Font font = new Font("宋", Font.PLAIN, 12);
		Color fcolor=new Color(13, 55, 85);
		Color c1=new Color(241, 250, 255);
		Container container = getContentPane();
		container.setLayout(null);
		Color bgc = new Color(119, 202, 250);
		container.setBackground(bgc);
		if(Login.icon!=null){
			setIconImage(Login.icon);
		}
		msgPane = new JPanel();
		msgPane.setBackground(bgc);
		msgPane.setBounds(10, 10, 390, 210);

		buttonPane = new JPanel();
		buttonPane.setBackground(bgc);
		buttonPane.setBounds(10, 220, 390, 290);
		buttonPane.setLayout(null);

		container.add(msgPane);
		container.add(buttonPane);

		txtMsg = new JTextArea(25, 33);
		msgPane.add(txtMsg);
		if (send) {
			btnOK = new JButton("发送");
		} else {
			btnOK = new JButton("回复");
		}
		btnOK.setBounds(200, 15, 70, 25);
		btnOK.setFont(font);
		btnOK.setForeground(fcolor);
		btnOK.setBackground(c1);
		btnOK.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		btnQuit = new JButton("关闭");
		btnQuit.setFont(font);
		btnQuit.setForeground(fcolor);
		btnQuit.setBackground(c1);
		btnQuit.setBounds(280, 15, 70, 25);
		btnQuit.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		btnOK.addActionListener(this);
		btnQuit.addActionListener(this);
		buttonPane.add(btnOK);
		buttonPane.add(btnQuit);
		if (!send) {
			txtMsg.setEditable(false);
			this.setTitle("你("+to+") 收到来自 " + from+" 的消息");
			this.txtMsg.setText(this.msg);
		} else {
			this.setTitle("你("+from+") 正在与 " + to+"聊天");
		}
		
		this.setResizable(false);
		this.setSize(410, 300);
		setCenter();
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
		Conversation c = new Conversation(null, null, null, null);
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if (btn.equals(btnQuit)) {
			this.dispose();
		} else if (btn.equals(btnOK)) {
			String txt = btn.getText();
			if (txt.equals("发送")) {
				String msg = this.txtMsg.getText();
				if (msg.trim() == "") {
					JOptionPane.showMessageDialog(this, "不能发送空消息");
					return;
				} else {
					try {
						if (send) {
							this.server.sendMessage(from, to, msg);
						} else {
							server.sendMessage(to, from, msg);
						}
						this.dispose();

					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(this, "服务器暂时不可用");
						this.dispose();
					}
				}
			} else if (txt.equals("回复")) {
				this.txtMsg.setEditable(true);
				this.btnOK.setText("发送");
				this.setTitle("你("+to+") 正在和" + from+"对话");
				this.txtMsg.setText("");
			}
		}
	}

}
