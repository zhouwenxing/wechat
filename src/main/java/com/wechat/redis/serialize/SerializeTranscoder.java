package com.wechat.redis.serialize;
import java.io.Closeable;


/**
 * 手动序列化抽象类
 * @author 周文星
 */
public abstract class SerializeTranscoder {
	
	public abstract byte[] serialize(Object value);
	
	public abstract Object deserialize(byte[] in);
	
	/**
	 * 关闭流
	 * @param closeable - 流
	 */
	public void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				 e.printStackTrace(); 
			}
		}
	}

}
