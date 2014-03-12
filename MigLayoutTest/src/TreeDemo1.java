import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreeDemo1{
  public TreeDemo1(){
    JFrame f=new JFrame("TreeDemo1");
    Container contentPane=f.getContentPane();
    
    String[] s1={"公司文件","个人信件","私人文件"}; 
    String[] s2={"本机磁盘(C:)","本机磁盘(D:)","本机磁盘(E:)"};
    String[] s3={"奇摩站","职棒消息","网络书店"};
    
    Hashtable hashtable1=new Hashtable();
    Hashtable hashtable2=new Hashtable();
    hashtable1.put("我的公文包",s1);
    hashtable1.put("我的电脑",s2);
    hashtable1.put("收藏夹",hashtable2);
    hashtable2.put("网站列表",s3);
    
    Font font = new Font("Dialog", Font.PLAIN, 12);
    Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
   /**定义widnows界面**/
    while (keys.hasMoreElements()) {
         Object key = keys.nextElement();
         if (UIManager.get(key) instanceof Font) {
             UIManager.put(key, font);
         }
   } 
   try{
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");  
   }catch(Exception el){
      System.exit(0);  
   }
   /**定义widnows界面**/
    JTree tree=new JTree(hashtable1);
    JScrollPane scrollPane=new JScrollPane();
    scrollPane.setViewportView(tree);
  contentPane.add(scrollPane);
  f.pack();
  f.setVisible(true);
  f.addWindowListener(new WindowAdapter(){
  public void windowClosing(WindowEvent e){
     System.exit(0); 
  }
  });
  } 
  public static void main(String[] args){
  new TreeDemo1();
  }
}