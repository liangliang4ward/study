import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;


public class Test3 extends JFrame{
	
	
	public Test3(){
		this.setLayout(new MigLayout("debug,insets 2","[grow][]","[grow][][]"));
		
		JTextArea historyArea = new JTextArea();
		this.add(new JScrollPane(historyArea),"growx,growy");
		JTextArea rightPanel = new JTextArea();
		this.add(rightPanel,"wrap,w 140!,span 1 4,growy");
		JTextField inputArea = new JTextField();
		this.add(new JScrollPane(inputArea),"growx,h 80!,wrap");
		
		JButton closeButton = new JButton("È¡Ïû");
		JButton sendButton = new JButton("·¢ËÍ");
		this.add(closeButton,"split 2,h 24!,align left,wrap");
		this.add(sendButton,"h 80!,h 24!");
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}
	public static void main(String[] args) {
		new Test3();
	}
	
}
