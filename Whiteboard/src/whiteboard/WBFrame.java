package whiteboard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import java.net.*;

public class WBFrame extends JFrame
{
  JLabel jLabel1;
  JPanel contentPane;
  JScrollPane jScrollPane1;
  JTextArea jTextArea1;
  JTextField jTextField1;
  JButton jButton1;
  JTextField jTextField2;
  JButton jButton2;
  JButton jButton3;
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  int port;                                       //声明组播使用的端口
  MulticastSocket socket;                          //声明建立组播组使用的MulticastSocket类
  InetAddress group;                               //声明建立组播组使用的组播组地址
  DatagramPacket packet;                           //声明发送和接收数据所使用的DatagramPacket类
  String username;                                 //声明用户名
  Canvas canvas1;                                  //声明画布
  boolean isMember;                               //声明判断是否已经加入组播组的变量
  Color color = new Color(255,0,0);                //声明画布使用的颜色为红色
  int startx;                                      //声明画图的起点横坐标
  int starty;                                      //声明画图的起点纵坐标
  int endx;                                        //声明画图的终点横坐标
  int endy;                                        //声明画图的终点纵坐标

  public WBFrame()
  {
    jScrollPane1 = new JScrollPane();
    jTextArea1 = new JTextArea();
    jTextField1 = new JTextField();
    jButton1 = new JButton();
    jTextField2 = new JTextField();
    jButton2 = new JButton();
    jLabel1 = new JLabel();
    canvas1 = new Canvas();
    jButton3 = new JButton();
    isMember = false;
    enableEvents(64L);
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    contentPane = (JPanel)getContentPane();              //设计布局
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    contentPane.setLayout(null);
    setResizable(false);
    setSize(new Dimension(620, 372));
    setTitle("白板程序");
    jScrollPane1.setBounds(new Rectangle(8, 5, 335, 211));
    jTextField1.setEnabled(false);
    jTextField1.setBounds(new Rectangle(8, 234, 248, 31));
    jButton1.setBounds(new Rectangle(267, 233, 75, 31));
    jButton1.setEnabled(false);
    jButton1.setBorder(titledBorder1);
    jButton1.setText("发送");
    jButton3.setEnabled(false);
    canvas1.setEnabled(false);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    jTextArea1.setEnabled(false);
    jTextField2.setBounds(new Rectangle(98, 289, 158, 31));
    jButton2.setBounds(new Rectangle(265, 288, 79, 35));
    jButton2.setBorder(titledBorder2);
    jButton2.setText("加入");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton2_actionPerformed(e);
      }
    });
    jLabel1.setText("用户名:");
    jLabel1.setBounds(new Rectangle(9, 289, 81, 28));
    contentPane.setBackground(new Color(184, 184, 217));
    canvas1.setBackground(Color.white);
    canvas1.setBounds(new Rectangle(358, 5, 251, 269));
    canvas1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        canvas1_mousePressed(e);
      }
      public void mouseReleased(MouseEvent e) {
        canvas1_mouseReleased(e);
      }
    });
    jButton3.setBounds(new Rectangle(440, 290, 91, 32));
    jButton3.setBorder(titledBorder3);
    jButton3.setText("清除");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton3_actionPerformed(e);
      }
    });
    contentPane.add(jScrollPane1, null);
    contentPane.add(jTextField1, null);
    contentPane.add(jButton1, null);
    contentPane.add(jTextField2, null);
    contentPane.add(jButton2, null);
    contentPane.add(jLabel1, null);
    contentPane.add(canvas1, null);
    contentPane.add(jButton3, null);
    jScrollPane1.getViewport().add(jTextArea1, null);
  }

  protected void processWindowEvent(WindowEvent e)
  {
    super.processWindowEvent(e);
    if(e.getID() == 201)
       System.exit(0);
  }

  void jButton2_actionPerformed(ActionEvent e)           //当用户点击“加入”（或“离开”）按钮时产生的动作
  {
    if (!isMember)                                       //如果用户不是组播组成员则加入组播组
    {
      try
      {
        port = 5000;                                      //设置端口号
        group = InetAddress.getByName("239.0.0.0");       //设置组播组地址
        socket = new MulticastSocket(port);              //初始化MulticastSocket实例
        socket.setTimeToLive(1);                          //设置组播数据报的发送范围为本地网络
        socket.setSoTimeout(10000);                       //设置套接字的接收数据报的最长时间
        socket.joinGroup(group);                          //加入此组播组
        username = jTextField2.getText();                 //得到用户所使用的用户名
        String tmp = String.valueOf(String.valueOf(username)).concat(" has joined the group");
        byte[] data = tmp.getBytes();
        packet = new DatagramPacket(data, data.length, group, port);  //初始化DatagramPacket实例
        socket.send(packet);                              //向组播组发送次用户已加入组播组的消息
        jButton1.setEnabled(true);                        //界面功能的可用或不可用的设置
        jTextField1.setEnabled(true);
        jTextArea1.setEnabled(true);
        jTextArea1.setEditable(false);
        jButton3.setEnabled(true);
        canvas1.setEnabled(true);
        jTextField2.setText("");
        jButton2.setText("离开");
        isMember = true;                                  //此用户已成为此组播组的成员
      }
      catch(Exception e1)
      {
        System.out.println("Error: " + e1);               //捕捉异常情况
      }
    }
    else
    {
      try
      {
        String tmp = String.valueOf(String.valueOf(username)).concat(" has left the group");
        byte[] data = tmp.getBytes();
        packet = new DatagramPacket(data, data.length, group, port);
        socket.send(packet);                               //向组播组发出此用户离开的消息
        socket.leaveGroup(group);                          //离开此组播组
        jButton1.setEnabled(false);                        //界面功能的可用或不可用的设置
        jTextField1.setEnabled(false);
        jTextArea1.setEnabled(false);
        jButton3.setEnabled(false);
        canvas1.setEnabled(false);
        jTextField2.setText("");
        jButton2.setText("加入");
        isMember = false;                                  //此用户已不是组播组的成员
      }
     catch(Exception e1)
     {
       System.out.println("Error: " + e1);                  //捕捉异常情况
     }
   }
 }

  void jButton1_actionPerformed(ActionEvent e)              //用户点击“发送”按钮时产生的动作
  {
    try
    {
      String tmp = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(username)))).append(" said: ").append(jTextField1.getText())));
      byte[] data = tmp.getBytes();
      packet = new DatagramPacket(data, data.length, group, port);
      socket.send(packet);                                  //得到用户所要发送的信息并向组播组发送
      jTextField1.setText("");
    }
    catch(Exception e1)
    {
      System.out.println("Error: " + e1);                   //捕捉异常情况
    }
  }

  void canvas1_mousePressed(MouseEvent e)                  //当用户在画板上落下鼠标时产生的动作
  {
    startx = e.getX();                                      //落下点的横坐标作为数据传送的起点横坐标
    starty = e.getY();                                      //落下点的纵坐标作为数据传送的起点纵坐标
  }

  void canvas1_mouseReleased(MouseEvent e)                 //当用户在画板上抬起鼠标时产生的动作
  {
    endx = e.getX();                                        //抬起点的横坐标作为数据传送的终点横坐标
    endy = e.getY();                                        //抬起点的纵坐标作为数据传送的起点纵坐标
    Graphics g = canvas1.getGraphics();                     //在本机画布上画线
    g.setColor(color);
    g.drawLine(startx,starty,endx,endy);
    //以下是向组播组传送此用户的画图信息，信息包括用户所画线的起点和终点的横坐标和纵坐标，信息以“@”符号起头，以表明此信息是画图信息，每个坐标信息使用“@”符号分割
    String tmp = "@" + String.valueOf(startx) + "@" + String.valueOf(starty) + "@" + String.valueOf(endx) + "@" + String.valueOf(endy) + "@";
    try
    {
      byte[] data = tmp.getBytes();
      packet = new DatagramPacket(data, data.length, group, port);
      socket.send(packet);
    }
    catch(Exception e1)
    {
      System.out.println("Error: " + e1);                   //捕捉异常情况
    }
  }

  void jButton3_actionPerformed(ActionEvent e)              //用户单击“清除”按钮时产生的动作
  {
    canvas1.setBackground(Color.black);
    canvas1.setBackground(Color.white);
  }

  public boolean waitforpackets()                         //等待组播组发送的消息并在本机上显示出来的函数
  {
    byte[] packetdata = new byte[512];                    //初始化接收数据的DatagramPacket使用的数组
    try
    {
      packet.setData(packetdata);                           //设定接收数据的DatagramPacket实例的数组大小
      packet.setLength(512);                                //设定接收数据的DatagramPacket实例的长度
      socket.receive(packet);                               //从组播组接收数据
    }
    catch (Exception e1){
      return true;                                        //如果没有新消息则返回
    }
    packetdata = packet.getData();
    if (packetdata[0]!='@')                                 //判断是文字信息或是画图信息
    {
      jTextArea1.append(String.valueOf(String.valueOf(new String(packetdata))).concat("\n"));  //如果是文字信息则在文字信息栏中显示
    }
    else                                           //如果此信息是画图信息的话
    {
      try                                          //以下程序段实现将画图起点和终点的坐标信息提取出来的功能
      {
        byte[] data;
        int i = 1;
        int size = 0;
        int startpos = i;
        while(packetdata[i]!='@')
        {
          i++;
          size++;
        }
        i = startpos;
        int j = 0;
        data = new byte[size];
        while(packetdata[i]!='@')
        {
          data[j] = packetdata[i];
          i++;
          j++;
        }
        startx = Integer.parseInt(new String(data));
        i++;
        startpos = i;
        size = 0;
        while(packetdata[i]!='@')
        {
          i++;
          size++;
        }
        i = startpos;
        j = 0;
        data = new byte[size];
        while(packetdata[i]!='@')
        {
          data[j] = packetdata[i];
          i++;
          j++;
        }
        starty = Integer.parseInt(new String(data));
        i++;
        startpos = i;
        size = 0;
        while(packetdata[i]!='@')
        {
          i++;
          size++;
        }
        i = startpos;
        j = 0;
        data = new byte[size];
        while(packetdata[i]!='@')
        {
          data[j] = packetdata[i];
          i++;
          j++;
        }
        while(packetdata[i]!='@')
        {
          data[j] = packetdata[i];
          i++;
          j++;
        }
        endx = Integer.parseInt(new String(data));
        i++;
        startpos = i;
        size = 0;
        while(packetdata[i]!='@')
        {
          i++;
          size++;
        }
        i = startpos;
        j = 0;
        data = new byte[size];
        while(packetdata[i]!='@')
        {
          data[j] = packetdata[i];
          i++;
          j++;
        }
        while(packetdata[i]!='@')
        {
          data[j] = packetdata[i];
          i++;
          j++;
        }
        endy = Integer.parseInt(new String(data));
        Graphics g = canvas1.getGraphics();                    //在本机上使用网络传来的画图信息画图
        g.setColor(color);
        g.drawLine(startx,starty,endx,endy);
      }
      catch(Exception e1)
      {
        System.out.println("Error" + e1);                      //捕捉异常情况
      }
    }
    return true;                                            //返回
  }
}
