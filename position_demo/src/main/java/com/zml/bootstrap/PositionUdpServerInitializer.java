package com.zml.bootstrap;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by zhumeilu on 17/9/10.
 */
public class PositionUdpServerInitializer extends ChannelInitializer<NioDatagramChannel> {

    protected void initChannel(NioDatagramChannel nioDatagramChannel) throws Exception {
        ChannelPipeline pipeline = nioDatagramChannel.pipeline();

//         添加UDP解码器
//         pipeline.addLast("datagramPacketDecoder", new DatagramPacketDecoder(
//         new ProtobufDecoder(BaseCommand.ServerCommand.getDefaultInstance())));
        // 添加UDP编码器
//         pipeline.addLast("datagramPacketEncoder",
//         new DatagramPacketEncoder<BaseCommand>(new ProtobufEncoder()));

        pipeline.addLast("handler", new PositionUdpServerHandler());//消息处理器
        pipeline.addLast("logging",new LoggingHandler(LogLevel.INFO));
//        pipeline.addLast("ackHandler", new UdpAckServerHandler());//ack处理器

//        pipeline.addLast("timeout", new IdleStateHandler(180, 0, 0,
//                TimeUnit.SECONDS));// //此两项为添加心跳机制,60秒查看一次在线的客户端channel是否空闲
//        pipeline.addLast(new UdpHeartBeatServerHandler());// 心跳处理handler
    }
}
