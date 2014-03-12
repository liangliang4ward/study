package cn.hexing.fk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 终端任务
 */
public class TaskTemplate {   
    /** 任务模版ID */
    private String taskTemplateID;
    /** 任务类型: 01专变，02公变，03低压,与终端用途字段保持一致*/
    private String taskType;	
    /** 采样开始基准时间 */
    private int sampleStartTime;
    /**任务属性  日冻结、月冻结.......*/
    private String taskProperty;
    /** 采样开始基准时间单位 */
    private String sampleStartTimeUnit;
    /** 采样间隔时间 */
    private int sampleInterval;
    /** 采样间隔时间单位 */
    private String sampleIntervalUnit;
    /** 上送基准时间 */
    private int uploadStartTime;
    /** 上送基准时间单位 */
    private String uploadStartTimeUnit;
    /** 上送间隔时间 */
    private int uploadInterval;
    /** 上送间隔时间单位 */
    private String uploadIntervalUnit;
    /** 上报数据频率 */
    private int frequence;
    /**保存点数*/
    private int savepts;
    /**执行次数*/
    private int donums;			
    /** 任务抄录数据项编码的字符串组合 */
    private String dataCodesStr;
    /** 任务抄录数据项编码列表[String] */
    private List<String> dataCodes=new ArrayList<String>();   
	/** 测量点号(非初始化属性) */
    private String tn;
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        return sb.toString();
    }
    
    /**
     * 添加数据项编码
     * @param code 上报数据项编码
     */
    public void addDataCode(String code) {  
        dataCodes.add(code);
    }
    
    /**
     * 取得任务抄录数据项编码的字符串组合
     * @return
     */
    public String getDataCodesAsString() {
        if (dataCodesStr == null && dataCodes != null) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < dataCodes.size(); i++) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((String) dataCodes.get(i));
            }
        }
        return dataCodesStr;
    }
    
    
    /**
     * @return Returns the taskType.
     */
    public String getTaskType() {
        return taskType;
    }
    /**
     * @param taskType The taskType to set.
     */
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    
    /**
     * @return Returns the tn.
     */
    public String getTn() {
        return tn;
    }
    /**
     * @param tn The tn to set.
     */
    public void setTn(String tn) {
        this.tn = tn;
    }   
    
    /**
     * @return Returns the sampleStartTime.
     */
    public int getSampleStartTime() {
        return sampleStartTime;
    }
    /**
     * @param sampleStartTime The sampleStartTime to set.
     */
    public void setSampleStartTime(int sampleStartTime) {
        this.sampleStartTime = sampleStartTime;
    }
    /**
     * @return Returns the sampleStartTimeUnit.
     */
    public String getSampleStartTimeUnit() {
        return sampleStartTimeUnit;
    }
    /**
     * @param sampleStartTimeUnit The sampleStartTimeUnit to set.
     */
    public void setSampleStartTimeUnit(String sampleStartTimeUnit) {
        this.sampleStartTimeUnit = sampleStartTimeUnit;
    }
    /**
     * @return Returns the sampleInterval.
     */
    public int getSampleInterval() {
        return sampleInterval;
    }
    /**
     * @param sampleInterval The sampleInterval to set.
     */
    public void setSampleInterval(int sampleInterval) {
        this.sampleInterval = sampleInterval;
    }
    /**
     * @return Returns the sampleIntervalUnit.
     */
    public String getSampleIntervalUnit() {
        return sampleIntervalUnit;
    }
    /**
     * @param sampleIntervalUnit The sampleIntervalUnit to set.
     */
    public void setSampleIntervalUnit(String sampleIntervalUnit) {
        this.sampleIntervalUnit = sampleIntervalUnit;
    }
    /**
     * @return Returns the uploadStartTime.
     */
    public int getUploadStartTime() {
        return uploadStartTime;
    }
    /**
     * @param uploadStartTime The uploadStartTime to set.
     */
    public void setUploadStartTime(int uploadStartTime) {
        this.uploadStartTime = uploadStartTime;
    }
    /**
     * @return Returns the uploadStartTimeUnit.
     */
    public String getUploadStartTimeUnit() {
        return uploadStartTimeUnit;
    }
    /**
     * @param uploadStartTimeUnit The uploadStartTimeUnit to set.
     */
    public void setUploadStartTimeUnit(String uploadStartTimeUnit) {
        this.uploadStartTimeUnit = uploadStartTimeUnit;
    }
    /**
     * @return Returns the uploadInterval.
     */
    public int getUploadInterval() {
        return uploadInterval;
    }
    /**
     * @param uploadInterval The uploadInterval to set.
     */
    public void setUploadInterval(int uploadInterval) {
        this.uploadInterval = uploadInterval;
    }
    /**
     * @return Returns the uploadIntervalUnit.
     */
    public String getUploadIntervalUnit() {
        return uploadIntervalUnit;
    }
    /**
     * @param uploadIntervalUnit The uploadIntervalUnit to set.
     */
    public void setUploadIntervalUnit(String uploadIntervalUnit) {
        this.uploadIntervalUnit = uploadIntervalUnit;
    }
    /**
     * @return Returns the frequence.
     */
    public int getFrequence() {
        return frequence;
    }
    /**
     * @param frequence The frequence to set.
     */
    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }
    
    /**
     * @return Returns the dataCodes.
     */
    public List<String> getDataCodes() {
        return dataCodes;
    }
    /**
     * @param dataCodes The dataCodes to set.
     */
    public void setDataCodes(List<String> dataCodes) {
        this.dataCodes = dataCodes;
        this.dataCodesStr = null;
    }
   

	

	public int getDonums() {
		return donums;
	}

	public int getSavepts() {
		return savepts;
	}

	
	public void setDonums(int donums) {
		this.donums = donums;
	}

	public void setSavepts(int savepts) {
		this.savepts = savepts;
	}
	/**
	 * @return 返回 dataCodesStr。
	 */
	public String getDataCodesStr() {
		return dataCodesStr;
	}

	/**
	 * @param dataCodesStr 要设置的 dataCodesStr。
	 */
	public void setDataCodesStr(String dataCodesStr) {
		this.dataCodesStr = dataCodesStr;
	}

	public String getTaskTemplateID() {
		return taskTemplateID;
	}

	public void setTaskTemplateID(String taskTemplateID) {
		this.taskTemplateID = taskTemplateID;
	}

	public final String getTaskProperty() {
		return taskProperty;
	}

	public final void setTaskProperty(String taskProperty) {
		this.taskProperty = taskProperty;
	}	

}
