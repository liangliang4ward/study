package com.hx.ansi.ansiElements.ansiElements.securityTable;

import cn.hexing.fk.utils.HexDump;

import com.hx.ansi.ansiElements.ansiElements.Table;
import com.hx.ansi.parse.AnsiDataSwitch;

/** 
 * @Description  安全相关限制表
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-10 上午10:06:10
 * @version 1.0 
 */

public class Table41 extends Table{
	public int NBR_PASSWORDS;//密码的个数
	public int PASSWORD_LEN ;//密码的长度
	public int NBR_KEYS ;//密钥的个数
	public int KEY_LEN ;//密钥的长度
	public int NBR_PERM_USED;//用户自定义权限的table或者procedure个数,见table44。
	
	@Override
	public void decode() {
		
	}
	@Override
	public void decode(String data){
		byte []b=new byte[1024];
		b=HexDump.toArray(data);
		NBR_PASSWORDS=AnsiDataSwitch.parseBytetoInt(b[0]);
		PASSWORD_LEN=AnsiDataSwitch.parseBytetoInt(b[1]);
		NBR_KEYS=AnsiDataSwitch.parseBytetoInt(b[1]);
		KEY_LEN=AnsiDataSwitch.parseBytetoInt(b[1]);
		NBR_PERM_USED=Integer.parseInt(data.substring(8, 12), 16);
	}
	@Override
	public void encode() {
		// TODO Auto-generated method stub
		
	}

	


}
