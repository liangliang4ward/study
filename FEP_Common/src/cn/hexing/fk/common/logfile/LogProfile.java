/**
 * 主要用途：用于系统的Profile信息记录日志。防止大量日志冲刷，导致profile信息找不到。
 * 注意事项：在log4j.properties文件中，把LogProfile专门配置输出到一个文件。
 */
package cn.hexing.fk.common.logfile;

import org.apache.log4j.Logger;

/**
 *
 */
public class LogProfile {
	private static final Logger log = Logger.getLogger(LogProfile.class);
	public static final Logger getLog(){
		return log;
	}
}
