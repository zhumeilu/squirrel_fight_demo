package com.lemeng.common.redis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializeUtil {
	public static byte[] serialize(Object object){
		if(object!=null){
			ObjectOutputStream oos=null;
			ByteArrayOutputStream baos=null;
			try {
				baos=new ByteArrayOutputStream();
				
				oos=new ObjectOutputStream(baos);
				
				oos.writeObject(object);
				
				byte[] bytes=baos.toByteArray();
				
				return bytes;
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(null!=oos){
						oos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	public static Object unserialize(byte[] bytes){
		if(bytes!=null){
			ByteArrayInputStream bais=null;
			ObjectInputStream ois=null;
			try {
				bais=new ByteArrayInputStream(bytes);
				
				ois=new ObjectInputStream(bais);
				
				return ois.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(null!=ois){
						ois.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 序列化 list 集合
	 *
	 * @param list
	 * @return
	 */
	public static byte[] serializeList(List<?> list) {

		if (list==null||list.size()==0) {
			return null;
		}
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			for (Object obj : list) {
				oos.writeObject(obj);
			}
			bytes = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(oos);
			close(baos);
		}
		return bytes;
	}
	/**
	 * 反序列化 list 集合
	 *
	 * @return
	 */
	public static List<?> unserializeList(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		List<Object> list = new ArrayList<Object>();
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			while (bais.available() > 0) {
				Object obj = (Object) ois.readObject();
				if (obj == null) {
					break;
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(bais);
			close(ois);
		}
		return list;
	}
	/**
	 * 关闭io流对象
	 *
	 * @param closeable
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
