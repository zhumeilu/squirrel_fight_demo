package com.lemeng.server.handler;

import com.lemeng.server.message.SquirrelFightTcpMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by zhumeilu on 17/9/7.
 */
public class SquirrelFightTcpChannelrHandler extends SimpleChannelInboundHandler<SquirrelFightTcpMessage> {



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SquirrelFightTcpMessage squirrelFightTcpMessage) throws Exception {

    }
}
