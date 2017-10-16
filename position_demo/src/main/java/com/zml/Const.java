package com.zml;

import com.zml.model.Player;

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

    public static final short LoginRequestCommand = 1;
    public static final short LoginResponseCommand = 2;
    public static final short UserInfoCommand = 3;
    public static final short UserInfoListCommand = 4;



    public static ConcurrentHashMap connections = new ConcurrentHashMap<InetSocketAddress,Player>();
    public static ConcurrentHashMap players = new ConcurrentHashMap<Integer,Player>();


    public static AtomicInteger id  = new AtomicInteger(0);
    public static Integer generatePlayerId(){
        return id.incrementAndGet();
    }
}
