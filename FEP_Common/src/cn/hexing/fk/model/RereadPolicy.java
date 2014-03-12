package cn.hexing.fk.model;


/**
 * 补招策略
 */
public class RereadPolicy {   
    /** 补招策略ID */
    private Integer rereadPolicyID;  
    /** 补招间隔（分钟为单位） */
    private int rereadInterval;
    /** 补招时间范围（分钟为单位）*/
    private int rereadRange;
    /** 补招基准时间（分钟为单位） */
    private int rereadStartTime;
    /** 补招提前时间（小时为单位） */
    private int rereadAdvanceTime;
    /** 补招启动标志 */
    private boolean rereadStartTag;
    
	public Integer getRereadPolicyID() {
		return rereadPolicyID;
	}
	public void setRereadPolicyID(Integer rereadPolicyID) {
		this.rereadPolicyID = rereadPolicyID;
	}
	public int getRereadInterval() {
		return rereadInterval;
	}
	public void setRereadInterval(int rereadInterval) {
		this.rereadInterval = rereadInterval;
	}
	public int getRereadRange() {
		return rereadRange;
	}
	public void setRereadRange(int rereadRange) {
		this.rereadRange = rereadRange;
	}
	public int getRereadStartTime() {
		return rereadStartTime;
	}
	public void setRereadStartTime(int rereadStartTime) {
		this.rereadStartTime = rereadStartTime;
	}
	public int getRereadAdvanceTime() {
		return rereadAdvanceTime;
	}
	public void setRereadAdvanceTime(int rereadAdvanceTime) {
		this.rereadAdvanceTime = rereadAdvanceTime;
	}
	public boolean isRereadStartTag() {
		return rereadStartTag;
	}
	public void setRereadStartTag(boolean rereadStartTag) {
		this.rereadStartTag = rereadStartTag;
	}
    

   
    
    
    
    
	
  		
}
