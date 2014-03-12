package sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -347003037896182059L;


	private JPanel contentPane = new JPanel(new MigLayout());
	
	private boolean isDisplay = true;
	
	public MainFrame(){
		
		init();

		this.setContentPane(contentPane);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {logout();}
		});
		
	}
	
	private void init(){
		
		//leftPanel
		createLeftPanel();
		contentPane.add(leftTabPane, new CC().spanY().growY().minWidth("150").maxWidth("200"));
		
		//rightPanel
		createRightPanel();
		contentPane.add(rightPanel);
		
	}

	private JPanel rightPanel;
	
	private JTextField tf_LogicAddr;
	private JTextField tf_LastHeartBeat;
	private JTextField tf_LastGprs;
	private JButton b_displaySwitch;
	
	private void createRightPanel() {
		
		rightPanel = new JPanel(new MigLayout("","grow"));
		
		rightPanel.add(Constant.createLabel("终端逻辑地址:"),"l");
		
		tf_LogicAddr=Constant.createTextField("",20,false);
		rightPanel.add(tf_LogicAddr,"wrap");
		
		rightPanel.add(Constant.createLabel("最近一次心跳时间:"),"l");
		
		tf_LastHeartBeat=Constant.createTextField("",20,false);
		rightPanel.add(tf_LastHeartBeat,"split");
		
		rightPanel.add(Constant.createLabel("最近一次GPRS时间:"),"l");
		
		tf_LastGprs=Constant.createTextField("",20,false);
		rightPanel.add(tf_LastGprs,"wrap");
		
		rightPanel.add(Constant.createLabel("报文信息:"),"l");
		rightPanel.add(Constant.createTextAreaScroll("", 20, 60, true),"wrap");
		
		
		b_displaySwitch=Constant.createButton("终止显示", false);
//		rightPanel.add(b_displaySwitch,"skip");
		
		b_displaySwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isDisplay=!isDisplay;
				String buttonText;
				buttonText=isDisplay?"终止显示":"开始显示";
				b_displaySwitch.setText(buttonText);
			}
		});
	}

	private JTree leftTree;
	private JScrollPane leftTreePane;
	
	private JTabbedPane leftTabPane;
	private JPanel leftQueryPane;
	
	
	
	private void createLeftPanel() {
		leftTabPane = new JTabbedPane(JTabbedPane.LEFT);
		createLeftTreePane();
		
		createLeftQueryPane();
		
		
		leftTabPane.addTab("<html>\n<br>设<br>\t<br>备<br>\n<br></html>", leftTreePane);
		leftTabPane.addTab("<html>\n<br>查<br>\t<br>寻<br>\n<br></html>", leftQueryPane);
	}

	private void createLeftQueryPane() {
		AC rowC = new AC().grow(1, 3);


		leftQueryPane = new JPanel(new MigLayout(null,null, rowC));
		
		//label  text
		leftQueryPane.add(Constant.createLabel("设备号:"),"split");
		leftQueryPane.add(Constant.createTextField(20),"wrap");
		//label
		leftQueryPane.add(Constant.createLabel("查询结果:"),"split,wrap");
		//list
		JScrollPane list2 = new JScrollPane(new JList());
		leftQueryPane.add(list2, new CC().spanY().growY().minWidth("150").maxWidth("200"));
	}

	private void createLeftTreePane() {
		leftTree=createLeftTree();
		leftTreePane  = new JScrollPane(leftTree);
	}
	
	
	

	private JTree createLeftTree() {
		JTree tree = new JTree();
		
		
		//tree model
		
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//处理鼠标双击事件
				
				//if leaf
				//not: do nothing
				//yes: if terminal
				//	 	yse: get info
				
			}
		});
		return tree;
	}

	
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				new MainFrame();
			}
		});
	}
	
	public void logout(){
		
		System.out.println("logout...");
		
	}
	
}
