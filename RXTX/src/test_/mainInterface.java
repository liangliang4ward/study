package test_;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import test_.serial.GetTime;
import test_.serial.ReturnEqual;
import test_.serial.WriteBean;
import test_.serial.getInfoToString;



public class mainInterface extends JFrame  implements ActionListener {
	static byte[] searchAll = new byte[] { 0x02,0x00 ,0x00 ,0x04 ,0x46 ,0x52 ,
		(byte)0x9C ,0x03 };// Œ¤ËùÓÐµÄ¿¨
	
	static byte[] antiCollision = new byte[]{0x02 ,0x00 ,0x00 ,0x04 ,0x47 ,0x04, 
		0x4F ,0x03};//·À³åÍ»
	
	static byte[] getDevNum = new byte[] { 0x02, 0x00, 0x00, 0x10, 0x03, 0x14,
		0x17, 0x03 };// µÃµ½¶Á¿¨Æ÷±àºÅ
	
	static byte[] choseCard = new byte[]{0x02,0x00 ,0x00 ,0x07 ,0x48 ,(byte)0x82,
		(byte)0xD4,(byte)0xD7 ,0x11 ,(byte)0x8D ,0x03};//Ñ¡¿¨£¬¿¨ºÅÎª£º82 D4 D7 11
	
	static byte[] checkCode = new byte[]{0x02 ,0x00 ,0x00 ,0x0B ,0x4A ,0x60, 0x00 ,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xAF,0x03};//ÑéÖ¤AÃÜÔ¿
	
	static byte[] readSector = new byte[]{0x02 ,0x00 ,0x00 ,0x04 ,0x4B ,0x00, 
		0x4F ,0x03};//¶Á¾ø¶Ô¿é0
	
	JLabel L1=new JLabel("´®¿ÚºÅ");
	JLabel L2=new JLabel("²¨ÌØÂÊ");
	JLabel L3=new JLabel("¿¨ºÅ");
	JLabel L4=new JLabel("ÃÜÂë");
	JLabel L5=new JLabel("Êý¾Ý");
	JLabel L6=new JLabel("Ë¢¿¨Ê±¼ä");
	JLabel L7=new JLabel("Ë¢¿¨Æ÷±àºÅ");
	JComboBox combo1=new JComboBox();
	JComboBox combo2=new JComboBox();
	JTextField cardNum = new JTextField(12);
	JPasswordField code = new JPasswordField(12);
	JTextField data = new JTextField(20);
	JTextField receiveDate = new JTextField(12);
	JTextField devNum = new JTextField(12);
	JRadioButton codeA=new JRadioButton("A");
	JRadioButton codeB=new JRadioButton("B");
	ButtonGroup AB=new ButtonGroup();
	JButton searchCard=new JButton("Ñ°¿¨");
	JButton readCard=new JButton("¶Á¿¨");
	
	String SelectedPort;
	
	public mainInterface() {
		setBounds(300, 200, 600, 300);
	//	setLayout(new FlowLayout());
		setLayout(new GridLayout(5,4,20,10));
		
		add(L1);
		add(combo1);
		combo1.addItem("COM3");
		combo1.addItem("COM8");
		//SelectedPort=(String)combo1.getSelectedItem();
		/*add(new JLabel("          "));*/
		add(L2);
		add(combo2);
		combo2.addItem("19200");
		
		add(L3);
		add(cardNum);
		
		add(L6);
		add(receiveDate);
		add(new JLabel("          "));
		add(new JLabel("          "));
		add(L7);
		add(devNum);
		add(L4);
		add(code);
		
		add(codeA);
		codeA.setSelected(true);
		add(codeB);
		AB.add(codeA);
		AB.add(codeB);
		
		add(L5);
		add(data);
		add(searchCard);
		add(readCard);

		searchCard.addActionListener(this);
		readCard.addActionListener(this);
		// b2.action(, what)
		setResizable(true);
		setVisible(true);
		validate();
	}
	//Ñ°¿¨£¬µÃµ½¿¨ºÅ
	public void SearchCard(){
		SelectedPort=(String)combo1.getSelectedItem();
		ReturnEqual a =new ReturnEqual();
		new WriteBean(searchAll);
		byte[] c= new getInfoToString().WriteBean(antiCollision,SelectedPort);
		String[] d=a.ReadEqual(c);
		String t=d[6].concat(d[7]).concat(d[8]).concat(d[9]);
		cardNum.setText(t.toUpperCase());
		
	}
	//µÃµ½¶Á¿¨Æ÷±àºÅ
	public void GetDevNum(){
		SelectedPort=(String)combo1.getSelectedItem();
		ReturnEqual a =new ReturnEqual();
		byte[] f= new getInfoToString().WriteBean(getDevNum,SelectedPort);
		String[] dd=a.ReadEqual(f);
		int b=dd.length;
		for(int i=0;i<b;i++){
			if(dd[i]==null){
				b=0;
			}else {
				if(dd[i].length()==1){
					dd[i]="0".concat(dd[i]);
				}
			}
			
		}
		String tt=dd[6].concat(dd[7]);
		devNum.setText(tt);
		
	}
//	¶Á¿¨£¬µÃµ½¿¨ÄÚÈÝ
	public void ReadSector(){
		SelectedPort=(String)combo1.getSelectedItem();
			if(code.getText().equals("FFFFFFFFFFFF")||code.getText().equals("ffffffffffff")){
			ReturnEqual a =new ReturnEqual();
			new WriteBean(searchAll);
			new WriteBean(antiCollision);
			new WriteBean(choseCard);
			new WriteBean(checkCode);
			byte[] g= new getInfoToString().WriteBean(readSector,SelectedPort);
			String[] h=a.ReadEqual(g);
			
			String b="";
			for(int j=0;j<16;j++){
				b=b.concat(h[j+6]);
			}
			
			code.setText("");
			data.setText(b);}
		else{
			int res = JOptionPane.showConfirmDialog(this, "ÇëÊäÈëÕýÈ·µÄÃÜÂë", "´íÎóÐÅÏ¢",
					JOptionPane.OK_CANCEL_OPTION);
			if(res==JOptionPane.OK_OPTION){
				code.setText("");
				code.requestFocus();
			}
		}
	
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==searchCard){
			String SerialPort=(String)combo1.getSelectedItem();
			//String BaudRate=(String)combo2.getSelectedItem();
			GetDevNum();
			SearchCard();
			receiveDate.setText(new GetTime().GetTime());
				}
		else if(e.getSource()==readCard){
			if(cardNum.getText().equals(""))
			{
				int res = JOptionPane.showConfirmDialog(this, "ÇëÑ°¿¨£¡", "´íÎóÐÅÏ¢",
						JOptionPane.OK_CANCEL_OPTION);
			}
			else{
			ReadSector();
			}
		}
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new mainInterface();

	}

}
