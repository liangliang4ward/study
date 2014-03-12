package test_.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class check_mod_Code {
	static byte[] checkCode = new byte[] { 0x02, 0x00, 0x00, 0x0B, 0x4A, 0x60,
			0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xAF, 0x03 };// 验证A密钥

	static String[] w = new String[100];

	public static String code2String() {
		for (int i = 0; i < checkCode.length; i++) {
			String hexA = Integer.toHexString(checkCode[i] & 0xFF);
			w[i] = hexA;
		}
		String Code = w[7].concat(w[8]).concat(w[9]).concat(w[10])
				.concat(w[11]).concat(w[12]);
		return Code;
	}

	/* 判断输入密码是否正确
	 * param in--code entered
	 * 
	 * */

	public static boolean cc(String in) {
		while (true) {
			/*for (int i = 0; i < checkCode.length; i++) {
			 String hexA = Integer.toHexString(checkCode[i] & 0xFF);
			 w[i] = hexA;
			 }
			 String Code = w[7].concat(w[8]).concat(w[9]).concat(w[10]).concat(
			 w[11]).concat(w[12]);*/
			String Code = code2String();
			in = enterCode();
			if (in.equals(Code)) {
				System.out.print("验证密码成功！");
				return true;
			} else {
				System.out.print("对不起，密码错误！");
				return false;
			}
		}
	}

	public static void modCode(String oldCode, String newCode) {
		oldCode = code2String();
		while (true) {
			while (enterCode().equals(oldCode)) {//当输入密码正确时
				newCode = enterCode();
				if (newCode.length() != 12) {
					System.out.print("对不起，密码长度应为12,请重新输入！");
				} else {
					byte r[] = new byte[6];
					r = StringToHex.HexString2Bytes(newCode);
					for (int z = 0; z < 6; z++) {
						checkCode[z + 7] = r[z];
					}
				}
			}
		}
	}

	/*while (true) {
	 oldCode = enterCode();
	 while(cc(oldCode)){
	 newCode = enterCode();
	 if (newCode.length() != 12) {
	 System.out.print("对不起，密码长度应为12,请重新输入！");
	 }
	 else {
	 byte r[] = new byte[6];
	 r=StringToHex.HexString2Bytes(newCode);
	 for(int z=0;z<6;z++){
	 checkCode[z+7]=r[z];}
	 }
	 }
	 }*/

	public static void main(String args[]) {
		check_mod_Code e = new check_mod_Code();
		e.modCode("ffffffffffff".toUpperCase(), "");
	}

	public static String enterCode() {
		String str = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("请输入密码:");
			try {
				str = br.readLine();
				System.out.println();
				System.out.println("您输入的密码为:" + str.toUpperCase());
				return str;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
