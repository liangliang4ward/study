import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;


public class Test1 extends JFrame{

	public Test1(){
		LC layC = new LC().fill().wrap();
		AC colC = new AC().align("right", 1).fill(2, 4).grow(100, 2, 4).align("right", 3).gap("15", 2);
		AC rowC = new AC().align("top", 7).gap("15!", 6).grow(100, 8);

		JPanel panel = new JPanel(new MigLayout(layC, colC, rowC));    // Makes the background gradient

		// References to text fields not stored to reduce code clutter.

		JScrollPane list2 = new JScrollPane(new JList(new String[] {"Mouse, Mickey"}));
		panel.add(list2,                     new CC().spanY().growY().minWidth("150").gapX(null, "10"));

		panel.add(new JLabel("Last Name"));
		panel.add(new JTextField());
		panel.add(new JLabel("First Name"));
		panel.add(new JTextField(),          new CC().wrap().alignX("right"));
		panel.add(new JLabel("Phone"));
		panel.add(new JTextField());
		panel.add(new JLabel("Email"));
		panel.add(new JTextField());
		panel.add(new JLabel("Address 1"));
		panel.add(new JTextField(),          new CC().spanX().growX());
		panel.add(new JLabel("Address 2"));
		panel.add(new JTextField(),          new CC().spanX().growX());
		panel.add(new JLabel("City"));
		panel.add(new JTextField(),          new CC().wrap());
		panel.add(new JLabel("State"));
		panel.add(new JTextField());
		panel.add(new JLabel("Postal Code"));
		panel.add(new JTextField(10),        new CC().spanX(2).growX(0));
		panel.add(new JLabel("Country"));
		panel.add(new JTextField(),          new CC().wrap());

		panel.add(new JButton("New"),        new CC().spanX(5).split(5).tag("other"));
		panel.add(new JButton("Delete"),     new CC().tag("other"));
		panel.add(new JButton("Edit"),       new CC().tag("other"));
		panel.add(new JButton("Save"),       new CC().tag("other"));
		panel.add(new JButton("Cancel"),     new CC().tag("cancel"));
		this.add(panel);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		Test1 t = new Test1();
		
	}
}
