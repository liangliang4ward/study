package com.hx.ansi.ansiElements;

import java.io.Serializable;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-6-21 下午03:12:37
 * @version 1.0 
 */

public class AnsiTaskInf implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1235687L;

	public String  taskCodes;//数据项
	public int codeCount;//数据项数量
	public int interval;//时间间隔
	public String  intervalUint;//时间间隔单位
	public int taskNo;//任务号
	
}
