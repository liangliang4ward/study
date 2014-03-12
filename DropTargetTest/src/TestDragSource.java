import java.awt.Cursor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class TestDragSource
{
	JFrame jf = new JFrame("Swing的拖放支持");
	JLabel srcLabel = new JLabel("AWT的拖放支持./n"
		+"将该文本域的内容拖入其他程序./n");
	public void init()
	{
		DragSource dragSource = DragSource.getDefaultDragSource();
		//将srcLabel转换成拖放源，它能接受复制、移动两种操作
		dragSource.createDefaultDragGestureRecognizer(srcLabel,
			DnDConstants.ACTION_COPY_OR_MOVE, new DragGestureListener()
			{
				public void dragGestureRecognized(DragGestureEvent event)
				{  
					//将JLabel里的文本信息包装成Transferable对象
					String txt = srcLabel.getText();
					Transferable transferable = new StringSelection(txt);
					//继续拖放操作,拖放过程中使用手状光标
					event.startDrag(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR), 
						transferable);
				}
			});
		jf.add(new JScrollPane(srcLabel));
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
	public static void main(String[] args) 
	{
		new TestDragSource().init();
	}
}
