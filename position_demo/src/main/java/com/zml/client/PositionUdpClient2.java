package com.zml.client;

import com.zml.util.ConvertUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Created by zhumeilu on 17/9/7.
 */
public class PositionUdpClient2 {
//    public void bind(int port) throws Exception{
//
//
//        //配置服务端的NIO线程组
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try{
//            Bootstrap b = new Bootstrap();
//             b.group(workerGroup)
//                    .channel(NioDatagramChannel.class)
//                    .option(ChannelOption.SO_BROADCAST, true)   //支持广播
//                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)// 设置UDP读缓冲区为1M
//                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)// 设置UDP写缓冲区为1M
//                    .handler(new PositionUdpClientHandler());
//            //绑定端口，同步等待成功
//            ChannelFuture f= b.bind(0).sync();
//            Channel channel = f.channel();
//
//            //登录
//            byte[] loginCommand = ConvertUtil.getBytes((short) 1);
//            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(loginCommand),new InetSocketAddress("127.0.0.1",port)));
//            Thread.sleep(1000);
//            //获取所有位置
//            byte[] positionListCommand = ConvertUtil.getBytes((short) 3);
//            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(positionListCommand),new InetSocketAddress("127.0.0.1",port)));
//            for (int i = 0; i < 3; i++) {
//                BaseCommand.PositionCommand.Builder builder = BaseCommand.PositionCommand.newBuilder();
//                builder.setId(i);
//                builder.setPositionX(i);
//                builder.setPositionY(i);
//                builder.setPositionZ(i);
//                byte[] positionBytes = builder.build().toByteArray();
//                byte[] positionCommand = ConvertUtil.getBytes((short) 2);
//                channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(positionCommand,positionBytes),new InetSocketAddress("127.0.0.1",port)));
//                Thread.sleep(1000);
//
//            }
//
//            //等待服务器监听端口关闭
//            channel.closeFuture().await();
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            //优雅退出，释放线程池资源
//            workerGroup.shutdownGracefully();
//        }
//
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        int port = 8081;
//        if(args!=null&&args.length>0){
//            try{
//                port = Integer.valueOf(args[0]);
//            }catch (NumberFormatException e){
//
//            }
//        }
//        new PositionUdpClient2().bind(port);
//
//    }
}
