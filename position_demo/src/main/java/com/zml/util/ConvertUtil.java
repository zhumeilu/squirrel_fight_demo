package com.zml.util;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/13
 * Time: 19:55
 */
public class ConvertUtil {
    public static short getShort(byte[] bytes){
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static byte[] getBytes(short data){
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }
}
