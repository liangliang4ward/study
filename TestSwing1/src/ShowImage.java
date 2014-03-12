import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ShowImage extends JPanel implements ActionListener {
	private JButton open ;
	private String filePath;
	private BufferedImage image;
	private static ShowImage show;
	
	public static ShowImage singleton(){
		if(show==null) 
			show=new ShowImage();
		return show;
	}

	private ShowImage(){
		open =new JButton("open");
		setLayout(new BorderLayout());
		add(open,BorderLayout.SOUTH);
		open.addActionListener(this);
		
	}
	
	private void init(String str){
		FileInputStream input;
		try {
			input = new FileInputStream(str);
			image=ImageIO.read(input);
			repaint();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 private static void createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("JFileChooserDemo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        //Add content to the window.
	        frame.add(singleton());
	        frame.setSize(300,400);
	        frame.setTitle("QRCode");

	        //Display the window.
	        frame.setVisible(true);
	    }
	 
	 public static void main(String[] args){
		 SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                //Turn off metal's use of bold fonts
		        UIManager.put("swing.boldMetal", Boolean.FALSE);
		        createAndShowGUI();
	            }
	        });
	 }

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(image,26,26,this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==open){
			JFileChooser dialog=new JFileChooser();
			int result=dialog.showOpenDialog(null);
			if(result==JFileChooser.APPROVE_OPTION){
				File file=dialog.getSelectedFile();
				filePath=file.getPath();
				init(filePath);
			}
			
		}
		
	}

}
