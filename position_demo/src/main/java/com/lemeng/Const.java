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

    public static final short PositionCommand = 21;
    public static final short UserInfoCommand = 20;
    public static final short BattleRequestCommand = 10;
    public static final short GameStartCommand = 11;
    public static final short GameEndCommand = 12;


    public static final short HeartBreakCommand = 0;
    public static final short LoginCommand = 1;
    public static final short LogoutCommand = 2;

    public static ConcurrentHashMap connections = new ConcurrentHashMap<InetSocketAddress,Role>();


    public static AtomicInteger id  = new AtomicInteger(0);
}
