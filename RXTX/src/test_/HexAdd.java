package test_;
public class HexAdd {
	static byte[] a = new byte[] { 0x23, 0x56 };

	static String s = "FF FF FF FF FF FF";

	public static void main(String args[]) {
		String d = "ddddddddddddddd";
		String f = "fffffffffffffff";
		f = f.concat(d);

		// System.out.println("s".concat("bbb").concat("ggggg"));

		System.out.println(f);
		System.out.println(a);

		for (int i = 0; i < a.length; i++) {
			String strHex = new String();
			strHex = Integer.toHexString(a[i]).toUpperCase();
			strHex = strHex.concat(s);

			/*
			 * s=s.concat(strHex); System.out.println(s);
			 */
			System.out.println(strHex);
		}
	}

}
