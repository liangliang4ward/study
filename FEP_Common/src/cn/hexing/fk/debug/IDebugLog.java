package cn.hexing.fk.debug;

import org.apache.log4j.Logger;

public interface IDebugLog {

	void setName(String name);
	Logger getLogger();
}
