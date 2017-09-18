package com.lemeng.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;


/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/18
 * Time: 19:11
 */
public class SquirrelFightUdpChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

    }
}
