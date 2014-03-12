package test_;
import test_.serial.toHex;

public class stb {
	public static void main(String args[]){
		String s = "1";
		
		//int sh = Integer.parseInt(s, 16); 
		

		byte [] bytes =s.getBytes(); 

		/*byte a = bytes [0] ; 
		byte b = bytes [1] ;*/ 
		toHex a=new toHex();
		a.WriteToHex(bytes);

	}

}
