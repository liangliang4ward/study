package cn.hexing.fk.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.utils.StringUtil;

/**
 * Dlms表计档案结构
 * @author Administrator
 *
 */
public class DlmsMeterRtu {
	/**
	 * 终端逻辑地址
	 */
	private String logicAddress;
	/**
	 *  单位代码
	 */
	private String deptCode;
	/**
	 * 子规约号
	 */
	private String subProtocol;
	/**
	 *  表计局号
	 */
	private String meterId;
	/**
	 *  高级权限密码
	 */
	private String highPassword;
	/**
	 *  低级权限密码
	 */
	private String lowPassword;
	/**
	 * 根密钥
	 */
	private String rootPassword;
	
	/**
	 *  接线方式
	 */
	private String wiringMode;
	
	/**密钥类型*/
	private String keyType;
	
	/**密钥版本*/
	private String keyVersion;
	
	/**通讯端口号*/
	private int port;
	
	/**电表型号*/
	private String meterMode;
	
	private String phoneNum;
	
	private int linkMode;
	
	/**1=csd,2=csd&gprs*/
	private int csdStatus;
	
	/**固定IP，用于表做服务端*/
	private String fixIp;
	/**固定端口号，用于表做服务端*/
	private int fixPort;
	  /** 测量点列表 */
    private Map<String,MeasuredPoint> measuredPoints=new HashMap<String,MeasuredPoint>();
    /** 终端任务列表 */
    private Map<Integer,RtuTask> tasksMap=new HashMap<Integer,RtuTask>();
    /** 最后刷新时间*/
    private Date lastRefreshTime;
    
    public Date getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(Date lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	/**根据任务号,获得任务模板*/
    public TaskTemplate getTaskTemplate(String taskNum){
    	if (tasksMap == null || taskNum == null) {
            return null;
        }
        RtuTask rt=(RtuTask)tasksMap.get(new Integer(taskNum)); 
        if(rt !=null){
        	return RtuManage.getInstance().getTaskPlateInCache(rt.getTaskTemplateID());
        }                
        return null;
    }
    
	/**
     * 根据测量点号取得测量点
     * @param tn 测量点号
     * @return 测量点。如果不存在，则返回 null
     */
    public MeasuredPoint getMeasuredPoint(String tn) {
    	return  measuredPoints.get(tn);
    }
    /**
     * @param taskNum
     * @return
     */
    public RtuTask getRtuTask(String taskNum) {   
    	if (tasksMap == null || taskNum == null) {
            return null;
        }
    	return (RtuTask)tasksMap.get(new Integer(taskNum)); 
    }
	public final String getLogicAddress() {
		return logicAddress;
	}
	public final void setLogicAddress(String logicAddress) {
		this.logicAddress = logicAddress;
	}
	public final String getSubProtocol() {
		return subProtocol;
	}
	public final void setSubProtocol(String subProtocol) {
		this.subProtocol = subProtocol;
	}
	public final String getMeterId() {
		return meterId;
	}
	public final void setMeterId(String meterId) {
		this.meterId = meterId;
	}
	public final String getHighPassword() {
		return highPassword;
	}
	public final void setHighPassword(String highPassword) {
		this.highPassword = highPassword;
	}
	public final String getLowPassword() {
		return lowPassword;
	}
	public final void setLowPassword(String lowPassword) {
		this.lowPassword = lowPassword;
	}
	public final String getRootPassword() {
		return rootPassword;
	}
	public final void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}
	public final String getDeptCode() {
		return deptCode;
	}
	public final void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	   /**
     * 添加测量点
     * @param mp 测量点
     */
    public void addMeasuredPoint(MeasuredPoint mp) {
		measuredPoints.put(mp.getTn(),mp);                
    }
    
    public void addRtuTask(RtuTask rt){
    	tasksMap.put(rt.getRtuTaskNum(), rt);
    }

	public final String getWiringMode() {
		return wiringMode;
	}

	public final void setWiringMode(String wiringMode) {
		this.wiringMode = wiringMode;
	}

	public final String getKeyType() {
		return keyType;
	}

	public final void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public final String getKeyVersion() {
		return keyVersion;
	}

	public final void setKeyVersion(String keyVersion) {
		this.keyVersion = keyVersion;
	}

	public final Map<String, MeasuredPoint> getMeasuredPoints() {
		return measuredPoints;
	}

	public final void setMeasuredPoints(Map<String, MeasuredPoint> measuredPoints) {
		this.measuredPoints = measuredPoints;
	}

	public final Map<Integer, RtuTask> getTasksMap() {
		return tasksMap;
	}

	public final void setTasksMap(Map<Integer, RtuTask> tasksMap) {
		this.tasksMap = tasksMap;
	}

	public final int getPort() {
		return port;
	}

	public final void setPort(int port) {
		this.port = port;
	}

	public String getMeterMode() {
		return meterMode;
	}

	public void setMeterMode(String meterMode) {
		this.meterMode = meterMode;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public int getLinkMode() {
		return linkMode;
	}

	public void setLinkMode(int linkMode) {
		this.linkMode = linkMode;
	}

	public int getCsdStatus() {
		return csdStatus;
	}
	public void setCsdStatus(int csdStatus) {
		//1=csd 2=csd&gprs
		this.csdStatus = IMessage.COMMUNICATION_TYPE_NORMAL;
		switch(csdStatus){
		case 1:
			this.csdStatus = IMessage.COMMUNICATION_TYPE_CSD;
			break;
		case 2:
			this.csdStatus = IMessage.COMMUNICATION_TYPE_CSDorGPRS;
			break;
		case 3:
			this.csdStatus = IMessage.COMMUNICATION_TYPE_EXTERNAL;
			break;
		}
	}

	public void setIPandPort(String ipAndPort){
		if(StringUtil.isEmptyString(ipAndPort)) return;
		if(ipAndPort.indexOf(':')!=-1 && ipAndPort.split(":").length==2){
			setFixIp(ipAndPort.split(":")[0]);
			setFixPort(Integer.parseInt(ipAndPort.split(":")[1]));
		}
	}
	
	public String getIPandPort(){
		return getFixIp()+":"+getFixPort();
	}
	
	public String getFixIp() {
		return fixIp;
	}

	public void setFixIp(String fixIp) {
		this.fixIp = fixIp;
	}

	public int getFixPort() {
		return fixPort;
	}

	public void setFixPort(int fixPort) {
		this.fixPort = fixPort;
	}
	
}
