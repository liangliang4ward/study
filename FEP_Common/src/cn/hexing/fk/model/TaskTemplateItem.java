package cn.hexing.fk.model;

/**
 * 终端任务抄录数据项
 */
public class TaskTemplateItem {
	/** 任务模版ID */
    private String taskTemplateID;
    /** 数据项编码 */
    private String code;
    
    public String toString() {
        return "[taskPlateID=" + taskTemplateID + ", code=" + code + "]";
    }
       
    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
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
}
