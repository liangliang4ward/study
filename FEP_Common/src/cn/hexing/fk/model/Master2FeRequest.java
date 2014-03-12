/**
 * Master-station send request object to all FE.
 */
package cn.hexing.fk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Master2FeRequest implements Serializable {
	private static final long serialVersionUID = 6678009282987527793L;
	
	public static final int CMD_UPDATE_SIM = 1;
	public static final int CMD_ENABLE_HEART_LOG = 2;
	public static final int CMD_DISABLE_HEART_LOG = 3;
	public static final int CMD_FE_PROFILE = 4;
	public static final int CMD_ENABLE_ZJUPDATE = 5;
	public static final int CMD_DISABLE_ZJUPDATE = 6;
	public static final int CMD_CLEAR_ZJUPDATE = 7;
	public static final int CMD_QUERY_RTU_HEARTBEAT = 8;
	public static final int CMD_HEART_SWITCH_LOG=9; //心跳开关
	public static final int CMD_GET_SAVEHEART_LIST=10;
	public static final int CMD_GET_IS_ALL_SAVE_HEART=11;//是否所有FE都保存报文
	
	private int command = 0;
	private int cmdSeq = 0;
	private List<Integer> rtuaList = null;
	private List<String> strList = null;  //Used by update SIM
	private List<String> logicAddrList = null;
	private int result = -1;		//FE reply result of this request's execution. 0 if successfully. other is error-code
	private boolean needReply = false;
	
	private boolean isSaveHeartBeart = false;
	
	
	public boolean isNeedReply(){
		return needReply;
	}
	
	public void updateSims(List<Integer>rtus ,List<String> sims){
		command = CMD_UPDATE_SIM;
		rtuaList = rtus;
		strList = sims;
	}
	
	public void getSaveHeart2DbList(){
		command=CMD_GET_SAVEHEART_LIST;
		needReply = true;
	}
	public void isAllFeSaveHeart() {
		command =CMD_GET_IS_ALL_SAVE_HEART;
		needReply=true;
	}
	public void saveHeart2Db(List<String>rtus){
		command = CMD_ENABLE_HEART_LOG;
		logicAddrList = rtus;
	}
	public void saveHeart2Db(boolean isSaveHeartBeart){
		command = CMD_HEART_SWITCH_LOG;
		this.isSaveHeartBeart = isSaveHeartBeart;
	}
	public void unsaveHeart2Db(List<String>rtus){
		command = CMD_DISABLE_HEART_LOG;
		logicAddrList = rtus;
	}
	
	public void gatherFeProfile(){
		command = CMD_FE_PROFILE;
		needReply = true;
	}
	
	public void enableUpdate(List<Integer> list){
		command = CMD_ENABLE_ZJUPDATE;
		rtuaList = list;
	}
	
	public void disableUpdate(List<Integer> list ){
		command = CMD_DISABLE_ZJUPDATE;
		rtuaList = list;
	}
	
	public void clearUpdate(){
		command = CMD_CLEAR_ZJUPDATE;
	}
	
	public void queryHeartBeatInfo(int rtua){
		command = CMD_QUERY_RTU_HEARTBEAT;
		needReply = true;
		rtuaList = new ArrayList<Integer>();
		rtuaList.add(rtua);
	}
	
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public List<Integer> getRtuaList() {
		return rtuaList;
	}
	public void setRtuaList(List<Integer> rtuaList) {
		this.rtuaList = rtuaList;
	}
	public List<String> getStrList() {
		return strList;
	}
	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getCmdSeq() {
		return cmdSeq;
	}

	public void setCmdSeq(int cmdSeq) {
		this.cmdSeq = cmdSeq;
	}
	
	public String desc(){
		StringBuilder sb = new StringBuilder();
		sb.append("command = ").append(command);
		sb.append(",seq=").append(cmdSeq);
		sb.append(",result=").append(result);
		sb.append(", strList=").append(strList);
		return sb.toString();
	}

	public boolean isSaveHeartBeart() {
		return isSaveHeartBeart;
	}

	public List<String> getLogicAddrList() {
		return logicAddrList;
	}

	public void setLogicAddrList(List<String> logicAddrList) {
		this.logicAddrList = logicAddrList;
	}

}
