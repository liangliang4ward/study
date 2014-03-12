package cn.hexing.fk.model;


import java.util.HashMap;
import java.util.Map;


/**
 * 漏点补招终端档案结构
 */
public class RereadRtu {   
    /** 终端局号ID */
    private String rtuId;        
    /** 终端任务列表<补招策略ID,补招任务信息>*/
    private Map<Integer,RereadTask> rereadTasksMap=new HashMap<Integer,RereadTask>();

    public String getRtuId() {
		return rtuId;
	}
	public void setRtuId(String rtuId) {
		this.rtuId = rtuId;
	}
	public Map<Integer, RereadTask> getRereadTasksMap() {
		return rereadTasksMap;
	}
	public void setRereadTasksMap(Map<Integer, RereadTask> rereadTasksMap) {
		this.rereadTasksMap = rereadTasksMap;
	}

               	  		
}
