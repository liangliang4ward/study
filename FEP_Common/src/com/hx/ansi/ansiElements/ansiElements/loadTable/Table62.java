package com.hx.ansi.ansiElements.ansiElements.loadTable;

import java.util.ArrayList;
import java.util.List;

import com.hx.ansi.ansiElements.AnsiContext;
import com.hx.ansi.ansiElements.ansiElements.Table;

/** 
 * @Description  xxxxx
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time£º2013-4-10 ÉÏÎç10:09:08
 * @version 1.0 
 */

public class Table62 extends Table{
	public List<LP_SEL_SETx> LP_SEL_SETx;
	public int INT_FMT_CDEx;
	public String SCALARS_SETx;
	public String DIVISORS_SETx;
	
	
	@Override
	public void decode() {
		
	}
	@Override
	public void decode(String data){

	
	}
	
	public void decode(AnsiContext context,String data){
		int i=context.table61.NBR_CHNS_SETx;//load profile x
		LP_SEL_SETx set[]=new LP_SEL_SETx[i];
		LP_SEL_SETx=new ArrayList<LP_SEL_SETx>();
		for(int j=0;j<i;j++){
			set[j]=new LP_SEL_SETx();
			set[j].decode(data.substring(j*6, 6+j*6));
			LP_SEL_SETx.add(set[j]);
		}
		INT_FMT_CDEx=Integer.parseInt(data.substring(6*i, 6*i+2), 16);
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}
	public class LP_SEL_SETx{
		public int associated ;
		public int index;
		public String dataSource;
		public void decode(String data){
			associated=Integer.parseInt(data.substring(0, 2), 16);
			index=Integer.parseInt(data.substring(2, 4), 16);
			dataSource=data.substring(4, 6);
		}
	}
	
	
	
	public static void main(String[] args) {
		//6020A803020104BE1928178115801340003E000D0000FF0001FF0002FF0003FF40BE
		//0000FF0001FF0002FF0003FF40
		//00 00 FF
		//00 01 FF 
		//00 02 FF 
		//00 03 FF  40

		Table62 t=new Table62();
		t.decode("0000FF0001FF0002FF0003FF40");
		
	}

}
