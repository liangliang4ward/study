package cn.hexing.fk.model;

public class RtuCmdItem {
	/** 命令ID */
    private long cmdId;
    /** 报文数量 */
    private int bwsl;
    /** 自动装接标志 */
    private int zdzjbz;
    
	public int getBwsl() {
		return bwsl;
	}
	public void setBwsl(int bwsl) {
		this.bwsl = bwsl;
	}
	public long getCmdId() {
		return cmdId;
	}
	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}
	public int getZdzjbz() {
		return zdzjbz;
	}
	public void setZdzjbz(int zdzjbz) {
		this.zdzjbz = zdzjbz;
	}

}
