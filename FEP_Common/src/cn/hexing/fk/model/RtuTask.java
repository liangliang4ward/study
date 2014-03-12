package cn.hexing.fk.model;


/**
 * 终端任务信息
 */
public class RtuTask {   
    /** 终端局号ID */
    private String rtuId;
    /** 任务模版ID */
    private String taskTemplateID;
    /** 任务模版属性 */
    private String taskTemplateProperty;
    /** 终端任务号 */
    private int rtuTaskNum;
    /** 测量点号 */
    private String tn;
	
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
	 * @return 返回 rtuaTaskNum。
	 */
	public int getRtuTaskNum() {
		return rtuTaskNum;
	}
	/**
	 * @param rtuaTaskNum 要设置的 rtuaTaskNum。
	 */
	public void setRtuTaskNum(int rtuaTaskNum) {
		this.rtuTaskNum = rtuaTaskNum;
	}
	/**
	 * @return 返回 taskPlateID。
	 */
	public String getTaskTemplateID() {
		return taskTemplateID;
	}
	/**
	 * @param taskPlateID 要设置的 taskPlateID。
	 */
	public void setTaskTemplateID(String taskTemplateID) {
		this.taskTemplateID = taskTemplateID;
	}
	
	
	public String getTaskTemplateProperty() {
		return taskTemplateProperty;
	}
	public void setTaskTemplateProperty(String taskTemplateProperty) {
		this.taskTemplateProperty = taskTemplateProperty;
	}
	/**
	 * @return 返回 tn。
	 */
	public String getTn() {
		return tn;
	}
	/**
	 * @param tn 要设置的 tn。
	 */
	public void setTn(String tn) {
		this.tn = tn;
	}
    
	
}
