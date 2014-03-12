package sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

public class RightPanel extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4596994277212950197L;
	
	private JPanel contentPane = new JPanel(new MigLayout("wrap","","[grow 20]10[grow 60]10[grow 20]"));
	
	private JPanel top ;
	private JPanel middle;
	private JPanel bottom;
	
	public RightPanel(){
		
		//上
		top=createTopPanel();
		//中 
		middle=createMiddlePanel();
		//下
		bottom=createBottomPanel();

		contentPane.add(top);
		contentPane.add(middle);
		contentPane.add(bottom);
		
		this.add(contentPane);
		
		
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JPanel createTopPanel() {
		
		JPanel p = new JPanel(new MigLayout("","","[][][][]"));
		p.add(Constant.createLabel("终端逻辑地址:"));
		p.add(Constant.createTextField("",20,false),"wrap");
		p.add(Constant.createLabel("最近一次心跳:"));
		p.add(Constant.createLabel(""));
		p.add(Constant.createLabel("最近一次收到GPRS:"));
		p.add(Constant.createLabel(""));
		return p;
	}
	private JPanel createMiddlePanel() {
		
		JPanel p = new JPanel(new MigLayout());
		p.add(Constant.createLabel("报文信息:"));
		p.add(Constant.createTextAreaScroll("",20,40,true));
		return p;
	}
	private JPanel createBottomPanel() {
		
		JPanel p = new JPanel(new MigLayout("insets 0 120 0 0 "));
		JButton createButton = Constant.createButton("停止显示", false);
		p.add(createButton,"span");
		createButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("s");
			}
		});
		return p;
	}
	
	public static void main(String[] args) {


		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				new RightPanel();
			}
		});
	
	
	}



	
	
}
