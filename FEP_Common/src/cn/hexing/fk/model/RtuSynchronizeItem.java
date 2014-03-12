package cn.hexing.fk.model;

public class RtuSynchronizeItem {
	/** 终端局号ID */
    private String rtuId;
    /** 同步类型：0 终端档案同步；1 任务模板同步 */
    private int SycType;
    /** 同步时间 */
    private String SycTime;
    
	public String getRtuId() {
		return rtuId;
	}
	public void setRtuId(String rtuId) {
		this.rtuId = rtuId;
	}
	public int getSycType() {
		return SycType;
	}
	public void setSycType(int sycType) {
		SycType = sycType;
	}
	public String getSycTime() {
		return SycTime;
	}
	public void setSycTime(String sycTime) {
		SycTime = sycTime;
	}
    
}
