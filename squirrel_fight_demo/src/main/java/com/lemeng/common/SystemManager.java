package com.lemeng.common;

import com.lemeng.game.domain.Room;
import com.lemeng.game.domain.Team;
import com.lemeng.server.session.NettyTcpSession;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 11:42
 */
public class SystemManager {

    private static SystemManager instance = new SystemManager();
    @Getter
    @Setter
    private ApplicationContext context ;        //spring上下文
    @Setter
    private EventLoopGroup udpWorkerGroup;
    @Setter
    private EventLoopGroup tcpBossGroup;
    @Setter
    private EventLoopGroup tcpWorkerGroup;

    private ConcurrentHashMap tcpSessionMap = new ConcurrentHashMap<Long,NettyTcpSession>();       //存储session
    @Getter
    private ConcurrentHashMap teamMap = new ConcurrentHashMap<Integer,Team>();       //存储session
    @Getter
    private ConcurrentHashMap userChannelMap = new ConcurrentHashMap<Integer,Channel>();       //存储user- channel
    @Getter
    private HashMap<Short,String> userOrderHandlerMap;           //存储命令和服务对应map

    BlockingQueue<Room> oneRoomQueue = new LinkedBlockingQueue<Room>();     //1人匹配队列
    BlockingQueue<Room> twoRoomQueue = new LinkedBlockingQueue<Room>();     //2人匹配队列
    BlockingQueue<Room> threeRoomQueue = new LinkedBlockingQueue<Room>();    //3人匹配队列
    BlockingQueue<Room> fourRoomQueue = new LinkedBlockingQueue<Room>();    //4人匹配队列
    BlockingQueue<Room> fiveRoomQueue = new LinkedBlockingQueue<Room>();    //5人匹配队列

    private SystemManager(){

        userOrderHandlerMap = new HashMap<Short, String>();
        userOrderHandlerMap.put(Const.HeartBreakCommand,"HeartBreak");  //心跳
        userOrderHandlerMap.put(Const.LoginRequestCommand,"UserLoginService");  //登录

        userOrderHandlerMap.put(Const.RegistRequestCommand,"UserRegistTestService");  //测试  upd位置同步使用
        userOrderHandlerMap.put(Const.PlayerInfoCommand,"PlayerInfoTestService");  //测试 upd位置同步使用
        userOrderHandlerMap.put(Const.QuitGameCommand,"QuitGameTestService");  //测试 upd位置同步使用


    }

    public static SystemManager getInstance(){
        return instance;
    }

    public void shutdownServer(){
        udpWorkerGroup.shutdownGracefully();
        System.out.println("-------udp服务器关闭----------");
        tcpBossGroup.shutdownGracefully();
        System.out.println("-------tcp boss服务器关闭----------");
        tcpWorkerGroup.shutdownGracefully();
        System.out.println("-------tcp worker服务器关闭----------");

    }

    public void addTcpSession(NettyTcpSession nettyTcpSession){
        tcpSessionMap.put(nettyTcpSession.getSessionId(),nettyTcpSession);
    }
    public void removeTcpSession(Long sessionId){
        tcpSessionMap.remove(sessionId);
    }

    public void addQuene(Room room){
        int size = room.getUserList().size();
        switch (size){
            case 1:
                oneRoomQueue.add(room);
                return;
            case 2:
                twoRoomQueue.add(room);
                return;
            case 3:
                threeRoomQueue.add(room);
                return;
            case 4:
                fourRoomQueue.add(room);
                return;
            case 5:
                fiveRoomQueue.add(room);
                return;
            default:
                throw new RuntimeException("房间为空，无法加入匹配队列");
        }

    }

}
