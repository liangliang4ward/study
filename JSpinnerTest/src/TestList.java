import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TestList
{
	private JFrame mainWin = new JFrame("测试列表框");
	String[] books = new String[]
	{
		"Spring2.0宝典",
		"轻量级J2EE企业应用实战",
		"基于J2EE的Ajax宝典",
		"Struts2权威指南",
		"ROR敏捷开发最佳实践"
	};
	JList bookList = new JList(books);
	JComboBox bookSelector;
	//定义布局选择按钮所在的面板
	JPanel layoutPanel = new JPanel();
	ButtonGroup layoutGroup = new ButtonGroup();
	//定义选择模式按钮所在的面板
	JPanel selectModePanel = new JPanel();
	ButtonGroup selectModeGroup = new ButtonGroup();
	JTextArea favoriate = new JTextArea(4 , 40);

	public void init()
	{
		//JList的可视高度可同时显示三个列表项
		bookList.setVisibleRowCount(3); 
		//默认选中第三项到第五项（第一项的索引是0）
		bookList.setSelectionInterval(2, 4);
		addLayoutButton("纵向滚动", JList.VERTICAL);
		addLayoutButton("纵向换行", JList.VERTICAL_WRAP);
		addLayoutButton("横向换行", JList.HORIZONTAL_WRAP);
		addSelectModelButton("无限制", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		addSelectModelButton("单选", ListSelectionModel.SINGLE_SELECTION);
		addSelectModelButton("单范围", ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		Box listBox = new Box(BoxLayout.Y_AXIS);
		//将JList组件放在JScrollPane中，再将该JScrollPane添加到listBox容器中
		listBox.add(new JScrollPane(bookList));
		//添加布局选择按钮面板、选择模式按钮面板
		listBox.add(layoutPanel);
		listBox.add(selectModePanel);
		//为JList添加事件监听器
		bookList.addListSelectionListener(new ListSelectionListener()
		{
			public void  valueChanged(ListSelectionEvent e)
			{
				//获取用户所选择的所有图书
				Object[] books = bookList.getSelectedValues();
				favoriate.setText("");
				for (Object book : books )
				{
					favoriate.append(book.toString() + "/n");
				}
			}
		});

		Vector<String> bookCollection = new Vector<String>();
		bookCollection.add("Spring2.0宝典");
		bookCollection.add("轻量级J2EE企业应用实战");
		bookCollection.add("基于J2EE的Ajax宝典");
		bookCollection.add("Struts2权威指南");
		bookCollection.add("ROR敏捷开发最佳实践");
		//用一个Vector对象来创建一个JComboBox对象
		bookSelector = new JComboBox(bookCollection);
		//为JComboBox添加事件监听器
		bookSelector.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) 
			{
				//获取JComboBox所选中的项
				Object book = bookSelector.getSelectedItem();
				favoriate.setText(book.toString());
			}
		});
		
		//设置可以直接编辑
		bookSelector.setEditable(true);
		//设置下拉列表框的可视高度可同时显示4个列表项
		bookSelector.setMaximumRowCount(4);


		JPanel p = new JPanel();
		p.add(bookSelector);
		Box box = new Box(BoxLayout.X_AXIS);
		box.add(listBox);
		box.add(p);
		mainWin.add(box);
		JPanel favoriatePanel = new JPanel();
		favoriatePanel.setLayout(new BorderLayout());
		favoriatePanel.add(new JScrollPane(favoriate));
		favoriatePanel.add(new JLabel("您喜欢的图书：") , BorderLayout.NORTH);
		mainWin.add(favoriatePanel , BorderLayout.SOUTH);

		
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.pack();
		mainWin.setVisible(true);
	}

	private void addLayoutButton(String label, final int orientation)
	{
		layoutPanel.setBorder(new TitledBorder(new EtchedBorder(), "确定选项布局"));
		JRadioButton button = new JRadioButton(label);
		//把该单选按钮添加到layoutPanel面板中
		layoutPanel.add(button);
		//默认选中第一个按钮
		if (layoutGroup.getButtonCount() == 0) 
			button.setSelected(true);
		layoutGroup.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				//改变列表框里列表项的布局方向
				bookList.setLayoutOrientation(orientation);
			}
		});
	}
	private void addSelectModelButton(String label, final int selectModel)
	{
		selectModePanel.setBorder(new TitledBorder(new EtchedBorder(), "确定选择模式"));
		JRadioButton button = new JRadioButton(label);
		//把该单选按钮添加到selectModePanel面板中
		selectModePanel.add(button);
		//默认选中第一个按钮
		if (selectModeGroup.getButtonCount() == 0) 
			button.setSelected(true);
		selectModeGroup.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				//改变列表框里的选择模式
				bookList.setSelectionMode(selectModel);
			}
		});
	}

	public static void main(String[] args) 
	{
		new TestList().init();
	}
}
