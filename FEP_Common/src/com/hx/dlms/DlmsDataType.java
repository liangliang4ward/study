package com.hx.dlms;

public interface DlmsDataType {
	public static final String[] TYPE_NAME = new String[] {"null","array","struct","boolean",
		"bit-string","int32","unsigned32","float32","[8]undef","octet-string","visiable-string",
		"[11]undef","[12]undef","bcd(int8)","[14]undef","integer(int8)","long(int16)","unsigned(unsigned8)","unsigned-long(unsigned16)",
		"compact-array","long64","unsigned-long64","enum","float32","float64",
		"date_time","date","time","28-x" };

	public static final int NULL = 0;
	public static final int ARRAY = 1;
	public static final int STRUCTURE = 2;
	public static final int BOOL = 3;
	public static final int BIT_STRING = 4;
	public static final int DOUBLE_LONG = 5;
	public static final int DOUBLE_LONG_UNSIGNED = 6;
	public static final int FLOATING_POINT = 7;
	
	public static final int OCTET_STRING = 9;
	public static final int VISIABLE_STRING = 10;
	
	public static final int BCD = 13;
	public static final int INTEGER = 15;
	public static final int LONG = 16;
	public static final int UNSIGNED = 17;
	public static final int UNSIGNED_LONG = 18;
	public static final int COMPACT_ARRAY = 19;
	public static final int LONG64 = 20;
	public static final int UNSIGNED64 = 21;
	public static final int ENUM = 22;
	public static final int FLOAT32 = 23;
	public static final int FLOAT64 = 24;
	public static final int DATE_TIME = 25;
	public static final int DATE = 26;
	public static final int TIME = 27;
	public static final int DONT_CARE = 255;
	//below is not used in DLMS_DATA
	public static final int ARRAY_STRUCT = 256;
	public static final int BCD_N = 257;

	//特殊格式,用来处理类似于SGC号，090X后面跟的直接就是可用值
	public static final int OCTET_RAW = 257;
	
}
