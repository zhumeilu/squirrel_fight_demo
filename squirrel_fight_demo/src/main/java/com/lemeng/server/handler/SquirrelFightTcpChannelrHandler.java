package com.lemeng.server.handler;

import com.lemeng.common.SystemManager;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.TcpHandlerService;
import com.lemeng.server.session.NettyTcpSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by zhumeilu on 17/9/7.
 */
public class SquirrelFightTcpChannelrHandler extends ChannelInboundHandlerAdapter {
    private int loss_connect_time = 0;
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

    /**
     * 处理超时
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                System.out.println("5 秒没有接收到客户端的信息了");
                if (loss_connect_time > 2) {
                    System.out.println("关闭这个不活跃的channel");
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
