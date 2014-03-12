/**
 * 浙江电力负控规约消息头定义。
 */
package cn.hexing.fk.message.zj;

import cn.hexing.fk.utils.HexDump;

/**
 */
public class MessageZjHead {
	public byte flag1; //帧起始符 68h

	public byte rtua_a1; //地市码

	public byte rtua_a2; //区县码

	public short rtua_b1b2; //终端地址

	public int rtua; //终端逻辑地址

	public byte msta; //主站地址

	public byte fseq; //帧序号

	public byte iseq; //帧内序号

	public byte flag2; //帧起始符 68h

	public byte c_dir; //控制码中的传送方向

	public byte c_expflag; //异常标志

	public byte c_func; //功能码

	public short dlen; //数据长度

	//数据内容...

	public byte cs; //校验码

	public byte flag3; //结束码 16h

	public MessageZjHead(){
		flag1 = 0x68;
		flag2 = 0x68;
		flag3 = 0x16;
		iseq = 0;
		c_dir = 0; //主站下发
		c_expflag = 0;
		c_func = 0;
		dlen = 0;
		cs = 0;
		fseq = 0;
		msta = 1;
		rtua = 0;
		rtua_a1 = rtua_a2 = 0;
		rtua_b1b2 = 0;
	}
	
	public void parseRtua() {
		if (rtua != 0) {
			return;
		} else {
			rtua |= (0xFF & rtua_a1)<< 24;
			rtua |= (0xFF & rtua_a2)<< 16;
			rtua |= (0xFF & rtua_b1b2)<<8;
			rtua |= (0xFF & (rtua_b1b2>>8));
		}
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append("[rtua=").append(HexDump.toHex(rtua));
		//sb.append(",rtua_a1=").append(HexDump.toHex(rtua_a1));
		//sb.append(",rtua_a2=").append(HexDump.toHex(rtua_a2));
		//sb.append(",rtua_b1b2=").append(HexDump.toHex(rtua_b1b2));
		sb.append(",msta=").append(HexDump.toHex(msta));
		sb.append(",fseq=").append(HexDump.toHex(fseq));
		sb.append(",iseq=").append(HexDump.toHex(iseq));
		//sb.append(",flag2=").append(HexDump.toHex(flag2));
		sb.append(",c_dir=").append(HexDump.toHex(c_dir));
		sb.append(",c_expflag=").append(HexDump.toHex(c_expflag));
		sb.append(",c_func=").append(HexDump.toHex(c_func));
		sb.append(",datalen=").append(HexDump.toHex(dlen));
		sb.append("]");
		return sb.toString();
	}
}
