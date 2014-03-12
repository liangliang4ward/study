package cn.hexing.fk.model;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hx.ansi.model.AnsiMeterRtu;



/**
 * 终端缓存
 * 
 */
public class RtuManage {	
	private static final Log log = LogFactory.getLog(RtuManage.class);
	/** 单例 */
	private static RtuManage instance;

	/** 终端的缺省规约:01浙江规约 */
	private static String defaultRtuProtocol="01";
	/** 瞬时有功阀值和平均有功与瞬时有功阀值:作为任务上报负荷数据批量保存传入参数*/
	private static SysConfig sysConfig;
	
	/** 终端逻辑地址与通讯前置机终端对照表[rtua - ComRtu] */
	private static Map<Integer,ComRtu> comRtuMap=new HashMap<Integer,ComRtu>(102400);
	
	/** 通过终端ID查找业务处理器终端逻辑地址[rtuId -> rtua] */
	private static Map<String,Integer> bizRtuaIdMap=new HashMap<String,Integer>(102400);
	/** 终端逻辑地址与业务处理器终端对照表[rtua - BusRtu] */
	private static Map<Integer,BizRtu> bizRtuMap=new HashMap<Integer,BizRtu>(102400);
	/** 任务模版ID与任务模版对照表[taskPlateID - TaskPlate] */
	private static Map<Integer,TaskTemplate> taskPlateMap=new HashMap<Integer,TaskTemplate>();
	/** 告警编码与告警编码对象之间的对照表[code - RtuAlertCode] */
    private static Map<Integer,RtuAlertCode> alertCodeMap=new HashMap<Integer,RtuAlertCode>();
    /** 任务保存数据库表影射信息对照表[code - TaskSaveInfo] */
    private static Map<String,TaskDbConfig> taskDbConfigMap=new HashMap<String,TaskDbConfig>();
    /** 终端远程升级登记表[rtuaInt - String] */
    private static Map<Integer,Boolean> rtuRemoteUpdateMap=new HashMap<Integer,Boolean>();
    
	private static Map<String,DlmsMeterRtu> dlmsMeterRtuMap = new HashMap<String, DlmsMeterRtu>();
	private static Map<String,AnsiMeterRtu> ansiMeterRtuMap = new HashMap<String, AnsiMeterRtu>();
	/** meterId->logicAddress*/
	private static Map<String,String> dlmsMeterIdMap = new HashMap<String, String>(102400);
	/** meterId->logicAddress*/
	private static Map<String,String> ansiMeterIdMap = new HashMap<String, String>(102400);
	/**
	 * 构造一个终端缓存
	 */
	private RtuManage() {		
		// 加载缓存
		//init();				
	}
	
	/**
	 * 取得终端缓存对象。若缓存尚未初始化，则在初始化后返回
	 * 
	 * @return 终端缓存对象
	 */
	public static RtuManage getInstance() {
		if (instance == null) {
			synchronized (RtuManage.class) {
				if (instance == null) {
					instance = new RtuManage();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 检查终端的通讯规约
	 * 
	 * @param rtu
	 *            终端
	 */
	private static void checkProtocol(BizRtu rtu) {
		if (rtu.getRtuProtocol() == null) {
			rtu.setRtuProtocol(defaultRtuProtocol);
		}
	}
	
	
	/**
	 * 直接在缓存中查找通讯终端档案。不尝试从数据库中查找
	 * 
	 * @param rtua
	 *            终端逻辑地址
	 * @return 匹配的终端。若不存在，则返回 null
	 */
	public ComRtu getComRtuInCache(int rtua) {
		return (ComRtu) comRtuMap.get(new Integer(rtua));
	}
	
	public Collection<ComRtu> getAllComRtu(){
		return comRtuMap.values();
	}
	
	public Map<Integer,ComRtu> getComRtuMap(){
		return comRtuMap;
	}
	/**
	 * 直接在缓存中查找业务终端档案。不尝试从数据库中查找
	 * 
	 * @param rtua
	 *            终端逻辑地址
	 * @return 匹配的终端。若不存在，则返回 null
	 */
	public BizRtu getBizRtuInCache(int rtua) {
		return (BizRtu) bizRtuMap.get(new Integer(rtua));
	}
	/**
	 * 直接在缓存中查找dlms表计档案
	 * @param logicAddress  逻辑地址
	 * @return
	 */
	public DlmsMeterRtu getDlmsMeterRtu(String logicAddress)
	{
		return dlmsMeterRtuMap.get(logicAddress);
	}
	/**
	 * 通过表计局号找到Dlms表
	 */
	public DlmsMeterRtu getDlmsMeterRtuById(String rtuId){
		return dlmsMeterRtuMap.get(dlmsMeterIdMap.get(rtuId));
	}
	/**
	 * 直接在缓存中查找ansi表计档案
	 * @param logicAddress  逻辑地址
	 * @return
	 */
	public AnsiMeterRtu getAnsiMeterRtu(String logicAddress)
	{
		return ansiMeterRtuMap.get(logicAddress);
	}
	/**
	 * 通过表计局号找到Ansi表
	 */
	public AnsiMeterRtu getAnsiMeterRtuById(String rtuId){
		return ansiMeterRtuMap.get(ansiMeterIdMap.get(rtuId));
	}
	/**
	 * 直接在缓存中查找业务终端档案。不尝试从数据库中查找
	 * 
	 * @param rtuId
	 *            终端局号
	 * @return 匹配的终端。若不存在，则返回 null
	 */
	public BizRtu getBizRtuInCache(String rtuId) {
		return (BizRtu) bizRtuMap.get(bizRtuaIdMap.get(rtuId));
	}
	/**
	 * 更新缓存中的业务处理器终端档案
	 * 
	 * @param BizRtu
	 */
	public synchronized void putBizRtuToCache(BizRtu bizRtu) {
		try {				
			checkProtocol(bizRtu);
			bizRtu.setRtua((int)Long.parseLong(bizRtu.getLogicAddress(), 16));
			bizRtuaIdMap.put(bizRtu.getRtuId(), new Integer(bizRtu.getRtua()));		
			bizRtuMap.put(new Integer(bizRtu.getRtua()), bizRtu);		
		} catch (Exception ex) {
			log.debug("Error to put BizRtu: " + bizRtu.toString());
		}
	}
	
	public synchronized void putDlmsMeterRtuToCache(DlmsMeterRtu meterRtu){
		dlmsMeterIdMap.put(meterRtu.getMeterId(), meterRtu.getLogicAddress());
		dlmsMeterRtuMap.put(meterRtu.getLogicAddress(), meterRtu);
	}
	public synchronized void putAnsiMeterRtuToCache(AnsiMeterRtu meterRtu){
		ansiMeterIdMap.put(meterRtu.getMeterId(), meterRtu.getLogicAddress());
		ansiMeterRtuMap.put(meterRtu.getLogicAddress(), meterRtu);
	}
	/**
	 * 更新缓存中的通讯前置机终端档案
	 * 
	 * @param BizRtu
	 */
	public synchronized void putComRtuToCache(ComRtu comRtu) {
		try {				
			comRtu.setRtua((int)Long.parseLong(comRtu.getLogicAddress(), 16));				
			comRtuMap.put(new Integer(comRtu.getRtua()), comRtu);		
		} catch (Exception ex) {
			log.debug("Error to put ComRtu: " + comRtu.toString(), ex);
		}
	}
	/**
	 * 对于Dlms表，将测量点放在缓存中。
	 * @param mp
	 */
	public synchronized void putMeasurePointToDlmsCache(MeasuredPoint mp){
		DlmsMeterRtu dlmsRtu = getDlmsMeterRtuById(mp.getRtuId());
		if(dlmsRtu ==null){
//			log.debug("Can't find dlmsRtu when loading MeasuredPoint: "
//					+ mp.toString());
			return;
		}
		dlmsRtu.addMeasuredPoint(mp);
		
	}
	/**
	 * 对于ansi表，将测量点放在缓存中。
	 * @param mp
	 */
	public synchronized void putMeasurePointToAnsiCache(MeasuredPoint mp){
		AnsiMeterRtu ansiRtu = getAnsiMeterRtuById(mp.getRtuId());
		if(ansiRtu ==null){
//			log.debug("Can't find dlmsRtu when loading MeasuredPoint: "
//					+ mp.toString());
			return;
		}
		ansiRtu.addMeasuredPoint(mp);
		
	}
	/**
	 * 更新缓存中的业务处理器终端测量点档案
	 * 
	 * @param MeasuredPoint
	 */
	public synchronized void putMeasuredPointToCache(MeasuredPoint mp) {
		try {				
			BizRtu bizRtu = getBizRtuInCache(mp.getRtuId());
			if (bizRtu == null) {
//				log.debug("Can't find busRtu when loading MeasuredPoint: "
//						+ mp.toString());
				return;
			}
			bizRtu.addMeasuredPoint(mp);	
		} catch (Exception ex) {
			log.debug("Error to put MeasuredPoint: " + mp.toString(), ex);
		}
	}
	/**
	 * 将终端任务和主站任务存储在缓存中
	 * @param rt
	 */
	public synchronized void putTaskToDlmsCache(RtuTask rt) {
		DlmsMeterRtu dlmsRtu = getDlmsMeterRtuById(rt.getRtuId());

		if (dlmsRtu == null) {
			return;
		}
		dlmsRtu.addRtuTask(rt);
	}
	/**
	 * 将终端任务和主站任务存储在缓存中ANSI
	 * @param rt
	 */
	public synchronized void putTaskToAnsiCache(RtuTask rt) {
		AnsiMeterRtu dlmsRtu = getAnsiMeterRtuById(rt.getRtuId());

		if (dlmsRtu == null) {
			return;
		}
		dlmsRtu.addRtuTask(rt);
	}
	/**
	 * 更新缓存中的业务处理器终端测量点档案
	 * 
	 * @param RtuTask
	 */
	public synchronized void putRtuTaskToCache(RtuTask rt) {
		try {				
			BizRtu bizRtu = getBizRtuInCache(rt.getRtuId());
			if (bizRtu == null) {
				return;
			}
			bizRtu.addRtuTask(rt);	
		} catch (Exception ex) {
			log.debug("Error to put RtuTask: " + rt.toString(), ex);
		}
	}
	/**
	 * 直接在缓存中查找任务模版信息。不尝试从数据库中查找
	 * 
	 * @param rtua
	 *            终端逻辑地址
	 * @return 匹配的终端。若不存在，则返回 null
	 */
	public TaskTemplate getTaskPlateInCache(String taskPlateID) {
		return (TaskTemplate) taskPlateMap.get(new Integer(taskPlateID));
	}
	/**
	 * 直接在缓存中查找异常携带数据项信息。不尝试从数据库中查找
	 * 
	 * @param rtua
	 *            终端逻辑地址
	 * @return 匹配的终端。若不存在，则返回 null
	 */
	public RtuAlertCode getRtuAlertCode(int code) {
		return (RtuAlertCode) alertCodeMap.get(new Integer(code));
	}
	/**
	 * 更新缓存中的业务处理器任务模版档案
	 * 
	 * @param TaskTemplate
	 */
	public synchronized void putTaskTemplateToCache(TaskTemplate tp) {
		try {				
			taskPlateMap.put(new Integer(tp.getTaskTemplateID()), tp);
		} catch (Exception ex) {
			log.debug("Error to put TaskPlate: " + tp.toString(), ex);
		}
	}
	/**
	 * 更新缓存中的业务处理器任务模版数据项档案
	 * 
	 * @param TaskTemplateItem
	 */
	public synchronized void putTaskTemplateItemToCache(TaskTemplateItem tpi) {
		try {							
			TaskTemplate tp = (TaskTemplate)taskPlateMap.get(new Integer(tpi.getTaskTemplateID()));
			if (tp == null) {
				log.debug("Can't find TaskPlate when loading TaskPlateItem: "
								+ tpi.toString());
				return;
			}
			tp.addDataCode(tpi.getCode());
		} catch (Exception ex) {
			log.debug("Error to put TaskPlateItem: " + tpi.toString(), ex);
		}
	}	
	/**
	 * 直接在缓存中查找任务保存数据库表影射信息。不尝试从数据库中查找
	 * 
	 * @param rtua
	 *            终端逻辑地址
	 * @return 匹配的终端。若不存在，则返回 null
	 */
	public TaskDbConfig getTaskDbConfigInCache(String key) {
		return (TaskDbConfig) taskDbConfigMap.get(key);
	}
	/**
	 * 更新缓存中的业务处理器任务保存映射信息档案
	 * 
	 * @param TaskDbConfig
	 */
	public synchronized void putTaskDbConfigToCache(TaskDbConfig tsi) {
		try {							
			tsi.setDbConfigStr(tsi.getDbConfigStr());
			taskDbConfigMap.put(tsi.getCode(), tsi);
		} catch (Exception ex) {
			log.debug("Error to put TaskDbConfig: " + tsi.toString(), ex);
		}
	}
	
	/**
	 * 更新缓存中的业务处理器异常携带数据项档案
	 * 
	 * @param RtuAlertCode
	 */
	public synchronized void putAlertCodeToCache(RtuAlertCode rac) {
		try {							
			alertCodeMap.put(new Integer(Integer.parseInt(rac.getCode(), 16)), rac);
		} catch (Exception ex) {
			//log.debug("Error to put RtuAlertCode: " + rac.toString(), ex);
		}
	}
	/**
	 * 登记远程升级终端逻辑地址
	 * 
	 * @param rtua
	 */
	public synchronized void putRemoteUpateRtuaToCache(String rtua) {
		try {							
			rtuRemoteUpdateMap.put(new Integer((int)Long.parseLong(rtua, 16)), true);
		} catch (Exception ex) {
			log.debug("Error to put RemoteUpateRtua: " + rtua, ex);
		}
	}
	/**
	 * 获取终端地址是否允许升级标志
	 * 
	 * @param rtua
	 */
	public boolean getRemoteUpateRtuaTag(String rtua) {
		if (rtuRemoteUpdateMap.get(new Integer((int)Long.parseLong(rtua, 16)))!=null)
			return rtuRemoteUpdateMap.get(new Integer((int)Long.parseLong(rtua, 16)));
		else
			return false;
	}
	/**
	 * 获取终端地址是否允许升级标志
	 * 
	 * @param rtua
	 */
	public boolean getRemoteUpateRtuaTag(int rtua) {
		if (rtuRemoteUpdateMap.get(new Integer(rtua))!=null)
			return rtuRemoteUpdateMap.get(new Integer(rtua));
		else
			return false;
	}
	/**
	 * 登记远程升级终端逻辑地址
	 * 
	 * @param rtua
	 */
	public synchronized void clearRtuRemoteUpdateMap() {						
		rtuRemoteUpdateMap.clear();		
	}
	public SysConfig getSysConfig() {
		return sysConfig;
	}

	public void setSysConfig(SysConfig myConfig) {
		sysConfig = myConfig;
	}

}
