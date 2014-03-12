package com.hx.ansi.ansiElements.ansiElements;

import java.io.Serializable;
import java.util.Date;

/**
 * 主站操作命令结果
 */
public class AnsiCommandResult implements Serializable {

    private static final long serialVersionUID = 278266439182006204L;
    
    /** 参数设置结果：设置成功 */
    public static final int STATUS_SUCCESS = 0;
    /** 参数设置结果：不确定 */
    public static final int STATUS_AMBIGUOUS = 1;
    /** 参数设置结果：设置失败 */
    public static final int STATUS_FAILED = 2;
    
    /** 命令ID */
    private Long commandId;
    /** 测量点号 */
    private String tn;
    /**电表通信地址*/
    private String meterAddr;
    /** 数据项代码 */
    private String code;
    /** 数据值 */
    private String value;
    /** 数据时间/告警时间 */
    private Date time;
    /** 编程时间 */
    private Date programTime;
    /** 通道 */
    private String channel;
    /** 参数设置结果 */
    private int status;
    /**终端逻辑地址*/
    private String logicAddr;
    /**消息总数 */
    private int messageCount;
    /**当前消息 */
    private int currentMessage;
    /** 升级任务ID*/
    private long softUpgradeID;
    /**
     * @return Returns the commandId.
     */
    public Long getCommandId() {
        return commandId;
    }
    /**
     * @param commandId The commandId to set.
     */
    public void setCommandId(Long commandId) {
        this.commandId = commandId;
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
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * @return Returns the time.
     */
    public Date getTime() {
        return time;
    }
    /**
     * @param time The time to set.
     */
    public void setTime(Date time) {
        this.time = time;
    }
    /**
     * @return Returns the programTime.
     */
    public Date getProgramTime() {
        return programTime;
    }
    /**
     * @param programTime The programTime to set.
     */
    public void setProgramTime(Date programTime) {
        this.programTime = programTime;
    }
    /**
     * @return Returns the channel.
     */
    public String getChannel() {
        return channel;
    }
    /**
     * @param channel The channel to set.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
    /**
     * @return Returns the status.
     */
    public int getStatus() {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(int status) {
        this.status = status;
    }
	public String getMeterAddr() {
		return meterAddr;
	}
	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}
	public String getLogicAddr() {
		return logicAddr;
	}
	public void setLogicAddr(String logicAddr) {
		this.logicAddr = logicAddr;
	}
	public int getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}
	public int getCurrentMessage() {
		return currentMessage;
	}
	public void setCurrentMessage(int currentMessage) {
		this.currentMessage = currentMessage;
	}
	public long getSoftUpgradeID() {
		return softUpgradeID;
	}
	public void setSoftUpgradeID(long softUpgradeID) {
		this.softUpgradeID = softUpgradeID;
	}   
}
