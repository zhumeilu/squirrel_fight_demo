package com.lemeng.server.handler;

import com.lemeng.common.SystemManager;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.TcpHandlerService;
import com.lemeng.server.session.NettyTcpSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by zhumeilu on 17/9/7.
 */
public class SquirrelFightTcpChannelrHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        if (cause instanceof java.io.IOException)
            return;
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) msg;
        //消息分发
        TcpHandlerService handlerService = (TcpHandlerService) SystemManager.getInstance().getContext().getBean("TcpHandlerService");
        handlerService.submit(ctx,tcpMessage);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        SystemManager.getInstance().addTcpSession(new NettyTcpSession(ctx.channel()));
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);

        Channel channel = ctx.channel();
        Long sessionId = channel.attr(NettyTcpSession.channel_session_id).get();
        SystemManager.getInstance().removeTcpSession(sessionId);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
