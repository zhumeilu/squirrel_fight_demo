package com.lemeng.common;

import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.*;
import com.lemeng.server.session.NettyTcpSession;
import com.sun.javafx.image.IntPixelGetter;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    Logger logger = LoggerFactory.getLogger(this.getClass());
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

    //暂时没用到
    private ConcurrentHashMap tcpSessionMap = new ConcurrentHashMap<Long,NettyTcpSession>();       //存储session

    @Getter
    private ConcurrentHashMap<Integer,Game> gameConcurrentHashMap = new ConcurrentHashMap<Integer,Game>();       //存储game
    @Getter
    private ConcurrentHashMap<Integer,Team> teamConcurrentHashMap = new ConcurrentHashMap<Integer,Team>();       //存储team
    @Getter
    private ConcurrentHashMap<Integer,AssistRecord> assistRecordConcurrentHashMap = new ConcurrentHashMap<Integer,AssistRecord>();       //存储助攻记录
    @Getter
    private ConcurrentHashMap<Integer,Player> playerConcurrentHashMap = new ConcurrentHashMap<Integer,Player>();       //存储玩家信息
    @Getter
    private ConcurrentHashMap<Integer,Room> roomConcurrentHashMap = new ConcurrentHashMap<Integer,Room>();       //存储Room


    @Getter
    private ConcurrentHashMap userChannelMap = new ConcurrentHashMap<Integer,Channel>();       //存储user- channel
    @Getter
    private HashMap<Short,String> userOrderHandlerMap;           //存储命令和服务对应map

    @Getter
    BlockingQueue<Room> oneRoomQueue = new LinkedBlockingQueue<Room>();     //1人匹配队列
    @Getter
    BlockingQueue<Room> twoRoomQueue = new LinkedBlockingQueue<Room>();     //2人匹配队列
    @Getter
    BlockingQueue<Room> threeRoomQueue = new LinkedBlockingQueue<Room>();    //3人匹配队列
    @Getter
    BlockingQueue<Room> fourRoomQueue = new LinkedBlockingQueue<Room>();    //4人匹配队列
    @Getter
    BlockingQueue<Room> fiveRoomQueue = new LinkedBlockingQueue<Room>();    //5人匹配队列
    @Getter
    BlockingQueue<Team> teamQuenue = new LinkedBlockingQueue<Team>();    //组队队列
    @Getter
    BlockingQueue<Game> gameQuenue = new LinkedBlockingQueue<Game>();    //游戏队列（可以加入的游戏队列）
    @Getter
    private IdGenertor idGenertor = new IdGenertor();

    private JedisClusterUtil jedisClusterUtil;
    public JedisClusterUtil getJedisClusterUtil(){
        return (JedisClusterUtil)context.getBean("jedisCluster");
    }

    private SystemManager(){

        userOrderHandlerMap = new HashMap<Short, String>();
        userOrderHandlerMap.put(Const.HeartBreakCommand,"HeartBreak");  //心跳
        userOrderHandlerMap.put(Const.LoginRequestCommand,"UserLoginUdpService");  //登录

        userOrderHandlerMap.put(Const.RegistRequestCommand,"UserRegistTestUdpService");  //测试  upd位置同步使用
        userOrderHandlerMap.put(Const.PlayerInfoCommand,"PlayerInfoTestUdpService");  //测试 upd位置同步使用
        userOrderHandlerMap.put(Const.QuitGameCommand,"QuitGameTestUdpService");  //测试 upd位置同步使用


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

    //同步synchronized
    public List<Team> getMatchTeam(){
        List<Team> teamList = new ArrayList<Team>();
        //满足6个组队
        if(teamQuenue.size()>=6){
            for (int i=0;i<teamQuenue.size();i++){
                try {
                    teamList.add(teamQuenue.take());
                } catch (InterruptedException e) {
                    logStackTrace(e);
                }
            }
            return teamList;

        }else if(teamQuenue.size()<6&&teamQuenue.size()>=2){
            //不满6个组队
            int num = RandomUtils.nextInt(2, 6);
            for (int i=0;i<num;i++){
                try {
                    teamList.add(teamQuenue.take());
                } catch (InterruptedException e) {
                    logStackTrace(e);
                }
            }

            return teamList;
        }

       return null;
    }

    protected void logStackTrace( Exception e ) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        // e.printStackTrace();
        logger.error(writer.toString());
    }
}
