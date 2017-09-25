package com.lemeng.server.channel;

import com.lemeng.server.decoder.TcpMessageDecoder;
import com.lemeng.server.encoder.TcpMessageEncoder;
import com.lemeng.server.handler.SquirrelFightTcpChannelrHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhumeilu on 17/9/7.
 */
public class SquirrelFightTcpChannelInitializer extends ChannelInitializer<NioSocketChannel>  {
    /**
     *  * LengthFieldBasedFrameDecoder 使用
     *  head+messagelength+serial+body
     *  参数maxFrameLength 为数据帧最大长度
     *  参数lengthFieldOffset为version长度表示 从第几个字段开始读取长度，表示同意为head的长度
     *  参数lengthFieldLength表示占用了多少个字节数 具体可查看LengthFieldBasedFrameDecoder的getUnadjustedFrameLength方法
     *  参数lengthAdjustment表示还需要拓展长度，具体表示为serial的长度
     *  参数initialBytesToStrip表示 传递给下个coder的时候跳过多少字节 如果从0开始为 head+messagelength+serial+body全部给下个coder
     */
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        ChannelPipeline pipeline = nioSocketChannel.pipeline();
        int maxLength = Integer.MAX_VALUE;
        pipeline.addLast("frame", new LengthFieldBasedFrameDecoder(maxLength, 2, 4, 3, 0));
        pipeline.addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));    //2次读超时5秒，自动关闭channel
        nioSocketChannel.pipeline().addLast(new TcpMessageDecoder());
        nioSocketChannel.pipeline().addLast(new TcpMessageEncoder());
        nioSocketChannel.pipeline().addLast(new SquirrelFightTcpChannelrHandler());
    }
}
