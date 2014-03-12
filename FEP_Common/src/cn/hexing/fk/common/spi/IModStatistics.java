/**
 * 通信模块的统计信息
 */
package cn.hexing.fk.common.spi;

/**
 *
 */
public interface IModStatistics {
	/**
	 * 下面定义通信模块统计信息
	 */
	long getLastReceiveTime();
	long getLastSendTime();
	long getTotalRecvMessages();
	long getTotalSendMessages();
	int getMsgRecvPerMinute();
	int getMsgSendPerMinute();
}
