package test_.serial;

public class toHex {
/*	public void toHexWrite(byte c[]) {
		for (int j = 0; j < c.length; j++) {
			String strHex = new String();
			strHex = Integer.toHexString(c[j]).toUpperCase();
			if (strHex.length() > 3)
				System.out.print(strHex.substring(6));
			else if (strHex.length() < 2)
				System.out.print("0" + strHex);
			else
				System.out.print(strHex);

			System.out.print(" ");
		}
	}*/

	public void ReadToHex(int r) {
		if(r==0)
			System.out.print("00 ");
		for (int i = 0; i < r; i++) {
			String hex = Integer.toHexString(r & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase());
			System.out.print(" ");
			r=1;//每次循环后，让r的值初始化为1，这样就可保证读取的数据不重复
		}
	}

	public void WriteToHex(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase());
			System.out.print(" ");
		}

	}

	public static void main(String args[]) {
		byte a[] = { 0X00, (byte)0X3C ,0X01};
		toHex c = new toHex();
		int e=78;
		c.ReadToHex(e);
		//c.printHexString(a);
		/*String w=Byte.toString(a[1]);
		if(w.length()<2)
		System.out.print("\n0"+w);
		else
		System.out.print("\n"+w);*/
	}

}
