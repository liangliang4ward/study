package cn.hexing.fk.model;

public class RtuSetValue {
	/** 命令ID */
    private long cmdId;
    /** 测量点号 */
    private int cldh;
    /** 数据项状态SJX1：ZT1，SJX2：ZT2……，ZT为01失败（默认），00成功 */
    private String sjxzt;
    
	public long getCmdId() {
		return cmdId;
	}
	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}
	public int getCldh() {
		return cldh;
	}
	public void setCldh(int cldh) {
		this.cldh = cldh;
	}
	public String getSjxzt() {
		return sjxzt;
	}
	public void setSjxzt(String sjxzt) {
		this.sjxzt = sjxzt;
	}
	
    
	
}
