package cn.hexing.fk.model;

/**
 * 下行请求对象定义
 */
public abstract class Operator {
	/** 默认值 */
    public static final int DEFAULT = 0; 
    /** 自动装接模块对象 */
    public static final int ZDZJ = 1;  
    /** 后台下发模块 */
    public static final int HTXF = 5; 
    /** 主站任务轮招模块功能中的数据采集功能 */
    public static final int ZZRW_SJCJ = 7; 
    /** dlms规约主站请求功能 */
    public static final int ZZ_DLMS = 8; 
    
    /** 自动装接模块中的电表控制功能 */
    public static final int ZDZJ_DBKZ = 1;      
    /** 后台下发模块中的终端对时功能 */
    public static final int HTXF_ZDDS = 2;    
    /** 国网漏点补招 */
    public static final int GWLDBZ = 3;  
    /** 自动装接模块中的身份认证功能 */
    public static final int ZDZJ_SFRZ = 4;  
    /** 主站任务轮招模块 */
    public static final int ZZRW = 6; 
    
}
