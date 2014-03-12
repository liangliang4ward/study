package join;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
public class MultiSvrFrame extends JFrame {
  private JPanel contentPane;                          //设计窗体布局
  private Button button1 = new Button();
  private Button button2 = new Button();
  private Label label1 = new Label();
  private TextField textField1 = new TextField();
  private TextField textField2 = new TextField();
  private Label label2 = new Label();
  private Label label3 = new Label();
  int port;                                       //声明组播使用的端口
  MulticastSocket socket;                          //声明建立组播组使用的MulticastSocket类
  MulticastSocket soc;                             //声明加入和离开组播组使用的MulticastSocket类
  InetAddress group;                               //声明建立组播组使用的组播组地址
  InetAddress addr;                                //声明加入和离开组播组用的组播组地址
  public MultiSvrFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception  {
    contentPane = (JPanel) this.getContentPane();
    button1.setLabel("加入组播组");
    button1.setBounds(new Rectangle(77, 231, 99, 37));
    button1.addActionListener(new java.awt.event.ActionListener() {
     public void actionPerformed(ActionEvent e) {
        button1_actionPerformed(e);
      }
    });
    contentPane.setLayout(null);
    this.setSize(new Dimension(400, 317));
    this.setTitle("组播成员");
    button2.setLabel("离开组播组");
    button2.setBounds(new Rectangle(220, 231, 99, 36));
    button2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button2_actionPerformed(e);
      }
    });
    label1.setBounds(new Rectangle(75, 53, 248, 33));
    textField1.setBounds(new Rectangle(118, 112, 178, 32));
    textField2.setBounds(new Rectangle(117, 158, 87, 33));
    label2.setText("组播组地址");
    label2.setBounds(new Rectangle(30, 112, 67, 29));
    label3.setText("端口号");
    label3.setBounds(new Rectangle(32, 159, 75, 30));
    contentPane.add(button1, null);
    contentPane.add(button2, null);
    contentPane.add(label2, null);
    contentPane.add(textField1, null);
    contentPane.add(textField2, null);
    contentPane.add(label3, null);
    contentPane.add(label1, null);
    CreateMulticastGroup();
  }
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }
  void button1_actionPerformed(ActionEvent e) {
    try
     {
       soc = new MulticastSocket(Integer.parseInt(textField2.getText()));         //初始化要加入组播组的MulticastSocket并将之与用户要求使用的端口号关联
       addr = InetAddress.getByName(textField1.getText());                        //设置用户要加入的组播组的地址
       soc.joinGroup(addr);                                                       //加入用户指定地址的组播组
       label1.setText("已加入地址为" + addr.toString() + "的组播组");                //显示加入成功信息
     }
    catch(Exception e1)
     {
       System.out.println("Error: " + e1);                                        //捕捉异常情况
     }
   }
  void button2_actionPerformed(ActionEvent e) {
    try
     {
       soc.leaveGroup(addr);                                                     //离开组播组
       label1.setText("已离开地址为" + addr.toString() + "的组播组");               //显示离开成功信息
     }
    catch(Exception e1)
     {
       System.out.println("Error: " + e1);                                       //捕捉异常情况
     }
   }
  void CreateMulticastGroup()
   {
    try
     {
       port = 5000;                                           //设置组播组的监听端口为5000
       group = InetAddress.getByName("239.0.0.0");            //设置组播组的地址为239.0.0.0
       socket = new MulticastSocket(port);                    //初始化MulticastSocket类并将端口号与之关联
     }
    catch(Exception e1)
     {
       System.out.println("Error " + e1);                      //捕捉异常情况
     }
   }
}
