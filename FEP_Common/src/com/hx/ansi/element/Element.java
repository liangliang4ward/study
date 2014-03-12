package com.hx.ansi.element;
/** 
 * @Description  元素编解码接口
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-3-20 下午06:33:05
 * @version 1.0 
 */

public interface Element {
	
	/** 元素组码*/
	public void encode();
	/** 元素解码*/
	public void decode();
	
}
