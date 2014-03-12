package com.hx.ansi.ansiElements.ansiElements.dataTable;

import com.hx.ansi.ansiElements.ansiElements.Table;

/**
 * Table 13 需量控制表
 * 需量相关参数的设置，可读写。
 */
public class Table13 extends Table{
	//对于海兴表计。前八个字节为0，后面两个字节定义如下：
	private byte  slipCycle;//滑差式区间周期  1字节
	private byte  silpNumber;//滑差数 1字节
	private byte  intervalCycle;//区间式区间周期 2个字节
	
	
	/**
	 * 获取滑差式计算需量的周期
	 * @return
	 */
	public byte getSlipCycle() {
		return slipCycle;
	}
	/**
	 * 设置滑差式计算需量的周期
	 * @param slipCycle
	 */
	public void setSlipCycle(byte slipCycle) {
		this.slipCycle = slipCycle;
	}
	/**
	 * 获取滑差式计算需量的滑差数
	 * @return
	 */
	public byte getSilpNumber() {
		return silpNumber;
	}
	/**
	 * 设置滑差式计算需量的滑差数
	 * @param silpNumber
	 */
	public void setSilpNumber(byte silpNumber) {
		this.silpNumber = silpNumber;
	}
	/**
	 * 获取区间式计算需量的需量周期
	 * @return
	 */
	public byte getIntervalCycle() {
		return intervalCycle;
	}
	/**
	 * 设置区间式计算需量的需量周期
	 * @param intervalCycle
	 */
	public void setIntervalCycle(byte intervalCycle) {
		this.intervalCycle = intervalCycle;
	}
	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}
	public void decode(String data){
		
		
		
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
}
