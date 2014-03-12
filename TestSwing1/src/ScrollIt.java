import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ScrollIt extends JFrame {
	JScrollPane spContainer;
	JTextArea taedit;
	JButton bScroll;

	public ScrollIt() {
		spContainer = new JScrollPane();
		taedit = new JTextArea();
		spContainer.getViewport().add(taedit);
		bScroll = new JButton("Scroll   to   last   line");
		bScroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JScrollBar sbar = spContainer.getVerticalScrollBar();
				sbar.setMaximum(700);
				System.out.println(sbar.getMinimum());
				System.out.println(sbar.getMaximum());
				sbar.setValue(100);
			}
		});
		System.out.println(spContainer.getMaximumSize().getHeight());
		for (int i = 0; i < 40; i++)
			taedit.append("line" + i + "\n");
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(spContainer, BorderLayout.CENTER);
		this.getContentPane().add(bScroll, BorderLayout.NORTH);
		this.setSize(500, 500);
		this.setVisible(true);
	}

	public static void main(String args[]) {
		new ScrollIt();
	}
}