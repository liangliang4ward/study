package test_.serial;

public class ReturnEqual {
	static byte[] contact_setBaud=new byte[]{0x02,0x00 ,0x00 ,0x10 ,0x03 ,0x15 ,0x00 ,0x18 ,0x03};
	static byte[] light_on_off=new byte[]{0x02 ,0x00 ,0x00 ,0x10 ,0x03 ,0x6A ,0x00 ,0x6D ,0x03};
	static byte[] searchAll = new byte[] { 0x02,0x00 ,0x00 ,0x05 ,0x46 ,0x00 ,
		0x4F ,0x03 };// 尋所有的卡,返回S50卡type：0400
	
	static byte[] antiCollision = new byte[]{0x02 ,0x00 ,0x00 ,0x07 ,0x47 ,0x00,(byte)0x82,
		(byte)0xD4,(byte)0xD7 ,0x11, (byte)0x8C ,0x03};//防冲突，返回4字节卡号82 D4 D7 11
	
	static byte[] choseCard = new byte[]{0x02,0x00 ,0x00 ,0x04 ,0x48 ,0x00,0x08,0x54,
		0x03};//选卡号为,返回卡容量08
	
	static byte[] checkCode = new byte[]{0x02 ,0x00 ,0x00 ,0x10,0x03,0x4A, 0x00 ,0x4D,0x03};//验证A密钥成功
	
	static String[] w=new String[100];
	
	public  String[] ReadEqual(byte r[]){
		for(int i=0;i<r.length;i++){
			String hexA = Integer.toHexString(r[i] & 0xFF);
			//String hexB = Integer.toHexString(b[i] & 0xFF);
			w[i]=hexA;
			//System.out.print(w[i].toUpperCase()+" ");
			}
			if(w[5].equals("0")||w[6].equals("0")){
				System.out.println("读取卡成功!");
				
			}
			else{
				System.out.println("对不起，操作失败!!");	
			}
				
			return w;
		}
	public static void main(String args[]){
		ReturnEqual ee=new ReturnEqual();
		ee.ReadEqual(antiCollision);
		ee.ReadEqual(checkCode);
	}
}


