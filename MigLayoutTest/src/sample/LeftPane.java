package sample;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

import net.miginfocom.swing.MigLayout;

public class LeftPane extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7244004492533108610L;

	
	
	public LeftPane(){
		
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.LEFT);
		
		
		createLeftPanel();
		createPane2();
		tabPane.addTab("<html>设<br>备<br></html>", leftTreePanel);
		tabPane.addTab("<html>查<br>询<br></html>", pane2);
		this.setContentPane(tabPane);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();

	}
	
	public static void main(String[] args) {
		new LeftPane();
	}
	private JPanel pane2;
	private void createPane2() {
		pane2 = new JPanel(new MigLayout()) ;
		pane2.add(Constant.createLabel("逻辑地址"));
	}
	private JTree leftTree;
	private JScrollPane leftTreePanel;
	private void createLeftPanel() {
		leftTree=createLeftTree();
		leftTreePanel  = new JScrollPane(leftTree);
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
}
