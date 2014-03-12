package cn.hexing.fk.model;

import cn.hexing.fk.utils.CalendarUtil;



/**
 * 通讯前置机终端档案结构
 */
public class ComRtu {     
    /** 终端局号ID */
    private String rtuId;
    /** 单位代码 */
    private String deptCode = "";
    /** 终端通讯规约 */
    private String rtuProtocol;
    /** 终端逻辑地址 */
    private int rtua;
    /** 终端逻辑地址（HEX） */
    private String logicAddress;
    /** 终端用途：01专变，02公变，03低压 */
    private String rtuType;
    /** 终端SIM卡号 */
    private String simNum;
    /** 主通讯信道类型 (8010)*/
    private String commType;
    /** 主通讯信道地址 (8010)*/
    private String commAddress;
    /** 备用通道类型1(8011) */
    private String b1CommType;
    /** 备用通道地址1(8011) */
    private String b1CommAddress;
    /** 终端类型*/
    private String terminalType;
    /** 通讯类型 包含GPRS、以太网*/
    private String communicationMode;
    
    
	public String getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	public String getB1CommAddress() {
		return b1CommAddress;
	}
	/**
	 * @param commAddress 要设置的 b1CommAddress。
	 */
	public void setB1CommAddress(String commAddress) {
		b1CommAddress = commAddress;
	}
	/**
	 * @return 返回 b1CommType。
	 */
	public String getB1CommType() {
		return b1CommType;
	}
	/**
	 * @param commType 要设置的 b1CommType。
	 */
	public void setB1CommType(String commType) {
		b1CommType = commType;
	}
	
	public String getRtuType() {
		return rtuType;
	}
	public void setRtuType(String rtuType) {
		this.rtuType = rtuType;
	}
	/**
	 * @return 返回 commAddress。
	 */
	public String getCommAddress() {
		return commAddress;
	}
	/**
	 * @param commAddress 要设置的 commAddress。
	 */
	public void setCommAddress(String commAddress) {
		this.commAddress = commAddress;
	}
	/**
	 * @return 返回 commType。
	 */
	public String getCommType() {
		return commType;
	}
	/**
	 * @param commType 要设置的 commType。
	 */
	public void setCommType(String commType) {
		this.commType = commType;
	}

	/**
	 * @return 返回 deptCode。
	 */
	public String getDeptCode() {
		return deptCode;
	}
	/**
	 * @param deptCode 要设置的 deptCode。
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * @return 返回 logicAddress。
	 */
	public String getLogicAddress() {
		return logicAddress;
	}
	/**
	 * @param logicAddress 要设置的 logicAddress。
	 */
	public void setLogicAddress(String logicAddress) {
		this.logicAddress = logicAddress;
	}
	
	/**
	 * @return 返回 rtua。
	 */
	public int getRtua() {
		return rtua;
	}
	/**
	 * @param rtua 要设置的 rtua。
	 */
	public void setRtua(int rtua) {
		this.rtua = rtua;
	}
	/**
	 * @return 返回 rtuId。
	 */
	public String getRtuId() {
		return rtuId;
	}
	/**
	 * @param rtuId 要设置的 rtuId。
	 */
	public void setRtuId(String rtuId) {
		this.rtuId = rtuId;
	}
	/**
	 * @return 返回 rtuProtocol。
	 */
	public String getRtuProtocol() {
		return rtuProtocol;
	}
	/**
	 * @param rtuProtocol 要设置的 rtuProtocol。
	 */
	public void setRtuProtocol(String rtuProtocol) {
		this.rtuProtocol = rtuProtocol;
	}

	/**
	 * @param simNum 要设置的 simNum。
	 */
	public void setSimNum(String simNum) {
		this.simNum = simNum;
	}
	
	public String getDateString(){
		return CalendarUtil.getDateString(System.currentTimeMillis());
	}
	public String getSimNum() {
		return simNum;
	}
	public String getCommunicationMode() {
		return communicationMode;
	}
	public void setCommunicationMode(String communicationMode) {
		this.communicationMode = communicationMode;
	}
}
