package com.hx.ansi.ansiElements.ansiElements.loadTable;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description    Table 60 负荷限制表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:08:43
 * @version 1.0 
 */

public class Table60 extends Table {
	public int LP_MEMORY_LEN;//负荷数据的最大字节数
	public int LP_FLAGS;
	public int LP_FMATS;
	public int NBR_BLKS_SETx ;//表计支持Load profile set x时才有参数NBR_BLKS_SETx。该参数指Load profile set x支持的最大block数
	public int NBR_BLK_INTS_SETx ;
	public int NBR_CHNS_SETx ;
	public int MAX_INT_TIME_SETx ;
	
	
	
	@Override
	public void decode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decode(String data) {
		LP_MEMORY_LEN=Integer.parseInt(data.substring(0,8), 16);
		LP_FLAGS=Integer.parseInt(data.substring(8,12), 16);
		LP_FMATS=Integer.parseInt(data.substring(12,14), 16);
		NBR_BLKS_SETx=Integer.parseInt(data.substring(14,18), 16);
		NBR_BLK_INTS_SETx=Integer.parseInt(data.substring(18,22), 16);
		NBR_CHNS_SETx=Integer.parseInt(data.substring(22,24), 16);
		MAX_INT_TIME_SETx=Integer.parseInt(data.substring(24,26), 16);
	}
	public static void main(String[] args) {
		//1027000000004002001800043C
		Table60 t60=new Table60();
		t60.decode("1027000000004002001800043C");
		
		
		
		
	}
}
