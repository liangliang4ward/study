package time;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class timeClientFrame extends JFrame {
  JPanel contentPane;
  Label label1 = new Label();
  Button button1 = new Button();
  int port;                              //声明组播使用的端口
  InetAddress group;                      //声明建立组播组使用的组播组地址
  MulticastSocket socket;                 //声明建立组播组使用的MulticastSocket类
  DatagramPacket packet;                  //声明发送和接收数据所使用的DatagramPacket类

  public timeClientFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {
    contentPane = (JPanel) this.getContentPane();            //设计窗体布局
    label1.setBounds(new Rectangle(70, 63, 266, 39));
    contentPane.setLayout(null);
    this.setSize(new Dimension(378, 235));
    this.setTitle("Time Client");
    button1.setLabel("接收时间");
    button1.setBounds(new Rectangle(132, 144, 126, 38));
    button1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button1_actionPerformed(e);
      }
    });
    contentPane.add(label1, null);
    contentPane.add(button1, null);
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void button1_actionPerformed(ActionEvent e) {           //用户点击“接收时间”按钮时产生的动作
    try{
      port = 5000;                                        //设置组播组的监听端口为5000
      group = InetAddress.getByName("239.255.0.0");       //设置组播组的地址为239.0.0.0
      socket = new MulticastSocket(port);                //初始化MulticastSocket类并将端口号与之关联
      socket.joinGroup(group);                            //加入组播组
      byte[] data = new byte[50];
      packet = new DatagramPacket(data,data.length,group,port);  //创建一个DatagramPacket实例
      socket.receive(packet);                             //接收组播组中时间服务器传来的时间
      String message = new String(packet.getData(),0,packet.getLength());
      label1.setText("现在时间： " + message);              //在窗体中显示时间
    }
    catch(Exception e1)
    {
      System.out.println("Error: " + e1);                 //捕捉异常情况
    }
  }
}