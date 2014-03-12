import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TestSplitPane
{
	Book[] books = new Book[]{
		new Book("Struts2权威指南" , new ImageIcon("ico/struts2.jpg") , 
			"全面介绍Struts2的各方/n面知识"),
		new Book("轻量级J2EE企业应用实战" , new ImageIcon("ico/j2ee.jpg") ,
			"介绍Struts、Spring和/nHibernate整合开发的知识"),
		new Book("基于J2EE的Ajax宝典" , new ImageIcon("ico/ajax.jpg") , 
			"全面介绍J2EE平台上Ajax/n开发的各方面知识")
	};
	JFrame jf = new JFrame("测试JSplitPane");
	JList bookList = new JList(books);
	JLabel bookCover = new JLabel();
	JTextArea bookDesc = new JTextArea();

	public void init()
	{
		//为三个组件设置最佳大小
		bookList.setPreferredSize(new Dimension(150, 300));
		bookCover.setPreferredSize(new Dimension(300, 150));
		bookDesc.setPreferredSize(new Dimension(300, 150));
		//为下拉列表添加事件监听器
		bookList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent event)
			{  
				Book book = (Book)bookList.getSelectedValue();
				bookCover.setIcon(book.getIco());
				bookDesc.setText(book.getDesc());
			}
         });
		//创建一个垂直的分割面板，
		//将bookCover放在上面，将bookDesc放在下面 , 支持连续布局
		JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true , 
			bookCover, new JScrollPane(bookDesc));
		//打开“一触即展”的特性
		left.setOneTouchExpandable(true);
		//下面代码设置分割条的大小。
		//left.setDividerSize(50); 
		//设置该分割面板根据所包含组件的最佳大小来调整布局 
		left.resetToPreferredSizes();
		//创建一个水平的分割面板
		//将left组件放在左边，将bookList组件放在右边
		JSplitPane content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			left, bookList);
		jf.add(content);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
	public static void main(String[] args)
	{  
		new TestSplitPane().init();
	}
}
class Book
{ 
	private String name;
	private Icon ico;
	private String desc;

	public Book(){}

	public Book(String name , Icon ico , String desc)
	{
		this.name = name;
		this.ico = ico;
		this.desc = desc;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		 return this.name;
	}

	public void setIco(Icon ico)
	{
		this.ico = ico;
	}
	public Icon getIco()
	{
		 return this.ico;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public String getDesc()
	{
		 return this.desc;
	}
	public String toString()
	{
		return name;
	}
}