package com.lemeng.server.channel;

import com.lemeng.server.decoder.UdpMessageDecoder;
import com.lemeng.server.encoder.UdpMessageEncoder;
import com.lemeng.server.handler.SquirrelFightUdpChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/18
 * Time: 19:09
 */
public class SquirrelFightUdpChannelInitializer extends ChannelInitializer<NioDatagramChannel> {
    protected void initChannel(NioDatagramChannel nioDatagramChannel) throws Exception {
        ChannelPipeline pipeline = nioDatagramChannel.pipeline();
        pipeline.addLast("decoder",new UdpMessageDecoder());
        pipeline.addLast("encoder",new UdpMessageEncoder());
        pipeline.addLast("handler", new SquirrelFightUdpChannelHandler());//消息处理器
//        pipeline.addLast("handler", new S);//消息处理器
        pipeline.addLast("logging",new LoggingHandler(LogLevel.INFO));
    }
}
