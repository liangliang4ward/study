package com.hx.ansi.ansiElements.ansiElements.loadTable;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-4-10 ÉÏÎç10:09:14
 * @version 1.0 
 */

public class Table63 extends Table{
	public int LP_SET_STATUS_FLAGS;
	public int NBR_VALID_BLOCKS ;
	public int LAST_BLOCK_ELEMENT ;
	public int LAST_BLOCK_SEQ_NBR;
	public int NBR_UNREAD_BLOCKS ;
	public int NBR_VALID_INT;
	
	
	
	@Override
	public void decode() {
		
	}
	@Override
	public void decode(String data){
		LP_SET_STATUS_FLAGS=Integer.parseInt(data.substring(0, 2), 16);
		NBR_VALID_BLOCKS=Integer.parseInt(data.substring(2, 6), 16);
		LAST_BLOCK_ELEMENT=Integer.parseInt(data.substring(6, 10), 16);
		LAST_BLOCK_SEQ_NBR=Integer.parseInt(data.substring(10, 18), 16);
		NBR_UNREAD_BLOCKS=Integer.parseInt(data.substring(18, 22), 16);
		NBR_VALID_INT=Integer.parseInt(data.substring(22, 26), 16);
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		//25010001000100000000000400
		Table63 t=new Table63();
		t.decode("25010001000100000000000400");
		
		
		
	} 


}
