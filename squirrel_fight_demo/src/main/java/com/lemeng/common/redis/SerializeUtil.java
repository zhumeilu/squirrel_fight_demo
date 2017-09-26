package com.lemeng.common.redis;

import java.io.*;

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
}
