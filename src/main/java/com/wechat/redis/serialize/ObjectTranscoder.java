package com.wechat.redis.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.springframework.stereotype.Service;

/**
 * Object序列化
 * @author 周文星
 */
@Service
public class ObjectTranscoder<T extends Serializable> extends SerializeTranscoder {

	@SuppressWarnings("unchecked")
	@Override
	public byte[] serialize(Object value) {
		if (value == null) {  
			throw new NullPointerException("Can't serialize null");  
		}  
		byte[] result = null;  
		ByteArrayOutputStream bos = null;  
		ObjectOutputStream os = null;  
		try {  
			bos = new ByteArrayOutputStream();  
			os = new ObjectOutputStream(bos);
			T t = (T) value;
			os.writeObject(t);  
			os.close();  
			bos.close();  
			result = bos.toByteArray();  
		} catch (IOException e) {  
			throw new IllegalArgumentException("Non-serializable object", e);  
		} finally {  
			close(os);  
			close(bos);  
		}  
		return result;  
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] in) {
		T result = null;  
		ByteArrayInputStream bis = null;  
		ObjectInputStream is = null;  
		try {  
		  if (in != null) {  
			  bis = new ByteArrayInputStream(in);  
			  is = new ObjectInputStream(bis);  
			  result = (T)is.readObject();  
			  is.close();  
			  bis.close();  
		  }  
		} catch (IOException e) {  
		 
		} catch (ClassNotFoundException e) {  
		 
		} finally {  
		  close(is);  
		  close(bis);  
		}  
	  return result;  
	}
}
