package com.hx.dlms.applayer.eventnotification;

import cn.hexing.fk.utils.HexDump;

public abstract class DlmsAlarmEventCode {
	public static final short FRAUD_OPEN_TERMINAL = 	(0x02 << 8)|40;		//开端纽盖 --
	public static final short FRAUD_CLOSE_TERMINAL = 	(0x02 << 8)|41;
	public static final short FRAUD_MAGNETIC_BEGIN = 	(0x02 << 8)|42;		//强磁场发生 --
	public static final short FRAUD_MAGNETIC_END = 		(0x02 << 8)|43;
	public static final short FRAUD_OPEN_METER = 		(0x02 << 8)|44;
	public static final short FRAUD_CLOSE_METER = 		(0x02 << 8)|45;
	public static final short FRAUD_COMM_KEY_ERROR = 	(0x02 << 8)|46;		//多次通信密钥错误--
	public static final short FRAUD_OPEN_MODULE = 		(0x02 << 8)|47;		//开模块盖板 --
	public static final short FRAUD_CLOSE_MODULE = 		(0x02 << 8)|48;
	
	public static final short STANDARD_CLOCK_ERROR = 	(0x01 << 8)|6;		//时钟发生故障--
	public static final short STANDARD_REPLACE_BATTERY =(0x01 << 8)|7;		//电池需要更换--
	public static final short STANDARD_PROGRAM_MEM_ERROR = 	(0x01 << 8)|12;		//程序存储器故障--
	public static final short STANDARD_DATA_MEM_ERROR = (0x01 << 8)|13;		//数据存储器故障--
	public static final short STANDARD_MEASURE_ERROR =	(0x01 << 8)|16;		//计量系统故障--
	public static final short STANDARD_SETTLE_UP =		(0x01 << 8)|29;		//结算--
	public static final short STANDARD_TOKEN_CHARGE =	(0x01 << 8)|34;		//token充值--
	public static final short STANDARD_TARIFF_ACTIVATE =(0x01 << 8)|35;		//费率激活事件--
	public static final short STANDARD_MANAGE_TOKEN =	(0x01 << 8)|36;		//管理码token--
	
	public static final short GRID_MISS_A_PHASE_BEGIN = (0x04 << 8)|1;		//A相断相开始--
	public static final short GRID_MISS_B_PHASE_BEGIN = (0x04 << 8)|2;		//
	public static final short GRID_MISS_C_PHASE_BEGIN = (0x04 << 8)|3;		//
	public static final short GRID_MISS_A_PHASE_END = 	(0x04 << 8)|4;		//
	public static final short GRID_MISS_B_PHASE_END = 	(0x04 << 8)|5;		//
	public static final short GRID_MISS_C_PHASE_END = 	(0x04 << 8)|6;		//
	
	public static final short GRID_BY_PASS_BEGIN = 		(0x04 << 8)|71;		//旁路开始--
	public static final short GRID_BY_PASS_END = 		(0x04 << 8)|72;		//
	public static final short GRID_REVERSE_PHASE_SEQ_BEGIN =  (0x04 << 8)|73;	//逆相序开始--
	public static final short GRID_REVERSE_PHASE_SEQ_END = (0x04 << 8)|74;	//逆相序结束--
	public static final short GRID_POWER_OFF =  		(0x04 << 8)|75;	//电网掉电--
	public static final short GRID_POWER_ON = 			(0x04 << 8)|76;	//电网上电--
	public static final short GRID_A_CURRENT_REVERSE_BEGIN = (0x04 << 8)|78;		//A相电流反向开始--
	public static final short GRID_A_CURRENT_REVERSE_END = (0x04 << 8)|79;		
	public static final short GRID_B_CURRENT_REVERSE_BEGIN = (0x04 << 8)|80;		
	public static final short GRID_B_CURRENT_REVERSE_END = (0x04 << 8)|81;		
	public static final short GRID_C_CURRENT_REVERSE_BEGIN = (0x04 << 8)|82;		
	public static final short GRID_C_CURRENT_REVERSE_END = (0x04 << 8)|83;		
	public static final short GRID_LOSS_CURRENT_BEGIN = (0x04 << 8)|84;	//失流起始--
	public static final short GRID_LOSS_CURRENT_END = 	(0x04 << 8)|85;	//--
	public static final short GRID_A_LOSS_VOLTAGE_BEGIN = (0x04 << 8)|86;		//A相失压开始--
	public static final short GRID_A_LOSS_VOLTAGE_END = (0x04 << 8)|87;		
	public static final short GRID_B_LOSS_VOLTAGE_BEGIN = (0x04 << 8)|88;		
	public static final short GRID_B_LOSS_VOLTAGE_END = (0x04 << 8)|89;		
	public static final short GRID_C_LOSS_VOLTAGE_BEGIN = (0x04 << 8)|90;		
	public static final short GRID_C_LOSS_VOLTAGE_END = (0x04 << 8)|91;		
	public static final short GRID_A_OVER_VOLTAGE_BEGIN = (0x04 << 8)|94;		//A相过压开始--
	public static final short GRID_A_OVER_VOLTAGE_END = (0x04 << 8)|95;		
	public static final short GRID_B_OVER_VOLTAGE_BEGIN = (0x04 << 8)|96;		
	public static final short GRID_B_OVER_VOLTAGE_END = (0x04 << 8)|97;		
	public static final short GRID_C_OVER_VOLTAGE_BEGIN = (0x04 << 8)|98;		
	public static final short GRID_C_OVER_VOLTAGE_END = (0x04 << 8)|99;		

	public static final short GRID_3C_PHASE_UNBALANCE_BEGIN = (0x04 << 8)|149; //三相电流不平衡起始--	
	public static final short GRID_3C_PHASE_UNBALANCE_END = (0x04 << 8)|93;		
	
	public static final String toString(short eventCode){
		switch(eventCode){
/*		case FRAUD_OPEN_TERMINAL: return "OPEN_TERMINAL";
		case FRAUD_CLOSE_TERMINAL: return "CLOSE_TERMINAL";
		case FRAUD_MAGNETIC_BEGIN: return "MAGNETIC_BEGIN";
		case FRAUD_MAGNETIC_END: return "MAGNETIC_END";
		case FRAUD_OPEN_METER: return "OPEN_METER";
		case FRAUD_CLOSE_METER: return "CLOSE_METER";
		case FRAUD_COMM_KEY_ERROR: return "COMM_KEY_ERROR";
		case FRAUD_OPEN_MODULE: return "OPEN_MODULE";
		case FRAUD_CLOSE_MODULE: return "CLOSE_MODULE";

		case STANDARD_CLOCK_ERROR: return "CLOCK_ERROR";
		case STANDARD_REPLACE_BATTERY: return "REPLACE_BATTERY";
		case STANDARD_PROGRAM_MEM_ERROR: return "PROGRAM_MEM_ERROR";
		case STANDARD_DATA_MEM_ERROR: return "DATA_MEM_ERROR";
		case STANDARD_MEASURE_ERROR: return "MEASURE_ERROR";
		case STANDARD_SETTLE_UP: return "SETTLE_UP";
		case STANDARD_TOKEN_CHARGE: return "TOKEN_CHARGE";
		case STANDARD_TARIFF_ACTIVATE: return "TARIFF_ACTIVATE";
		case STANDARD_MANAGE_TOKEN: return "MANAGE_TOKEN";
		//Grid events
		case GRID_BY_PASS_BEGIN: return "BY_PASS_BEGIN";
		case GRID_BY_PASS_END: return "BY_PASS_END";
		case GRID_REVERSE_PHASE_SEQ_BEGIN: return "REVERSE_PHASE_SEQ_BEGIN";
		case GRID_REVERSE_PHASE_SEQ_END: return "REVERSE_PHASE_SEQ_END";
		case GRID_POWER_OFF: return "POWER_OFF";
		case GRID_POWER_ON: return "POWER_ON";
		case GRID_3C_PHASE_UNBALANCE_BEGIN: return "3C_PHASE_UNBALANCE_BEGIN";
		case GRID_3C_PHASE_UNBALANCE_END: return "3C_PHASE_UNBALANCE_END";
		
		case GRID_MISS_A_PHASE_BEGIN: return "MISS_A_PHASE_BEGIN";
		case GRID_MISS_A_PHASE_END: return "MISS_A_PHASE_END";
		case GRID_MISS_B_PHASE_BEGIN: return "MISS_B_PHASE_BEGIN";
		case GRID_MISS_B_PHASE_END: return "MISS_B_PHASE_END";
		case GRID_MISS_C_PHASE_BEGIN: return "MISS_C_PHASE_BEGIN";
		case GRID_MISS_C_PHASE_END: return "MISS_C_PHASE_END";
		case GRID_LOSS_CURRENT_BEGIN: return "LOSS_CURRENT_BEGIN";
		case GRID_LOSS_CURRENT_END: return "LOSS_CURRENT_END";

		case GRID_A_CURRENT_REVERSE_BEGIN: return "A_CURRENT_REVERSE_BEGIN";
		case GRID_A_CURRENT_REVERSE_END: return "A_CURRENT_REVERSE_END";
		case GRID_B_CURRENT_REVERSE_BEGIN: return "B_CURRENT_REVERSE_BEGIN";
		case GRID_B_CURRENT_REVERSE_END: return "B_CURRENT_REVERSE_END";
		case GRID_C_CURRENT_REVERSE_BEGIN: return "C_CURRENT_REVERSE_BEGIN";
		case GRID_C_CURRENT_REVERSE_END: return "C_CURRENT_REVERSE_END";

		case GRID_A_LOSS_VOLTAGE_BEGIN: return "A_LOSS_VOLTAGE_BEGIN";
		case GRID_A_LOSS_VOLTAGE_END: return "A_LOSS_VOLTAGE_END";
		case GRID_B_LOSS_VOLTAGE_BEGIN: return "B_LOSS_VOLTAGE_BEGIN";
		case GRID_B_LOSS_VOLTAGE_END: return "B_LOSS_VOLTAGE_END";
		case GRID_C_LOSS_VOLTAGE_BEGIN: return "C_LOSS_VOLTAGE_BEGIN";
		case GRID_C_LOSS_VOLTAGE_END: return "C_LOSS_VOLTAGE_END";

		case GRID_A_OVER_VOLTAGE_BEGIN: return "A_OVER_VOLTAGE_BEGIN";
		case GRID_A_OVER_VOLTAGE_END: return "A_OVER_VOLTAGE_END";
		case GRID_B_OVER_VOLTAGE_BEGIN: return "B_OVER_VOLTAGE_BEGIN";
		case GRID_B_OVER_VOLTAGE_END: return "B_OVER_VOLTAGE_END";
		case GRID_C_OVER_VOLTAGE_BEGIN: return "C_OVER_VOLTAGE_BEGIN";
		case GRID_C_OVER_VOLTAGE_END: return "C_OVER_VOLTAGE_END";
*/
		default:{
			return HexDump.toHex(eventCode);
		}
		}
	}
}
