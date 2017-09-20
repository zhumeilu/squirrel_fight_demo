package com.lemeng.server.channel;

import com.lemeng.common.SystemManager;
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
        System.out.println("-------当前channelInitializer:"+this);
        ChannelPipeline pipeline = nioDatagramChannel.pipeline();
        pipeline.addLast("handler", new SquirrelFightUdpChannelHandler());//消息处理器
//        pipeline.addLast("handler", new S);//消息处理器
        pipeline.addLast("logging",new LoggingHandler(LogLevel.INFO));
    }
}
