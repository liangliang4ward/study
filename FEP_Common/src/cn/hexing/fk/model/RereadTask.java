package cn.hexing.fk.model;

import java.util.HashMap;
import java.util.Map;


/**
 * 漏点补招任务信息
 */
public class RereadTask {   
    /** 终端局号ID */
    private String rtuId;
    /** 任务号 */
    private String taskNum;
    /** 任务间隔 */
    private String taskInterval;
    /** 补招策略ID */
    private Integer rereadPolicyID;  
    /** 任务时间点列表<任务时间，补招信息类> */
    private Map<Long,RereadInfo> datemaps=new HashMap<Long,RereadInfo>();
    
	public String getRtuId() {
		return rtuId;
	}
	public void setRtuId(String rtuId) {
		this.rtuId = rtuId;
	}
	public String getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}
	public String getTaskInterval() {
		return taskInterval;
	}
	public void setTaskInterval(String taskInterval) {
		this.taskInterval = taskInterval;
	}
	public Integer getRereadPolicyID() {
		return rereadPolicyID;
	}
	public void setRereadPolicyID(Integer rereadPolicyID) {
		this.rereadPolicyID = rereadPolicyID;
	}
	public Map<Long, RereadInfo> getDatemaps() {
		return datemaps;
	}
	public void setDatemaps(Map<Long, RereadInfo> datemaps) {
		this.datemaps = datemaps;
	}
	
}
