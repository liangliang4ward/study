import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestJSpinner
{
	final int SPINNER_NUM = 6;
	JFrame mainWin = new JFrame("微调控制器示范");
	Box spinnerBox = new Box(BoxLayout.Y_AXIS);
	JSpinner[] spinners = new JSpinner[SPINNER_NUM];
	JLabel[] valLabels = new JLabel[SPINNER_NUM];
	JButton okBn = new JButton("确定");
	public void init() 
	{
		for (int i = 0 ; i < SPINNER_NUM ; i++ )
		{
			valLabels[i] = new JLabel();
		}
		//-----------普通Spinner-----------
		spinners[0] = new JSpinner();
		addSpinner(spinners[0], "普通" , valLabels[0]);

		//-----------指定最小值、最大值、步长的Spinner-----------
		//创建一个SpinnerNumberModel对象，指定最小值、最大值和步长
		SpinnerNumberModel numModel = new SpinnerNumberModel(3.4, 
			-1.1, 4.3, 0.1); 
		spinners[1] = new JSpinner(numModel);
		addSpinner(spinners[1], "数 值 范 围" , valLabels[1]);

		//-----------使用SpinnerListModel的Spinner------------
		String[] books = new String[]
		{
			"轻量级J2EE企业应用实战",
			"Struts2权威指南",
			"基于J2EE的Ajax宝典"
		};
		//使用字符串数组创建SpinnerListModel对象
		SpinnerListModel bookModel = new SpinnerListModel(books);
		//使用SpinnerListModel对象创建JSpinner对象
		spinners[2] = new JSpinner(bookModel);
		addSpinner(spinners[2], "字符串序列值" , valLabels[2]);

		//-----------使用序列值是ImageIcon的Spinner------------
		ArrayList<ImageIcon> icons = new ArrayList<ImageIcon>();
		icons.add(new ImageIcon("a.gif"));
		icons.add(new ImageIcon("b.gif"));
		//使用ImageIcon数组创建SpinnerListModel对象
		SpinnerListModel iconModel = new SpinnerListModel(icons);
		//使用SpinnerListModel对象创建JSpinner对象
		spinners[3] = new JSpinner(iconModel);
		addSpinner(spinners[3], "图标序列值" , valLabels[3]);

		//-----------使用SpinnerDateModel的Spinner------------
		//分别获取起始时间、结束时间、初时时间
		Calendar cal = Calendar.getInstance();
		Date init = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH , -3);
		Date start = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH , 8);
		Date end = cal.getTime();
		//创建一个SpinnerDateModel对象，指定最小时间、最大时间和初始时间
		SpinnerDateModel dateModel = new SpinnerDateModel(init , 
			start , end , Calendar.HOUR_OF_DAY);
		//以SpinnerDateModel对象创建JSpinner
		spinners[4] = new JSpinner(dateModel);
		addSpinner(spinners[4], "时 间 范 围" , valLabels[4]);

		//-----------使用DateEditor来格式化Spinner------------
		dateModel = new SpinnerDateModel();
		spinners[5] = new JSpinner(dateModel);
		//创建一个JSpinner.DateEditor对象，用于对指定Spinner进行格式化
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinners[5],
			"公元yyyy年MM月dd日 HH时");
		//设置使用JSpinner.DateEditor对象进行格式化
		spinners[5].setEditor(editor);
		addSpinner(spinners[5], "使用DateEditor" , valLabels[5]);
		//为“确定”按钮添加一个事件监听器
		okBn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				//取出每个微调控制器的值，并将该值用后面的Label标签显示出来。
				for (int i = 0 ; i < SPINNER_NUM ; i++)
				{
					valLabels[i].setText(spinners[i].getValue().toString());
				}
			}
		});

		JPanel bnPanel = new JPanel();
		bnPanel.add(okBn);
		mainWin.add(spinnerBox, BorderLayout.CENTER);
		mainWin.add(bnPanel, BorderLayout.SOUTH);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.pack();
		mainWin.setVisible(true);
	
	}
	//定义一个方法，用于将滑动条添加到容器中
	public void addSpinner(JSpinner spinner, String description
		,JLabel valLabel)
	{
		Box box = new Box(BoxLayout.X_AXIS);
		JLabel desc = new JLabel(description + "：");
		desc.setPreferredSize(new Dimension(100 , 30));
		box.add(desc);
		box.add(spinner);
		valLabel.setPreferredSize(new Dimension(180 , 30));
		box.add(valLabel);
		spinnerBox.add(box);
	}
	public static void main(String[] args)
	{
		String laf = UIManager.getSystemLookAndFeelClassName();
		final String laff = laf;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(laff);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				new TestJSpinner().init();
			}
		});
		
	}
}
