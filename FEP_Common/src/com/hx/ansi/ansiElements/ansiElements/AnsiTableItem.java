package com.hx.ansi.ansiElements.ansiElements;

import java.io.Serializable;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-5-14 ÏÂÎç08:36:17
 * @version 1.0 
 */

public class AnsiTableItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 125488555L;
	public  int table;
	
	//for reading load profile
	public boolean readLatestDate=false;
	public boolean readLoadProfileData=false;
}
