package com.lemeng.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

	public static Logger logger = LoggerFactory.getLogger(RandomUtil.class);
	/**
	 * 获取指定数量的随机字符串
	 * @return
	 */
	public static String getRandomCode(int num){
		String val = "";
		Random random = new Random();
		for (int i = 0; i < num; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}
	
	/**
	 * 获取指定数量的随机数
	 * @return
	 */
	public static String getRandomNumber(int num){
		String str="";
		Random random=new Random();
		for(int i=0;i<num;i++){
			str+=random.nextInt(10);
		}
		return str;
	}

	/**
	 * 获取16进制随机数
	 * @param len
	 * @return
	 */
	public static String getRandomHexString(int len)  {
		try {
			StringBuffer result = new StringBuffer();
			for(int i=0;i<len;i++) {
				result.append(Integer.toHexString(new Random().nextInt(16)));
			}
			return result.toString().toLowerCase();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	public static String getRandomUUID(){
		return UUID.randomUUID().toString().replace("-","");
	}
	public static void main(String[] args) {
		
		System.out.println(getRandomCode(6));
		System.out.println(getRandomNumber(12));
		System.out.println(getRandomHexString(6));
	}
}
