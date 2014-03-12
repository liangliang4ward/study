package cn.hexing.fk.message.gw;

public class MessageGwHead {
	public byte flag1=0x68; //帧起始符 68h
	public byte flag2=0x68;
	public short dlen = 0;	//长度L包括控制域、地址域、链路用户数据，由2字节组成 d2-d15
	public byte proto_flag = 2;//长度L的d0d1两位 
	public int rtua = 0;	//a1a2 行政区划码+终端地址
	public byte a3_d0=0; 	//终端组地址标志，D0=0表示终端地址A2为单地址；D0=1表示终端地址A2为组地址
	public byte a3_msa=1; 	//A3的D1～D7组成0～127个主站地址MSA
	public byte c_dir=0; 	//传输方向位,C字节的d7位. 0 表示下行 1表示终端上行。
	public byte c_prm=1;	//启动标志位, d6  PRM =1：表示此帧报文来自启动站；PRM =0：表示此帧报文来自从动站
	public byte c_acd=0;	//D5位。ACD位用于上行响应报文中。ACD=1表示终端有重要事件等待访问
	public byte c_fcv=0;	//D4.  FCV=1：表示FCB位有效；FCV=0：表示FCB位无效。
	protected byte c_func=0;	//D0-D3 功能码。
	
	protected byte app_func=0;	//应用层功能码
	public byte seq_tpv=0;	//帧序列域字节D7 0：表示在附加信息域中无时间标签Tp
	public byte seq_fir=1;	//D6 置“1”，报文的第一帧
	public byte seq_fin=1;	//D5 置“1”，报文的最后一帧
	protected byte seq_con=1;	//D4 位置“1”，表示需要对该帧报文进行确认
	public byte seq_pseq=0;	//d0-d3
	
	//规约的L字段
	public void decodeL(short L){
		proto_flag = (byte)(L & 0x3) ;		// 2 表示Q／GDW 376.1-2009《电力用户用电信息采集系统通信协议：主站与采集终端通信协议》
		dlen = (short)(L >>> 2) ;
	}
	
	public short encodeL(){
		int len=(((dlen & 0xFFFF)<<2) | proto_flag );
		len= (len<<8)& 0x00FF00 | ((len>>> 8) & 0x00FF);
		return (short)len;
		//return (short)(((dlen & 0xFFFF)<<2) | proto_flag );
	}
	
	//读取控制域C
	public void decodeC(byte C){
		c_dir = (byte)( ( 0x80 & C ) >>> 7);
		c_prm = (byte)( ( 0x40 & C ) >>> 6);
		c_acd = (byte)( ( 0x20 & C ) >>> 5);
		c_fcv = (byte)( ( 0x10 & C ) >>> 4);
		c_func =(byte)( 0x0F & C );
	}
	
	public byte encodeC(){
		int c = c_dir << 7;
		c |= c_prm << 6;
		c |= c_acd << 5;
		c |= c_fcv << 4;
		c |= c_func;
		return (byte)c;
	}
	
	public void decodeA3(byte A3){
		a3_d0 = (byte)( 0x01 & A3);
		a3_msa = (byte)( A3>>>1 );
	}
	
	public byte encodeA3(){
		return (byte)((a3_msa << 1) | a3_d0);
	}
	
	public void decodeSEQ(byte SEQ){
		seq_tpv = (byte)( ( 0x80 & SEQ ) >>> 7);
		seq_fir = (byte)( ( 0x40 & SEQ ) >>> 6);
		seq_fin = (byte)( ( 0x20 & SEQ ) >>> 5);
		seq_con = (byte)( ( 0x10 & SEQ ) >>> 4);
		seq_pseq =(byte)( 0x0F & SEQ );
	}
	
	public byte encodeSEQ(){
		int c = seq_tpv << 7;
		c |= seq_fir << 6;
		c |= seq_fin << 5;
		c |= seq_con << 4;
		c |= (seq_pseq & 0x0F);
		return (byte)c;
	}
}
