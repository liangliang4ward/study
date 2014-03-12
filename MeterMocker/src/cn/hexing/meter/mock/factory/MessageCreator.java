package cn.hexing.meter.mock.factory;

import cn.hexing.meter.mock.spi.Attribute;


public interface MessageCreator {

	 byte[] createHeartBeat(Attribute attr);

}
