import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.miginfocom.swing.MigLayout;


public class Test2 extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1914154991551691383L;
	private JPanel contentPanel = new JPanel(new MigLayout("wrap", "[]unrel[grow]", "[grow][pref]"));

	
	public Test2(){
		
		//左边一个树
		MyTree t = new MyTree();
		
		JScrollPane scroll = new JScrollPane(t);
		contentPanel.add(scroll,"spany,grow");

		//右边一个panel
		this.setContentPane(contentPanel);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				new Test2();
			}
		});
	}
	
	
	private class MyTree extends JTree{
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2921959263179986603L;

		public MyTree(){
			
		    DefaultMutableTreeNode root = new DefaultMutableTreeNode("中国");   
		    DefaultMutableTreeNode guangdong = new DefaultMutableTreeNode("广东");  
		    DefaultMutableTreeNode guangxi = new DefaultMutableTreeNode("广西");  
		    DefaultMutableTreeNode foshan = new DefaultMutableTreeNode("佛山");  
		    DefaultMutableTreeNode shantou = new DefaultMutableTreeNode("汕头");  
		    DefaultMutableTreeNode guilin = new DefaultMutableTreeNode("桂林");  
		    DefaultMutableTreeNode nanning = new DefaultMutableTreeNode("南宁");  
		    //通过add方法建立树节点之间的父子关系  
	        guangdong.add(foshan);  
	        guangdong.add(shantou);  
	        guangxi.add(guilin);  
	        guangxi.add(nanning);  
	        root.add(guangdong); 
	        root.add(guangxi);  
	        this.setModel(new DefaultTreeModel(root, false));
	        //以根节点创建树  
	        //默认连线  
	        //tree.putClientProperty("JTree.lineStyle" , "Angeled");  
	        //没有连线  
	        this.putClientProperty("JTree.lineStyle" , "None");  
	        //水平分隔线  
	        //tree.putClientProperty("JTree.lineStyle" , "Horizontal");   
	 
	        //设置是否显示根节点的“展开/折叠”图标,默认是false  
	        this.setShowsRootHandles(true);  
	        //设置节点是否可见,默认是true  
	        this.setRootVisible(true);  
			
		}
		
		
		
	}
	
}
