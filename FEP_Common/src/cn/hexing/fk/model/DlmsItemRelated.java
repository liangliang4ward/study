package cn.hexing.fk.model;
/**
 * Dlms数据项与 数据项内码的关系
 * @author Administrator
 *
 */
public class DlmsItemRelated {
	
	/**
	 * classId+obis+attr;
	 */
	String attribute;
	
	String code;

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
