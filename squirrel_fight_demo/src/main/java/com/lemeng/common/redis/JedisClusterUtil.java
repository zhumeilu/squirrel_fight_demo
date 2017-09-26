package com.lemeng.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JedisClusterUtil implements IJedisClusterUtil, InitializingBean {

	public static String					address;
	public static int						timeout;
	public static int						maxRedirections;
	public static GenericObjectPoolConfig genericObjectPoolConfig;
	public static JedisCluster jedisCluster;
	protected Logger logger	= LoggerFactory.getLogger(this.getClass());

	private Set<HostAndPort> getHostAndPort() throws Exception {
		try {
			Set<HostAndPort> haps = new HashSet<HostAndPort>();
			String[] adrArray = address.split(";");
			for (int i = 0; i < adrArray.length; i++) {
				String[] ipAndPort = adrArray[i].split(":");
				HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
				haps.add(hap);
			}
			return haps;
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new Exception("解析 jedisClusterCluster 配置文件失败", ex);
		}
	}

	public void afterPropertiesSet() throws Exception {
		Set<HostAndPort> haps = this.getHostAndPort();
		jedisCluster = new JedisCluster(haps, timeout, maxRedirections, genericObjectPoolConfig
				);
	}

	/**
	 * 根据某key和对象类型获取对象
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		T obj = null;
		try {
			byte[] value = jedisCluster.get(key.getBytes());
			if (value == null) {
				return null;
			}
			obj = (T) SerializeUtil.unserialize(value);
		} catch (Exception e) {
			logger.error("jedisCluster.get key=" + key, e);
			return null;
		}
		return obj;
	}

	/**
	 * 根据某key存储对象
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public <T> boolean setObject(String key, T object) {
		if (object == null || StringUtils.isBlank(key)) {
			return false;
		}
		try {
			byte[] value = SerializeUtil.serialize(object);
			jedisCluster.set(key.getBytes(), value);
		} catch (Exception e) {
			logger.error("jedisCluster.set key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 根据某key在一定时间内存储对象
	 * 
	 * @param key
	 * @param time
	 * @param object
	 * @return
	 */
	public <T> boolean setObjectWithAliveTime(String key, T object, int time) {
		if (object == null || StringUtils.isBlank(key)) {
			return false;
		}
		try {
			byte[] value = SerializeUtil.serialize(object);
			jedisCluster.setex(key.getBytes(), time, value);
		} catch (Exception e) {
			logger.error("jedisCluster.setObjectWithAliveTime key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 根据某key获取字符串
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		String str = null;
		try {
			String value = jedisCluster.get(key);
			if (StringUtils.isBlank(value)) {
				return null;
			}
			str = jedisCluster.get(key);
		} catch (Exception e) {
			logger.error("jedisCluster.getString key=" + key, e);
			return null;
		}
		return str;
	}

	/**
	 * 根据某key存储字符串
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setString(String key, String value) {
		if (value == null || StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.set(key, value);
		} catch (Exception e) {
			logger.error("jedisCluster.setString key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 根据某key在一定时间内存储字符串
	 * 
	 * @param key
	 * @param time
	 * @param value
	 * @return
	 */
	public boolean setStringWithAliveTime(String key, int time, String value) {
		if (value == null || StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.setex(key, time, value);
		} catch (Exception e) {
			logger.error("jedisCluster.setStringWithAliveTime key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 根据某key获取字节数组
	 * 
	 * @param key
	 * @return
	 */
	public byte[] getBytes(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		byte[] bytes = null;
		try {
			bytes = jedisCluster.get(key.getBytes());
			if (bytes == null) {
				return null;
			}
		} catch (Exception e) {
			logger.error("jedisCluster.getBytes key=" + key, e);
			return null;
		}
		return bytes;
	}

	/**
	 * 根据某key存储字节数粗
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public boolean setBytes(String key, byte[] data) {
		if (data == null || StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.set(key.getBytes(), data);
		} catch (Exception e) {
			logger.error("jedisCluster.setBytes key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 根据某key在一定时间内存储字节数组
	 * 
	 * @param key
	 * @param time
	 * @param data
	 * @return
	 */
	public boolean setBytesWithAliveTime(String key, int time, byte[] data) {
		if (data == null || StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.setex(key.getBytes(), time, data);
		} catch (Exception e) {
			logger.error("jedisCluster.setBytesWithAliveTime key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 根据某key存储Map数组
	 * 
	 * @param key
	 * @param hash
	 * @return
	 */
	public boolean setMap(String key, Map<String,String> hash) {
		if (hash == null || StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.hmset(key, hash);
		} catch (Exception e) {
			logger.error("jedisCluster.setMap key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 根据某key获取Map
	 * 
	 * @param key
	 * @return
	 */
	public Map<String,String> getMap(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		Map<String,String> value = null;
		try {
			value = jedisCluster.hgetAll(key);
		} catch (Exception e) {
			logger.error("jedisCluster.getMap key=" + key, e);
			return null;
		}
		return value;
	}

	/**
	 * 根据某key给field设置指定的值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean setMapValue(String key, String field, String value) {
		if (StringUtils.isBlank(value) || StringUtils.isBlank(field) || StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.hsetnx(key, field, value);
		} catch (Exception e) {
			logger.error("jedisCluster.setMapValue key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 根据某key和 field 获取指定的 value
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String getMapValue(String key, String field) {
		if (StringUtils.isBlank(field) || StringUtils.isBlank(key)) {
			return null;
		}
		String value = null;
		try {
			value = jedisCluster.hget(key, field);
		} catch (Exception e) {
			logger.error("jedisCluster.getMapValue key=" + key, e);
			return null;
		}
		return value;
	}

	/**
	 * 删除某key的数据
	 * 
	 * @param key
	 * @return
	 */
	public boolean delete(String key) {
		if (StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.del(key);
		} catch (Exception e) {
			logger.error("jedisCluster.delete key=" + key, e);
			File file = new File("/www/resin/default/webapps/recharge-manage-bs/WEB-INF/log/redis.log");
			//将删除redis失败的key写入文件中
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fileWritter = new FileWriter(file.getName(), true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(key + "\n");
				bufferWritter.close();
			} catch (IOException e1) {
				logger.error("redis key write error" + key, e1);
			}
			return false;
		}
		return true;
	}

	/**
	 * 判断某key的的数据是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean exisits(String key) {
		if (StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.exists(key);
		} catch (Exception e) {
			logger.error("jedisCluster.delete key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 判断某key的数据是否超时
	 * 
	 * @param key
	 * @return
	 */
	public boolean expire(String key, int time) {
		if (StringUtils.isBlank(key)) {
			return false;
		}
		try {
			jedisCluster.expire(key, time);
		} catch (Exception e) {
			logger.error("jedisCluster.expire key=" + key, e);
			return false;
		}
		return true;
	}

	/**
	 * 向通道发布一个对象数据
	 * 
	 * @param channel
	 * @param object
	 * @return
	 */
	public <T> boolean publicObject(String channel, T object) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 向通道发布一个字符串数据
	 * 
	 * @param channel
	 * @param value
	 * @return
	 */
	public boolean publicString(String channel, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 向通道发布一个字节数组数据
	 * 
	 * @param channel
	 * @param data
	 * @return
	 */
	public boolean publicBytes(String channel, byte[] data) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 订阅一个通道，当其他程序向该通道发布数据时，会调用JedisPubSub的onMessage方法，我们通过重写onMessage方法，
	 * 对相应发布的数据进行处理，这里处理的是对象类型的数据，该方法在程序初始化时，不同的通道只需调用一次。
	 * 
	 * @param channel
	 * @param clazz
	 * @param listener
	 * @return
	 */
	public <T> boolean subscribeObject(String channel, Class<T> clazz, JedisPubSub listener) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 订阅一个通道，当其他程序向该通道发布数据时，会调用JedisPubSub的onMessage方法，我们通过重写onMessage方法，
	 * 对相应发布的数据进行处理，这里处理的是字符串数据，该方法在程序初始化时，不同的通道只需调用一次。
	 * 
	 * @param channel
	 * @param listener
	 * @return
	 */
	public boolean subscribeString(String channel, JedisPubSub listener) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 订阅一个通道，当其他程序向该通道发布数据时，会调用JedisPubSub的onMessage方法，我们通过重写onMessage方法，
	 * 对相应发布的数据进行处理，这里处理的是字节数组数据，该方法在程序初始化时，不同的通道只需调用一次。
	 * 
	 * @param channel
	 * @param listener
	 * @return
	 */
	public boolean subscribeBytes(String channel, JedisPubSub listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAddress(String address) {
		JedisClusterUtil.address = address;
	}

	public void setTimeout(int timeout) {
		JedisClusterUtil.timeout = timeout;
	}

	public void setMaxRedirections(int maxRedirections) {
		JedisClusterUtil.maxRedirections = maxRedirections;
	}

	public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
		JedisClusterUtil.genericObjectPoolConfig = genericObjectPoolConfig;
	}
}
