package test_;
import test_.serial.FunCollection;

public class WriteRead {
	/*String PortName;

	CommPortIdentifier portId;

	SerialPort serialPort;

	static OutputStream out;

	static InputStream in;*/

	static byte[] lightGreen = new byte[] { 0x02, 0x00, 0x00, 0x04, 0x6A, 0x10,
			0x02, 0x70, 0x03 };// ¿ªÂÌµÆ

	static byte[] lightYellow = new byte[] { 0x02, 0x00, 0x00, 0x04, 0x6A,
			0x10, 0x03, 0x71, 0x03 };// ¿ª»ÆµÆ

	static byte[] offGreen = new byte[] { 0x02, 0x00, 0x00, 0x04, 0x6A, 0x01,
			0x6F, 0x03 };// ¹ØÂÌµÆ

	static byte[] offYellow = new byte[] { 0x02, 0x00, 0x00, 0x04, 0x6A, 0x00,
		0x6E, 0x03 };// ¹Ø»ÆµÆ
	
	static byte[] getDevNum = new byte[] { 0x02, 0x00, 0x00, 0x10, 0x03, 0x14,
		0x17, 0x03 };// µÃµ½¶Á¿¨Æ÷±àºÅ
	
	static byte[] searchAll = new byte[] { 0x02,0x00 ,0x00 ,0x04 ,0x46 ,0x52 ,
		(byte)0x9C ,0x03 };// Œ¤ËùÓÐµÄ¿¨
	
	static byte[] antiCollision = new byte[]{0x02 ,0x00 ,0x00 ,0x04 ,0x47 ,0x04, 
		0x4F ,0x03};//·À³åÍ»
	
	static byte[] choseCard = new byte[]{0x02,0x00 ,0x00 ,0x07 ,0x48 ,(byte)0x82,
		(byte)0xD4,(byte)0xD7 ,0x11 ,(byte)0x8D ,0x03};//Ñ¡¿¨£¬¿¨ºÅÎª£º82 D4 D7 11
	
	static byte[] checkCode = new byte[]{0x02 ,0x00 ,0x00 ,0x0B ,0x4A ,0x60, 0x00 ,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xAF,0x03};//ÑéÖ¤AÃÜÔ¿
	
	static byte[] readSector = new byte[]{0x02 ,0x00 ,0x00 ,0x04 ,0x4B ,0x00, 
		0x4F ,0x03};//·À³åÍ»

	
	public static void main(String args[]) {
		
		/*for (int i = 0; i < 1; i++) {
			new WriteBean(lightGreen);
			try {
				Thread.sleep(100);// 1000ms = 1sec
			} catch (InterruptedException e) {
			}

			new WriteBean(offGreen);
			try {
				Thread.sleep(100);// 1000ms = 1sec
			} catch (InterruptedException e) {
			}
			new WriteBean(lightYellow);
			try {
				Thread.sleep(100);// 1000ms = 1sec
			} catch (InterruptedException e) {
			}
			new WriteBean(offYellow);
			try {
				Thread.sleep(100);// 1000ms = 1sec
			} catch (InterruptedException e) {
			}*/
		FunCollection ex=new FunCollection();
		ex.R_W_judge(searchAll);
		ex.R_W_judge(antiCollision);
			/*new WriteBean(searchAll);
			try {
				Thread.sleep(100);// 1000ms = 1sec
			} catch (InterruptedException e) {
			}
			new judgeR_W(searchAll);
			
			new WriteBean(antiCollision);*/
			/*new WriteBean(choseCard);
			GetTime T=new GetTime();
			T.GetTime();
			new WriteBean(checkCode);
			new WriteBean(readSector);*/
		}
	}

//}
