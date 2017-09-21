package com.lemeng.common;

import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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

    @Getter
    private HashMap<Short,String> userOrderHandlerMap;           //存储命令和服务对应map
    private SystemManager(){

        userOrderHandlerMap = new HashMap<Short, String>();
        userOrderHandlerMap.put(Const.HeartBreakCommand,"HeartBreak");  //心跳
        userOrderHandlerMap.put(Const.LoginCommand,"UserLoginService");  //登录
        userOrderHandlerMap.put((short)2,"Quit");  //退出

        userOrderHandlerMap.put((short)100,"UpdateRole");  //更新角色信息
        userOrderHandlerMap.put((short)101,"GetRoleList");  //获取所有角色信息

        userOrderHandlerMap.put((short)201,"Move");  //移动
        userOrderHandlerMap.put((short)202,"Turn");  //转向
        userOrderHandlerMap.put((short)203,"Fire");  //开火
        userOrderHandlerMap.put((short)204,"Hit");  //击中

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

}
