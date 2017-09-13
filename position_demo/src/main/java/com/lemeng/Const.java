package com.lemeng;

import com.lemeng.model.Role;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/13
 * Time: 15:40
 */
public class Const {


    public static ConcurrentHashMap connections = new ConcurrentHashMap<InetSocketAddress,Role>();


    public static AtomicInteger id  = new AtomicInteger(0);
}
