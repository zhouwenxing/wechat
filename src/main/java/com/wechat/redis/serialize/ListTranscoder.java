package com.wechat.redis.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * List序列化
 * @author 周文星
 */
@Service
public class ListTranscoder<T extends Serializable> extends SerializeTranscoder {

	@SuppressWarnings("unchecked")
	@Override
	public byte[] serialize(Object value) {
		if (value == null) {
			throw new NullPointerException("Can't serialize null");
		}
		List<T> values = (List<T>) value;
		byte[] results = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			for (T t : values) {
				os.writeObject(t);
			}
			os.close();
			bos.close();
			results = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			close(os);
			close(bos);
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> deserialize(byte[] in) {
		List<T> list = new ArrayList<T>();
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				while (true) {
					T t = (T)is.readObject();
					if (t == null) {
						break;
					}
					list.add(t);
				}
				is.close();
				bis.close();
			}
		} catch (IOException e) {
		
		} catch (ClassNotFoundException e) {
			
		} finally {
			close(is);
			close(bis);
		}
		return list;
	}
}
