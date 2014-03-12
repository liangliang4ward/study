package cn.hexing.fk.model;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.hexing.fk.message.gw.MessageGw;

/**
 * 业务处理器终端档案结构
 */
public class BizRtu {   
    /** 终端局号ID */
    private String rtuId;
    /** 单位代码 */
    private String deptCode;
    /** 终端规约类型 */
    private String rtuProtocol;
    /** 终端用途：01专变，02公变，03低压 */
    private String rtuType;
    /** 终端类型：01专变终端 02公变终端 03集中器 04无线采集器 */
    private String rtuClass;
    /** 终端逻辑地址 */
    private int rtua;
    /** 终端逻辑地址（HEX） */
    private String logicAddress;
    /** 厂商（编号） */
    private String manufacturer;
    /** 高权限密码 */
    private String hiAuthPassword;
    /** 低权限密码 */
    private String loAuthPassword;  
    /**对称密钥版本*/
    private int symmetricKeyVersion;
    /**非对称密钥版本*/
    private int asymmetricKeyVersion;
    /**集中器本身公钥*/
    private String pubKey;
    /**通信方式*/
    private String linkMode;
    
    /** 测量点列表 */
    private Map<String,MeasuredPoint> measuredPoints=new HashMap<String,MeasuredPoint>();
    /** 终端任务列表 */
    private Map<Integer,RtuTask> tasksMap=new HashMap<Integer,RtuTask>();

    /** 参数时间列表 */
    private Map<Integer,Object> paramMap=new HashMap<Integer,Object>();
    
    /** 升级文件报文列表 */
    private Map<Integer,MessageGw> fileMessageMap=new HashMap<Integer,MessageGw>();
    /** 升级文件帧总数列表*/
    private Map<Integer,Integer> messageCountMap=new HashMap<Integer,Integer>();
    /** 升级文件帧当前数列表*/
    private Map<Integer,Integer> currentMessageCountMap=new HashMap<Integer,Integer>();
    /** 升级文件请求列表*/
    private Map<Integer,Object>requestMap=new HashMap<Integer,Object>();
    
    //暂存升级信息
    private Map<String,Object> upgradeParams = new HashMap<String,Object>();
    
    private Date lastRefreshTime;
    
    //分帧的时候要用到
    private int firstFrameSeq; 
    
    public String getRtuType() {
		return rtuType;
	}

	public void setRtuType(String rtuType) {
		this.rtuType = rtuType;
	}
	public String getAnyTaskNum(){
    	String taskNum="";
		for(RtuTask task:tasksMap.values()){
    		taskNum=""+task.getRtuTaskNum();
    		break;
    	}				
		return taskNum;
	}
	/**
     * 根据测量点号取得测量点
     * @param tn 测量点号
     * @return 测量点。如果不存在，则返回 null
     */
    public MeasuredPoint getMeasuredPoint(String tn) {
    	return (MeasuredPoint) measuredPoints.get(tn);
    }
    /**
     * 根据表地址取得测量点
     * @param tnAddr 测量点地址
     * @return 测量点。如果不存在，则返回 null
     */
    public MeasuredPoint getMeasuredPointByTnAddr(String tnAddr) {
    	try{
    		if (measuredPoints!=null){
        		Iterator<Map.Entry<String,MeasuredPoint>> it=measuredPoints.entrySet().iterator();
        		while(it.hasNext()){
        			Map.Entry<String,MeasuredPoint> entry=it.next();
        			MeasuredPoint mp=(MeasuredPoint)entry.getValue();
        			String addr=mp.getTnAddr();
        			String stationNo = mp.getStationNo();
        			addr=strStuff("0",12,addr,"left");
        			stationNo=strStuff("0",12,stationNo,"left");
        			tnAddr=strStuff("0",12,tnAddr,"left");
        			if(addr.equals(tnAddr) || stationNo.equals(tnAddr))
        				return mp;
        		}
            	return (MeasuredPoint) measuredPoints.get(tnAddr);
        	}
        	else
        		return null;
    	}catch(Exception ex){
    		return null;
    	}
    	
    }
    /**
     * 添加测量点
     * @param mp 测量点
     */
    public void addMeasuredPoint(MeasuredPoint mp) {
        measuredPoints.put(mp.getTn(),mp);                
    }
    /**
     * 添加终端任务
     * @param rt 终端任务
     */
    public void addRtuTask(RtuTask rt) {
    	tasksMap.put(new Integer(rt.getRtuTaskNum()), rt);            
    }     
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[id=").append(rtuId)
            .append(", logicAddress=").append(logicAddress)
            .append(", protocol=").append(rtuProtocol)
            .append(", manufacturer=").append(manufacturer).append(", ... ]");
        return sb.toString();
    }
    public RtuTask getRtuTask(String taskNum) {   
    	if (tasksMap == null || taskNum == null) {
            return null;
        }
    	return (RtuTask)tasksMap.get(new Integer(taskNum)); 
    }
    /**
     * 添加参数
     * @param ifseq 帧序号
     * @param ob   参数
     */
    public void addParamToMap(int ifseq, Object ob) {
    	paramMap.put(ifseq, ob);            
    }     
    
    public Object removeParamFromMap(int ifseq){
    	return paramMap.remove(ifseq);
    }

    /**
     * 获得参数
     * @param ifseq 帧序号
     * @return
     */
    public Object getParamFromMap(int ifseq) {   
    	return (Object)paramMap.get(ifseq); 
    }
    /**
     * 添加参数
     * @param key 
     * @param ob   参数
     */
    public void addRequestToMap(int key, Object ob) {
    	requestMap.put(key, ob);            
    }     
    
    public Object removeRequestFromMap(int key){
    	return requestMap.remove(key);
    }

    /**
     * 获得参数
     * @param key 
     * @return
     */
    public Object getRequestFromMap(int key) {   
    	return (Object)requestMap.get(key); 
    }

    /**
     * 添加参数
     * @param   cout 消息序号
     * @param message   消息
     */
    public void addParamToFileMap(int cout, MessageGw message) {
    	fileMessageMap.put(cout, message);            
    }     
    
    public Object removeParamFromFileMap(int cout){
    	return fileMessageMap.remove(cout);
    }
    public void removeAllFromFileMap(){
    	 fileMessageMap.clear();
    }
    
    /**
     * 获得参数
     * @param cout 消息序号
     * @return
     */
    public Object getParamFromFileMap(int cout) {   
    	return (MessageGw)fileMessageMap.get(cout); 
    }
    /**
     * 添加参数
     * @param   key
     * @param messagecount   消息总数
     */
    public void addParamToMessageCountMap(int key, int count) {
    	messageCountMap.put(key, count);            
    }     
    
    public Object removeParamFromMessageCountMap(int key){
    	return messageCountMap.remove(key);
    }

    /**
     * 获得参数
     * @param key 
     * @return
     */
    public int getParamFromMessageCountMap(int key) {   
    	return messageCountMap.get(key); 
    }
    
    /**
     * 添加参数
     * @param   key
     * @param messagecount   消息总数
     */
    public void addParamToCurrentMessageCountMap(int key, int count) {
    	currentMessageCountMap.put(key, count);            
    }     
    
    public Object removeParamFromCurrentMessageCountMap(int key){
    	return currentMessageCountMap.remove(key);
    }

    /**
     * 获得参数
     * @param key 
     * @return
     */
    public int getParamFromCurrentMessageCountMap(int key) {   
    	return currentMessageCountMap.get(key); 
    }
    /**
     * 根据任务号取得终端任务及保存所需信息
     * @param taskNum 任务号
     * @return 终端任务。如果没有对应的任务，则返回 null
     */
    public TaskTemplate getTaskTemplate(String taskNum) {
        if (tasksMap == null || taskNum == null) {
            return null;
        }
        RtuTask rt=(RtuTask)tasksMap.get(new Integer(taskNum)); 
        if(rt !=null){
        	return RtuManage.getInstance().getTaskPlateInCache(rt.getTaskTemplateID());
        	/*MeasuredPoint mp=getMeasuredPoint(rt.getTn());
        	if (mp!=null){
        		tp.setDeptCode(deptCode);        		
        		tp.setTn(mp.getTn());
            	tp.setDataSaveID(mp.getDataSaveID());
            	tp.setCt(mp.getCt());
            	tp.setPt(mp.getPt());
        	}  */      
        	//return tp;
        }                
        return null;
    }
    /**
     * 根据解析得到的数据项列表从终端模版数据项完全匹配得到任务号
     * @param dataCodes 数据项列表
     * @return 对应的终端任务号，否则返回 null
     */
    @SuppressWarnings("unchecked")
	public String getTaskNum(List<String> dataCodes,String rtuType){
    	String taskNum=null,codeStr="";
    	for (int i=0;i<dataCodes.size();i++){
    		if (codeStr.indexOf(dataCodes.get(i))<0)//过滤重复数据项
    			codeStr=codeStr+dataCodes.get(i)+",";
    	}
    	Iterator<?> it=tasksMap.entrySet().iterator();
		while(it.hasNext()){
			int icount=0;
			Map.Entry<Integer,RtuTask> entry=(Map.Entry<Integer,RtuTask>)it.next();
			RtuTask rt=(RtuTask)entry.getValue();
			TaskTemplate ttp=RtuManage.getInstance().getTaskPlateInCache(rt.getTaskTemplateID());
			//if (!rtuType.equals(ttp.getTaskType()))//终端用途和任务类型要一致才匹配
				//continue;
			List<String> codes=ttp.getDataCodes();
			//模板数据项
			//上来的任务数据项
			//如果模板的任务数据项,包含上来的任务数据项
//			if (dataCodes.size()>=codes.size()){
				for (int i=0;i<codes.size();i++){
					//任务模版所有数据项都得在解析后的数据项列表中找到才能确定是否改任务
					if(codeStr.contains(codes.get(i)) && !"0400122000".equals(codes.get(i))){
						//由于任务上来的数据都有0400122000,而很多任务都不加这个字段，只有抄表日数据在模板里有这个字段，所以抄表时间不在判断之列
						icount++;
						break;
					}
//					if (codeStr.indexOf(codes.get(i))<0)
//						break;
//					else 
//						icount++;
				}
				if(icount>0){
					if (taskNum == null) {
						taskNum = "" + entry.getKey();
					} else {
						taskNum += "," + entry.getKey();
					}
				}
//				if (codes.size()==icount){
//					if(taskNum==null){
//						taskNum=""+entry.getKey();
//					}else{
//						taskNum+=","+entry.getKey();						
//					}
//				}
//			}
		}
    	return taskNum;
    	
    }
    
    /**
     * @return Returns the id.
     */
    public String getRtuId() {
        return rtuId;
    }
    /**
     * @param id The id to set.
     */
    public void setRtuId(String rtuId) {
        this.rtuId = rtuId;
    }
      
    /**
	 * @return 返回 rtuProtocol。
	 */
	public String getRtuProtocol() {
		return rtuProtocol;
	}

	/**
	 * @param rtuProtocol 要设置的 rtuProtocol。
	 */
	public void setRtuProtocol(String rtuProtocol) {
		this.rtuProtocol = rtuProtocol;
	}

	/**
     * @return Returns the rtua.
     */
    public int getRtua() {
        return rtua;
    }
    /**
     * @param rtua The rtua to set.
     */
    public void setRtua(int rtua) {
        this.rtua = rtua;
    }
    /**
     * @return Returns the logicAddress.
     */
    public String getLogicAddress() {
        return logicAddress;
    }
    /**
     * @param logicAddress The logicAddress to set.
     */
    public void setLogicAddress(String logicAddress) {
        this.logicAddress = logicAddress;
    }
    
    
    /**
     * @return Returns the manufacturer.
     */
    public String getManufacturer() {
        return manufacturer;
    }
    /**
     * @param manufacturer The manufacturer to set.
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    /**
     * @return Returns the hiAuthPassword.
     */
    public String getHiAuthPassword() {
        return hiAuthPassword;
    }
    /**
     * @param hiAuthPassword The hiAuthPassword to set.
     */
    public void setHiAuthPassword(String hiAuthPassword) {
        this.hiAuthPassword = hiAuthPassword;
    }
    /**
     * @return Returns the loAuthPassword.
     */
    public String getLoAuthPassword() {
        return loAuthPassword;
    }
    /**
     * @param loAuthPassword The loAuthPassword to set.
     */
    public void setLoAuthPassword(String loAuthPassword) {
        this.loAuthPassword = loAuthPassword;
    }
        
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getRtuClass() {
		return rtuClass;
	}

	public void setRtuClass(String rtuClass) {
		this.rtuClass = rtuClass;
	}
	
	//字符补足匹配函数:补足字符；要求长度；输入字符串；补足方向
	public String strStuff(String str,int iLen,String sInput,String sSign){
		String sOutput="";
		try{
		    int iLenStr=sInput.length();
		    if (iLen>iLenStr){//输入字符需要补足
		    for (int i = 0; i < (iLen-iLenStr); i++){
			    if (sSign.equals("left")){//左补足
			      sInput=str+sInput;
			    }
			    else {//右补足
			      sInput=sInput+str;
			    }
			}
		}
		else if (iLen<iLenStr){//输入字符过长需要消去
		  if (sSign.equals("left")){//消去左部
		    sInput = sInput.substring(iLenStr-iLen,iLenStr);
		  }
		  else {//消去右部
			    sInput = sInput.substring(0,iLen);
			  }
			}
			sOutput=sInput;
		}
		catch(Exception e){
		}
		return sOutput;
	}

	public final int getSymmetricKeyVersion() {
		return symmetricKeyVersion;
	}

	public final void setSymmetricKeyVersion(int symmetricKeyVersion) {
		this.symmetricKeyVersion = symmetricKeyVersion;
	}

	public final int getAsymmetricKeyVersion() {
		return asymmetricKeyVersion;
	}

	public final void setAsymmetricKeyVersion(int asymmetricKeyVersion) {
		this.asymmetricKeyVersion = asymmetricKeyVersion;
	}

	public final String getPubKey() {
		return pubKey;
	}

	public final void setPubKey(String pubKey) {
		this.pubKey = pubKey;
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

	public final void setRequestMap(Map<Integer, Object> paramMap) {
		this.requestMap = paramMap;
	}

	public final Map<Integer, Object> getRequestMap() {
		return requestMap;
	}

	public final void setParamMap(Map<Integer, Object> paramMap) {
		this.paramMap = paramMap;
	}

	public final Map<Integer, Object> getParamMap() {
		return paramMap;
	}
	public final Map<Integer, MessageGw> getFilemessageMap() {
		return fileMessageMap;
	}

	public final void setFilemessageMap(Map<Integer, MessageGw> paramMap) {
		this.fileMessageMap = paramMap;
	}

	public final Date getLastRefreshTime() {
		return lastRefreshTime;
	}

	public final void setLastRefreshTime(Date lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}
	public final Map<Integer, Integer> getMessageCountMap() {
		return messageCountMap;
	}

	public final void setMessageCountMap(Map<Integer, Integer> paramMap) {
		this.messageCountMap = paramMap;
	}

	public final Map<Integer, Integer> getCurrentMessageCountMap() {
		return currentMessageCountMap;
	}

	public final void setCurrentMessageCountMap(Map<Integer, Integer> paramMap) {
		this.currentMessageCountMap = paramMap;
	}
	public int getFirstFrameSeq() {
		return firstFrameSeq;
	}

	public void setFirstFrameSeq(int firstFrameSeq) {
		this.firstFrameSeq = firstFrameSeq;
	}

	public String getLinkMode() {
		return linkMode;
	}

	public void setLinkMode(String linkMode) {
		this.linkMode = linkMode;
	}
	
	public boolean isCanRefresh(){
		Date refreshDate = this.getLastRefreshTime();
		boolean canRefresh = false;
		if(refreshDate==null){
			canRefresh = true;
		}else{
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(refreshDate);
			try {
				calendar.add(Calendar.MINUTE, Integer.parseInt(System.getProperty("bp.task.refreshInterval")));
			} catch (NumberFormatException e) {
				calendar.add(Calendar.MINUTE, 1);
			}
			Calendar nowCalendar = Calendar.getInstance();
			nowCalendar.setTime(new Date());
			if(calendar.before(nowCalendar)){
				canRefresh=true;
			}
		}
		return canRefresh;
	}

	public Map<String, Object> getUpgradeParams() {
		return upgradeParams;
	}

	public void setUpgradeParams(Map<String, Object> upgradeParams) {
		this.upgradeParams = upgradeParams;
	}

	public void copy(BizRtu tmpRtu) {
		if(tmpRtu == null)
			return;
		this.upgradeParams = tmpRtu.upgradeParams;
		this.requestMap =tmpRtu.requestMap;
		this.fileMessageMap = tmpRtu.fileMessageMap;
		this.messageCountMap =tmpRtu.messageCountMap;
		this.currentMessageCountMap = tmpRtu.currentMessageCountMap;
		this.fileMessageMap = tmpRtu.fileMessageMap;
	}

	

  			
}
