package com.lemeng.server.bootstrap;

import com.lemeng.common.SystemManager;
import com.lemeng.server.channel.SquirrelFightTcpChannelInitializer;
import com.lemeng.server.channel.SquirrelFightUdpChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/18
 * Time: 19:02
 */
public class SquirrelFightServer {


    void initSpring(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SystemManager.getInstance().setContext(context);
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(context)));
    }

    void startUdpService(int port){
        //配置服务端的NIO线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        SystemManager.getInstance().setUdpWorkerGroup(workerGroup);
        try{
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_BROADCAST, true)   //支持广播
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)// 设置UDP读缓冲区为1M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)// 设置UDP写缓冲区为1M
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new SquirrelFightUdpChannelInitializer());
            //绑定端口，同步等待成功
            ChannelFuture f= b.bind(port).sync();
            //等待服务器监听端口关闭
            f.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void startTcpService(int port){

        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        SystemManager.getInstance().setTcpBossGroup(bossGroup);
        SystemManager.getInstance().setTcpWorkerGroup(workerGroup);
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new SquirrelFightTcpChannelInitializer());
            //绑定端口，同步等待成功
            ChannelFuture f= b.bind(port).sync();
            //等待服务器监听端口关闭
            f.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addShutdownHook(){
        // 注册停服监听器，用于执行资源的销毁等停服时的处理工作
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                SystemManager.getInstance().shutdownServer();
            }
        });
    }
    public static void main(String[] args) throws Exception {
        int udpPort = 6666;
        int tcpPort = 7777;

        SquirrelFightServer squirrelFightServer = new SquirrelFightServer();
        squirrelFightServer.initSpring();
        System.out.println("---------spring启动成功------------");
        squirrelFightServer.startUdpService(udpPort);
        System.out.println("---------udp启动成功------------");
        squirrelFightServer.startTcpService(tcpPort);
        System.out.println("---------tcp启动成功------------");
        squirrelFightServer.addShutdownHook();
        System.out.println("---------添加服务器关闭回调------------");
    }
}
