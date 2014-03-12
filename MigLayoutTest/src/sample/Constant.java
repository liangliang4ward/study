package sample;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class Constant {
	public static  JLabel createLabel(String text)
	{
		return createLabel(text, SwingConstants.LEADING);
	}

	public static  JLabel createLabel(String text, int align)
	{
		final JLabel b = new JLabel(text, align);
		return b;
	}

	public static  JTextField createTextField(int cols)
	{
		return createTextField("", cols,true);
	}
	

	public static  JTextField createTextField(String text)
	{
		return createTextField(text, 0,true);
	}

	public static  JTextField createTextField(String text, int cols,boolean canEdit)
	{
		final JTextField b = new JTextField(text, cols);
		b.setEditable(canEdit);
		return b;
	}
	
	public static  JScrollPane createTextAreaScroll(String text, int rows, int cols, boolean hasVerScroll)
	{
		JTextArea ta = new JTextArea(text, rows, cols);
		ta.setFont(UIManager.getFont("TextField.font"));
		ta.setWrapStyleWord(true);
		ta.setLineWrap(true);

		JScrollPane scroll = new JScrollPane(
			    ta,
			    hasVerScroll ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
			    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		return scroll;
	}
	
	public static  JButton createButton(String text, boolean bold)
	{
		JButton b = new JButton(text);
		
		if (bold)
			b.setFont(b.getFont().deriveFont(Font.BOLD));

		b.setContentAreaFilled(true);

		return b;
	}
}
