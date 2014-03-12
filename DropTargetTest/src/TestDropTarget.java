import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class TestDropTarget
{
	final int DESKTOP_WIDTH = 480;
	final int DESKTOP_HEIGHT = 360;
	final int FRAME_DISTANCE = 30;
	JFrame jf = new JFrame("�����Ϸ�Ŀ�ꡪ����ͼƬ�ļ�����ô���");
	//����һ����������
	private JDesktopPane desktop = new JDesktopPane();
	//������һ���ڲ����ڵ������
	private int nextFrameX;
	private int nextFrameY;
	//�����ڲ�����Ϊ���������1/2��С
	private int width = DESKTOP_WIDTH / 2;
	private int height = DESKTOP_HEIGHT / 2;

	public void init()
	{
		desktop.setPreferredSize(new Dimension(DESKTOP_WIDTH, DESKTOP_HEIGHT));
		//����ǰ���ڴ������Ϸ�Դ
		new DropTarget(jf, DnDConstants.ACTION_COPY ,  new ImageDropTargetListener());
		jf.add(desktop);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}

	class ImageDropTargetListener extends DropTargetAdapter
	{
		public void drop(DropTargetDropEvent event)
		{
			//���ܸ��Ʋ���
			event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			//��ȡ�Ϸŵ�����
			Transferable transferable = event.getTransferable();
			DataFlavor[] flavors = transferable.getTransferDataFlavors();
			//�����Ϸ���������������ݸ�ʽ
			for (int i = 0; i < flavors.length; i++)
			{  
				DataFlavor d = flavors[i];
				try
				{
					//����Ϸ����ݵ����ݸ�ʽ���ļ��б�
					if (d.equals(DataFlavor.javaFileListFlavor))
					{
						//ȡ���ϷŲ�������ļ��б�
						java.util.List fileList 
							= (java.util.List) transferable.getTransferData(d);
						for (Object f : fileList)
						{
							//��ʾÿ���ļ�
							showImage((File)f , event);
						}
					}
				}
				catch (Exception e)
				{  
					e.printStackTrace();
				}
				//ǿ���ϷŲ���������ֹͣ�����Ϸ�Դ
				event.dropComplete(true);
			}
		}
		//��ʾÿ���ļ��Ĺ��߷���
		private void showImage(File f , DropTargetDropEvent event)throws java.io.IOException
		{
			Image image = ImageIO.read(f);
			if (image == null)
			{
				//ǿ���ϷŲ���������ֹͣ�����Ϸ�Դ
				event.dropComplete(true);
				JOptionPane.showInternalMessageDialog(desktop , "ϵͳ��֧���������͵��ļ�");
				//�������أ������������
				return;
			}
			ImageIcon icon = new ImageIcon(image);
			//�����ڲ�������ʾ��ͼƬ
			JInternalFrame iframe = new JInternalFrame(f.getName()
				,true , true , true , true);
			JLabel imageLabel = new JLabel(icon);
			iframe.add(new JScrollPane(imageLabel));
			desktop.add(iframe);
			//�����ڲ����ڵ�ԭʼλ�ã��ڲ�����Ĭ�ϴ�С��0X0������0,0λ�ã�
			iframe.reshape(nextFrameX, nextFrameY, width, height);
			//ʹ�ô��ڿɼ���������ѡ����
			iframe.show();
			//������һ���ڲ����ڵ�λ��
			nextFrameX += FRAME_DISTANCE;
			nextFrameY += FRAME_DISTANCE;
			if (nextFrameX + width > desktop.getWidth()) nextFrameX = 0;
			if (nextFrameY + height > desktop.getHeight()) nextFrameY = 0;
		}
	}
	public static void main(String[] args)
	{
		new TestDropTarget().init();
	}
}