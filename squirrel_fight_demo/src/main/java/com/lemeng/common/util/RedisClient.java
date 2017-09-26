package com.lemeng.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unused" })
public class RedisClient {

	Jedis				jedis;				//非切片额客户端连接
	JedisPool			jedisPool;			//非切片连接池
	ShardedJedis		shardedJedis;		//切片额客户端连接
	ShardedJedisPool	shardedJedisPool;	//切片连接池

	public RedisClient() {
		initialPool();
		initialShardedPool();
		shardedJedis = shardedJedisPool.getResource();
		jedis = jedisPool.getResource();
	}

	/**
	 * 初始化非切片池
	 */
	private void initialPool() {
		// 池基本配置 
		JedisPoolConfig config = new JedisPoolConfig();
	}

	/**
	 * 初始化切片池
	 */
	private void initialShardedPool() {
		// 池基本配置 
		JedisPoolConfig config = new JedisPoolConfig();
	}

	/**
	 * 添加缓存
	 * 
	 * @param cacheKey
	 * @param cacheValue
	 */
	public void addCache(String cacheKey, Object cacheValue) {
		String value = "";
		if (cacheValue instanceof Map) {
			value = JSON.toJSONString(cacheValue);
//			value = JSONObject.fromObject(cacheValue).toString();
		} else if (cacheValue instanceof List) {
//			value = JSONArray.fromObject(cacheValue).toString();
			value = JSONArray.toJSONString(cacheValue);
		}
		jedis.set(cacheKey, value);
	}

	/**
	 * 获取缓存
	 * 
	 * @param cacheKey
	 *            缓存key
	 * @param returnCacheType
	 *            缓存返回值类型
	 * @return
	 */
	public Object getCache(String cacheKey, Class returnCacheType) {
		Object cacheValue = jedis.get(cacheKey);
		if (returnCacheType == List.class) {
//			JSONArray data = JSONArray.fromObject(cacheValue);
			return JSONArray.parseArray(cacheValue.toString(),returnCacheType);
		} else if (returnCacheType == Map.class) {
//			JSONArray data = JSONArray.fromObject(cacheValue);
			JSONArray data = JSONArray.parseArray(cacheValue.toString());
			return null;
//			return toHashMap(data);
		} else {
			return cacheValue;
		}
	}

	/**
	 * 将json格式的字符串解析成Map对象 <li>json格式：{"name":"admin","retries":"3fff","testname" :"ddd","testretries":"fffffffff"}
	 */
//	private Map<String,Object> toHashMap(Object object) {
//		Map<String,Object> dataOut = new HashMap<String,Object>();
//		// 将json字符串转换成jsonObject
//		JSONObject jsonObjectOut = JSONObject.fromObject(object);
//		Iterator itOut = jsonObjectOut.keys();
//		// 遍历jsonObject数据，添加到Map对象
//		while (itOut.hasNext()) {
//			String keyOut = String.valueOf(itOut.next());
//			Object valueOut = jsonObjectOut.get(keyOut);
//			Map<String,Object> data = new HashMap<String,Object>();
//			if (valueOut instanceof Map) {
//				JSONObject jsonObject = JSONObject.fromObject(valueOut);
//				Iterator it = jsonObject.keys();
//				// 遍历jsonObject数据，添加到Map对象
//				while (it.hasNext())
//				{
//					String key = String.valueOf(it.next());
//					Object value = jsonObject.get(key);
//					data.put(key, value);
//				}
//			}
//			dataOut.put(keyOut, data);
//		}
//		return dataOut;
//	}
}