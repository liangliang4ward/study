package time;

import java.io.*;
import java.net.*;
import java.util.Date;

class timeServer {

  public timeServer()
  {
    int port;
    InetAddress group;
    MulticastSocket socket;
    try {
      port = 5000;                                   //设置组播组的监听端口为5000
      group = InetAddress.getByName("239.255.0.0");  //设置组播组的地址为239.0.0.0
      socket = new MulticastSocket(port);           //初始化MulticastSocket类并将端口号与之关联
      socket.setSoTimeout(1000);                     //设置组播数据报的发送范围为本地网络
      socket.setTimeToLive(1);                       //设置套接字的接收数据报的最长时间
      socket.joinGroup(group);                       //加入组播组
      String outMessage;
      byte[] data;
      DatagramPacket packet;
      while(true)
      {
         Date time = new Date();                     //创建一个Date对象的实例，用来取得当前时间
         int curyear = time.getYear() + 1900;       //得到当前年份
         int curmonth = time.getMonth() + 1;        //得到当前月份
         int curday = time.getDate();               //得到当前日期
         int curhour = time.getHours();             //得到当前小时
         int curminute = time.getMinutes();         //得到当前分钟
         int cursecond = time.getSeconds();         //得到当前秒数
         outMessage = "公元" + String.valueOf(curyear) + "年" + String.valueOf(curmonth) + "月" + String.valueOf(curday) + "日" + String.valueOf(curhour) + ":" + String.valueOf(curminute) + ":" + String.valueOf(cursecond);
         data = outMessage.getBytes();
         packet =  new DatagramPacket(data,data.length,group,port);  //创建一个DatagramPacket实例
         socket.send(packet);                                        //向组播组发送当前时间
         System.out.println("Message sent: " + outMessage);
      }
    }
    catch(Exception e)
    {
      System.out.println("Error: " + e);               //捕捉异常情况
    }

  }

  public static void main(String[] args) {
      timeServer svr = new timeServer();
  }
}