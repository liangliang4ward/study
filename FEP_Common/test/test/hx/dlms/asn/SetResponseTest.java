package test.hx.dlms.asn;

import java.io.IOException;

import cn.hexing.fk.utils.HexDump;

import com.hx.dlms.DecodeStream;
import com.hx.dlms.applayer.set.SetResponse;

public class SetResponseTest {

	public static void main(String[] args) {
		String s1 = "C50102000204110106FFFFFFFF02040500002710030116011130090AFFFFFFFFFFFFFFFFFFFF";
		byte[] buf = HexDump.toArray(s1);
		SetResponse sresp = new SetResponse();
		try {
			sresp.decode(DecodeStream.wrap(buf));
			System.out.println(sresp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
