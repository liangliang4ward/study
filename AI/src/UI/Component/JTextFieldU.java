package UI.Component;

import javax.swing.JTextField;

public class JTextFieldU extends JTextField {
	private String uuid;
	
	public void setuuid(String uuid){
		
		
		this.uuid=uuid;
	}
	public String getUUid(){
		
		
		return this.uuid;
	}
	
	public JTextFieldU(String s){
		
		super(s);
	}
	
	
	
}
