package com.hx.ansi.ansiElements;


/** 
 * @Description  索引查找
 * @author  Rolinbor
 * @Copyright 2013 hexing Inc. All rights reserved
 * @time：2013-4-18 上午08:52:56
 * @version 1.0 
 */

public class SeekIndex {
	
	private static SeekIndex instance=null;
	public static SeekIndex getInstance(){
		if(instance==null){
			instance=new SeekIndex();
		}
		return instance;
	}

	/*设计思路：(当前电量和需量)
		1.对于主站发过来的读取数据的请求，主站发送过来的类似数据项编码的东西也就是4个字节（因为table12里面的每个数据项是以4个字节表示的）
		2.前置机收到主站读取请求先根据数据项从table的map中获取索引，根据索引从table16中获取数据源定义表
		3.table21 获取是否记录需量复位次数，数据寄存器的个数费率数等信息
		4.table22 获取电量和需量在12中的索引
		5.table23获取电量和需量对应table22中的数据项
	*/
//	/**
//	 * 电量和需量读取
//	 */
//	public List<AnsiRequest>  powerAndDemand(AnsiRequest req, AnsiContext context){
//		//对于从req里面获取了多个数据项的情况，需要分多个请求下发
//		List<AnsiRequest> requests=new ArrayList<AnsiRequest>();
//		String[] params =req.getDataItems().split("#");
//		for(String param:params){
//			Long lparam=Long.parseLong(param,16);//从请求中回去数据项（也有可能是一个包）
//			int key12=context.table12.dataItemMap.get(lparam);//从table 12中获取要读取数据项的索引
//			if(key12==0) {
//				System.out.println("未定义的数据项。。。");
//				continue;
//			}
//			int key23=context.table22.selectMap.get(key12);
//			if(key23==0){
//				System.out.println("表计不支持此数据项。。。");
//				continue;
//			}
//			String  offSet=HexDump.toHex(key23).substring(2);
//			String  count=HexDump.toHex(getOffSet(context)).substring(4);
//			req.setFull(false);
//			req.setServiceTag("3F");
//			req.setOffset(offSet);
//			req.setCount(count);
//			requests.add(req);
//		}
//		return requests;
//	}
//	
//	//结算数据和当前电量类似，只是最后一步table不同，所以这里采用powerAndDemand来组索引读取数据的request
////	public List<AnsiRequest>  powerAndDemandFreeze(AnsiRequest req, AnsiContext context){
////		
////	}
//	
//	/**
//	 * 读取实时量
//	 */
//	public List<AnsiRequest>  currentData(AnsiRequest req, AnsiContext context){
//
//		//对于从req里面获取了多个数据项的情况，需要分多个请求下发
//		List<AnsiRequest> requests=new ArrayList<AnsiRequest>();
//		String[] params =req.getDataItems().split("#");
//		for(String param:params){
//			Long lparam=Long.parseLong(param,16);//从请求中回去数据项（也有可能是一个包）
//			int key12=context.table12.dataItemMap.get(lparam);//从table 12中获取要读取数据项的索引
//			if(key12==0) {
//				System.out.println("未定义的数据项。。。");
//				continue;
//			}
//			int key23=context.table27.selectMap.get(key12);
//			if(key23==0){
//				System.out.println("表计不支持此数据项。。。");
//				continue;
//			}
//			String  offSet=HexDump.toHex(key23).substring(2);
//			String  count=HexDump.toHex(getOffSet(context)).substring(4);
//			req.setFull(false);
//			req.setServiceTag("3F");
//			req.setOffset(offSet);
//			req.setCount(count);
//			requests.add(req);
//		}
//		return requests;
//	
//		
//		
//		
//	}
//	
//	
	
	
	/**
	 * 获取没一个数据项字节数
	 * @param context
	 * @return
	 */
	public int getCount(AnsiContext context){
		int offSet=0;
		//table0中有一些数据类型还不明确
		switch(context.table0.formatControl_3_NI_FMAT2){
		case 0:
			offSet=8;//FLOAT64
		case 1:
			offSet=4;//FLOAT32
		case 2:
			offSet=4;//FLOAT—CHAR12
		case 3:
			offSet=4;//FLOAT-CHAR6
		case 4:
			offSet=4;//INT32 /10000
		case 5:
			offSet=6;//BCD6
		case 6:
			offSet=4;//BCD4
		case 7:
			offSet=3;//INT24
		case 8:
			offSet=4;//INT32
		case 9:
			offSet=5;//INT40
		case 10:
			offSet=6;//INT48
		case 11:
			offSet=8;//BCD8
		case 12:
			offSet=4;//FLOAT-CHAR21
		default :
			offSet=4;//默认偏移4个字节
		}
		return offSet;
	}
}
