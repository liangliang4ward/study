package cn.hexing.fk.message;

public class MessageConst {
	//浙江规约消息常数定义
	/**
	 * 中继
	 */
	public static final byte ZJ_FUNC_RELAY = 0x00;
	/**
	 * 读当前数据
	 */
	public static final byte ZJ_FUNC_READ_CUR = 0x01;
	/**
	 * 读任务数据
	 */
	public static final byte ZJ_FUNC_READ_TASK = 0x02;
	/**
	 * 读编程日志
	 */
	public static final byte ZJ_FUNC_READ_PROG	= 0x04;
	/**
	 * 实时写对象参数
	 */
	public static final byte ZJ_FUNC_WRITE_ROBJ = 0x07;	
	/**
	 * 写对象参数
	 */
	public static final byte ZJ_FUNC_WRITE_OBJ = 0x08;
	/**
	 * 异常告警
	 */
	public static final byte ZJ_FUNC_EXP_ALARM	= 0x09;
	/**
	 * 告警确认
	 */
	public static final byte ZJ_FUNC_ALARM_CONFIRM = 0x0A;
	/**
	 * 广规点抄任务数据
	 */
	public static final byte GG_FUNC_READ_TASK1 = 0x11;
	/**
	 * 广规集抄任务数据
	 */
	public static final byte GG_FUNC_READ_TASK2 = 0x12;
	/**
	 * 广规集中器操作类数据
	 */
	public static final byte GG_FUNC_Action = 0x14;
	/**
	 * 广规集中器档案注册信息数据
	 */
	public static final byte GG_FUNC_AutoRegistered = 0x15;
	/**
	 * 广规集抄事件告警数据
	 */
	public static final byte GG_FUNC_Event = 0x19;
	/**
	 * 广规预付费信息数据
	 */
	public static final byte GG_Pay_token = 0x33;
	
	/**
	 * 广规远程升级
	 */
	public static final byte GG_UPGRADE=0x30;
	

	/**
	 * 用户自定义数据
	 */
	public static final byte ZJ_FUNC_USER_DEFINE = 0x0F;
	public static final byte ZJ_FUNC_LOGIN = 0x21;			//登录
	public static final byte ZJ_FUNC_LOGOUT = 0x22;		//登录退出
	public static final byte ZJ_FUNC_HEART = 0x24;			//心跳检验
	public static final byte ZJ_FUNC_REQ_SMS = 0x28;		//请求发送短信
	public static final byte ZJ_FUNC_RECV_SMS = 0x29;		//收到短信上报
	
	
	//消息方向定义
	public static final byte DIR_DOWN = 0x00;			//由主站发出的命令帧
	public static final byte DIR_UP = 0x01;				//由终端发出的应答帧
	
	//国网应用层功能码定义  16进制定义
	public static final byte GW_FUNC_REPLY = 0x00;		//确认∕否认
	public static final byte GW_FUNC_RESET = 0x01;		//复位
	public static final byte GW_FUNC_HEART = 0x02;		//心跳, 链路接口检测
	public static final byte GW_FUNC_RELAY_CTRL = 0X03;		//中继站命令
	public static final byte GW_FUNC_SETPARAM = 0x04;	//设置参数
	public static final byte GW_FUNC_CONTROL = 0x05;	//控制命令
	public static final byte GW_FUNC_AUTH = 0x06;		//身份认证及密钥协商
	public static final byte GW_FUNC_BAK1 = 0x07;		//备用
	public static final byte GW_FUNC_REQ_CASCADE_UP=0x08;	//请求被级联终端主动上报
	public static final byte GW_FUNC_REQ_RTU_CFG = 0x09;	//请求终端配置
	public static final byte GW_FUNC_GETPARAM = 0x0A;		//查询参数
	public static final byte GW_FUNC_GET_TASK = 0x0B;		//请求任务数据
	public static final byte GW_FUNC_GET_DATA1 = 0x0C;		//请求1类数据（实时数据）
	public static final byte GW_FUNC_GET_DATA2 = 0x0D;		//请求2类数据（历史数据）
	public static final byte GW_FUNC_GET_DATA3 = 0x0E;		//请求3类数据（事件数据）
	public static final byte GW_FUNC_FILE = 0x0F;			//文件传输 厂家自定义数据
	public static final byte GW_FUNC_RELAY_READ = 0x10;		//数据转发。中继抄表
	public static final byte GW_FUNC_BAK2 = 0x11;			//11H～FFH,备用
	
	//国网规约帧层面功能码	10进制定义 主站下行
	public static final byte GW_FN_RESET = 1;				//复位命令
	public static final byte GW_FN_USER = 4;				//用户数据
	public static final byte GW_FN_HEART = 9;				//心跳 或者叫 链路测试
	public static final byte GW_FN_LEVEL1 = 10;				//请求1级数据 应用层请求确认（CON=1）
	public static final byte GW_FN_LEVEL2 = 11;				//请求2级数据

	//dlms规约功能码
	public static final short DLMS_FUNC_HEART = 0xDD;			//心跳检验
	
	public static final String DLMS_RELAY_FLAG = "dlms-relay";
}

