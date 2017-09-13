package com.lemeng.server.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/13
 * Time: 14:54
 */
public class PostionUdpServer {

    public void bind(int port) throws Exception{

//        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        //配置服务端的NIO线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_BROADCAST, true)   //支持广播
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)// 设置UDP读缓冲区为1M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)// 设置UDP写缓冲区为1M
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new PositionUdpServerInitializer());
            //绑定端口，同步等待成功
            ChannelFuture f= b.bind(port).sync();
            //等待服务器监听端口关闭
            f.channel().closeFuture().await();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //优雅退出，释放线程池资源
            workerGroup.shutdownGracefully();
        }


    }


    public static void main(String[] args) throws Exception {
        int port = 8081;
        if(args!=null&&args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        new PostionUdpServer().bind(port);

    }
}
