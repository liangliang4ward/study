package cn.hexing;

/**
 * 
 * 
 * 项目名称：ThreadDeadLock
 * 类名称：Resource
 * 类描述：资源类，用于代表线程竞争的数据资源
 * 创建人：Defonds
 * 创建时间：2010-1-26 下午02:01:16
 * 修改人：Defonds
 * 修改时间：2010-1-26 下午02:01:16
 * 修改备注：
 * @version 
 *
 */
public class Resource {
	
	private int value;//资源的属性

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
