package cn.hexing.fk.model;
/**
 * 数据库表系统配置信息
 * 目前只用到瞬时有功阀值(BJ10)和平均有功与瞬时有功阀值(BJ11)
 */
public class SysConfig {
	/** 系统配置项值*/
    private String pzz;
    private String bj10;
    private String bj11;

	public String getPzz() {
		return pzz;
	}
	public void setPzz(String pzz) {
		this.pzz = pzz;
	}
	public String getBj10() {
		return bj10;
	}
	public void setBj10(String bj10) {
		this.bj10 = bj10;
	}
	public String getBj11() {
		return bj11;
	}
	public void setBj11(String bj11) {
		this.bj11 = bj11;
	}

}
