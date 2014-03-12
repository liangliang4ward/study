package cn.hexing.fk.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @author gaoll
 *
 * @time 2013-2-17 下午2:06:43
 *
 * @info 升级信息
 * 
 */
public class UpgradeInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 278785093401013791L;
	/**升级状态:升级成功=0,等待升级1,正在升级=2,升级终止=3,等待补发=4,正在补发=5,补发终止=6,检查位图情况失败=7,验证升级包失败=8,检查升级包失败=9,设置生效时间失败=10,升级失败=255*/
	private int status;
	/**终端逻辑地址*/
	private String logicAddr;
	/**测量点*/
	private int tn;
	/**文件头信息*/
	private String fileHead;
	/**补发块*/
	private String reissueBlock;
	/**块总数*/
	private int blockCount;
	/**当前发送块号*/
	private int curBlockNum;
	/**Ftp目录*/
	private String ftpDir;
	/**Ftp用户名*/
	private String ftpUserName;
	/**Ftp密码*/
	private String ftpPassword;
	/**Ftp端口*/
	private int ftpPort;
	/**FtpIp*/
	private String ftpIp;
	/**文件名*/
	private String fileName;
	/**生效时间*/
	private	Date effectDate;
	/**每帧发送最大字节数,用于计算总共多少帧*/
	private int maxSize;
	
	private String protocol;
	
	private long softUpgradeID;
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**升级成功*/
	public static final int SUCCESS=0;
	/**等待升级*/
	public static final int WAIT_UPGRADE=1;
	/**升级中*/
	public static final int UPGRADEING=2;
	/**升级终止*/
	public static final int UPGRADE_PAUSE=3;
	/**等待补发*/
	public static final int WAIT_RESSIUE=4;
	/**补发中*/
	public static final int RESSIUEING=5;
	/**补发终止*/
	public static final int RESSIUE_PAUSE=6;
	/**检查位图没有返回*/
	public static final int CHECK_MAP_FAIL=7;
	/**验证升级包没有返回*/
	public static final int VERFIY_FILE_FAIL=8;
	/**检查升级包没有返回*/
	public static final int CHECK_FILE_FAIL=9;
	/**设置生效时间没有返回*/
	public static final int SET_EFFECTTIME_FAIL=10;
	/**读取状态没有返回*/
	public static final int READ_STATUS_FAIL=12;
	/**升级初始化没有返回*/
	public static final int WAIT_UPGARDEINIT=13; 
	/**升级失败*/
	public static final int FAIL=255;

	public final String getLogicAddr() {
		return logicAddr;
	}

	public final void setLogicAddr(String logicAddr) {
		this.logicAddr = logicAddr;
	}

	public final int getTn() {
		return tn;
	}

	public final void setTn(int tn) {
		this.tn = tn;
	}

	public final String getFileHead() {
		return fileHead;
	}

	public final void setFileHead(String fileHead) {
		this.fileHead = fileHead;
	}

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public final String getReissueBlock() {
		return reissueBlock;
	}

	public final void setReissueBlock(String reissueBlock) {
		this.reissueBlock = reissueBlock;
	}

	public final int getBlockCount() {
		return blockCount;
	}

	public final void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}

	public final int getCurBlockNum() {
		return curBlockNum;
	}

	public final void setCurBlockNum(int curBlockNum) {
		this.curBlockNum = curBlockNum;
	}

	public final String getFtpDir() {
		return ftpDir;
	}

	public final void setFtpDir(String ftpDir) {
		this.ftpDir = ftpDir;
	}

	public final String getFtpUserName() {
		return ftpUserName;
	}

	public final void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}

	public final String getFtpPassword() {
		return ftpPassword;
	}

	public final void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public final int getFtpPort() {
		return ftpPort;
	}

	public final void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public final String getFtpIp() {
		return ftpIp;
	}

	public final void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	public final String getFileName() {
		return fileName;
	}

	public final void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public final Date getEffectDate() {
		return effectDate;
	}

	public final void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public final int getMaxSize() {
		return maxSize;
	}

	public final void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public long getSoftUpgradeID() {
		return softUpgradeID;
	}

	public void setSoftUpgradeID(long softUpgradeID) {
		this.softUpgradeID = softUpgradeID;
	}


}
