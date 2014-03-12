package cn.hexing.fk.model;

import java.util.ArrayList;


/**
 * 任务数据项数据库保存信息
 */
public class TaskDbConfig {   
	/** 数据项编码 */
    private String code;
    /** 数据库表名+字段名+特殊处理标记字串*/
    private String dbConfigStr;
    /** 数据库表名+字段名+特殊处理标记:TaskDbConfigItem */
    private ArrayList<TaskDbConfigItem> taskDbConfigItemList=new ArrayList<TaskDbConfigItem>();
    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public void addTaskDbConfigItemList(String[] s){
		TaskDbConfigItem tsti= new TaskDbConfigItem() ;
		if (s.length==4){
			tsti.setTableName(s[0]);
			tsti.setFieldName(s[1]);
			tsti.setTag(s[2]);
			tsti.setTaskPropertyStr(s[3]);
			//tsti.setTaskType(s[4]);//任务类型即终端用途不再判断
			this.taskDbConfigItemList.add(tsti);
		}
	}
	
	public void setTaskDbConfigItemList(String dbConfigStr) {
		if(dbConfigStr==null) return;
		String[] s=dbConfigStr.split("/");
		for(int i=0;i<s.length;i++){
			String[] ss=s[i].split(";");
			addTaskDbConfigItemList(ss);
		}
	}

	public String getDbConfigStr() {
		return dbConfigStr;
	}

	public void setDbConfigStr(String dbConfigStr) {
		this.dbConfigStr = dbConfigStr;
		setTaskDbConfigItemList(dbConfigStr);
	}

	public ArrayList<TaskDbConfigItem> getTaskDbConfigItemList() {
		return taskDbConfigItemList;
	}	
}
