package com.lemeng.common.redis;


import redis.clients.jedis.JedisPubSub;

import java.util.Map;

public interface IJedisClusterUtil {

		/**
		 * 根据某key和对象类型获取对象
		 * 
		 * @param key
		 * @param clazz
		 * @return
		 */
		<T> T getObject(String key);

		/**
		 * 根据某key存储对象
		 * 
		 * @param key
		 * @param object
		 * @return
		 */
		<T> boolean setObject(String key, T object);

		/**
		 * 根据某key在一定时间内存储对象
		 * 
		 * @param key
		 * @param time
		 * @param object
		 * @return
		 */
		<T> boolean setObjectWithAliveTime(String key, T object, int time);

		/**
		 * 根据某key获取字符串
		 * 
		 * @param key
		 * @return
		 */
		String getString(String key);

		/**
		 * 根据某key存储字符串
		 * 
		 * @param key
		 * @param value
		 * @return
		 */
		boolean setString(String key, String value);

		/**
		 * 根据某key在一定时间内存储字符串
		 * 
		 * @param key
		 * @param time
		 * @param value
		 * @return
		 */
		boolean setStringWithAliveTime(String key, int time, String value);

		/**
		 * 根据某key获取字节数组
		 * 
		 * @param key
		 * @return
		 */
		byte[] getBytes(String key);

		/**
		 * 根据某key存储字节数粗
		 * 
		 * @param key
		 * @param data
		 * @return
		 */
		boolean setBytes(String key, byte[] data);

		/**
		 * 根据某key在一定时间内存储字节数组
		 * 
		 * @param key
		 * @param time
		 * @param data
		 * @return
		 */
		boolean setBytesWithAliveTime(String key, int time, byte[] data);
		
		/**
		 * 根据某key存储Map数组
		 * @param key
		 * @param hash
		 * @return
		 */
		boolean setMap(String key, Map<String, String> hash);

		/**
		 * 根据某key给field设置指定的值
		 * @param key
		 * @param field
		 * @param value
		 * @return
		 */
		boolean setMapValue(String key, String field, String value);
		
		/**
		 * 根据某key获取Map
		 * @param key
		 * @return
		 */
		Map<String,String> getMap(String key);
		
		/**
		 * 根据某key和 field 获取指定的 value
		 * @param key
		 * @param field
		 * @return
		 */
		String getMapValue(String key, String field);
		
		/**
		 * 删除某key的数据
		 * 
		 * @param key
		 * @return
		 */
		boolean delete(String key);

		/**
		 * 判断某key的的数据是否存在
		 * 
		 * @param key
		 * @return
		 */
		boolean exisits(String key);

		/**
		 * 判断某key的数据是否超时
		 * 
		 * @param key
		 * @return
		 */
		boolean expire(String key, int time);

		/**
		 * 向通道发布一个对象数据
		 * 
		 * @param channel
		 * @param object
		 * @return
		 */
		<T> boolean publicObject(String channel, T object);

		/**
		 * 向通道发布一个字符串数据
		 * 
		 * @param channel
		 * @param value
		 * @return
		 */
		boolean publicString(String channel, String value);

		/**
		 * 向通道发布一个字节数组数据
		 * 
		 * @param channel
		 * @param data
		 * @return
		 */
		boolean publicBytes(String channel, byte[] data);

		/**
		 * 订阅一个通道，当其他程序向该通道发布数据时，会调用JedisPubSub的onMessage方法，我们通过重写onMessage方法，
		 * 对相应发布的数据进行处理，这里处理的是对象类型的数据，该方法在程序初始化时，不同的通道只需调用一次。
		 * 
		 * @param channel
		 * @param clazz
		 * @param listener
		 * @return
		 */
		<T> boolean subscribeObject(String channel, Class<T> clazz, JedisPubSub listener);

		/**
		 * 订阅一个通道，当其他程序向该通道发布数据时，会调用JedisPubSub的onMessage方法，我们通过重写onMessage方法，
		 * 对相应发布的数据进行处理，这里处理的是字符串数据，该方法在程序初始化时，不同的通道只需调用一次。
		 * 
		 * @param channel
		 * @param listener
		 * @return
		 */
		boolean subscribeString(String channel, JedisPubSub listener);

		/**
		 * 订阅一个通道，当其他程序向该通道发布数据时，会调用JedisPubSub的onMessage方法，我们通过重写onMessage方法，
		 * 对相应发布的数据进行处理，这里处理的是字节数组数据，该方法在程序初始化时，不同的通道只需调用一次。
		 * 
		 * @param channel
		 * @param listener
		 * @return
		 */
		boolean subscribeBytes(String channel, JedisPubSub listener);

}
