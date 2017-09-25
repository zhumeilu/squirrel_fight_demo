package com.lemeng.test.service;

import com.lemeng.game.domain.Player;

import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 14:20
 */
public class TestSystemManager {
    private static TestSystemManager instance = new TestSystemManager();
    public static TestSystemManager getInstance(){
        return instance;
    }

    private ConcurrentHashMap playerInfoMap = new ConcurrentHashMap<InetSocketAddress,Player>();       //存储session
    public AtomicInteger id  = new AtomicInteger(0);

    public Integer getId(){
        return id.getAndIncrement();
    }
    public Player savePlayer(InetSocketAddress sender,Player player){
        return (Player)this.playerInfoMap.put(sender,player);
    }
    public Player removePlayer(InetSocketAddress sender){
        return (Player)this.playerInfoMap.remove(sender);
    }
    public Player getPlayer(InetSocketAddress sender){
        return (Player) this.playerInfoMap.get(sender);
    }
    public Enumeration getAllSender(){
        return this.playerInfoMap.keys();
    }
}
