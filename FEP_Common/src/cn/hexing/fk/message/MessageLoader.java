/**
 * 对于需要把消息序列化到缓冲的消息，需要实现消息的序列化成字符串以及从字符串加载为消息的接口。
 */
package cn.hexing.fk.message;


/**
 */
public interface MessageLoader {
	IMessage loadMessage(String serializedString);
	String 	serializeMessage(IMessage message);
}
