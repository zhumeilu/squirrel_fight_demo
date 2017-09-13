package com.lemeng.client;

import com.lemeng.proto.BaseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;


/**
 * Created by zhumeilu on 17/9/10.
 */
public class PositionUdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        ByteBuf data = datagramPacket.content();
        BaseCommand.PositionCommand positionCommand = BaseCommand.PositionCommand.parseFrom(data.array());
        System.out.println("接收到"+datagramPacket.sender()+"的消息：x-"+positionCommand.getPositionX());
        System.out.println("接收到"+datagramPacket.sender()+"的消息：y-"+positionCommand.getPositionY());
        System.out.println("接收"+datagramPacket.sender()+"的消息：z-"+positionCommand.getPositionZ());

    }
}
