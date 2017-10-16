package com.zml.client;

import com.zml.Const;
import com.zml.util.ConvertUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;


/**
 * Created by zhumeilu on 17/9/10.
 */
public class PositionUdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
//        InetSocketAddress sender = datagramPacket.sender();
//        ByteBuf data = datagramPacket.content();
//        //获取命令
//        byte[] orderByte = new byte[2];
//        data.readBytes(orderByte);
//        short aShort = ConvertUtil.getShort(orderByte);
//        byte[] positionByte = new byte[data.readableBytes()];
//        data.readBytes(positionByte);
//        //1登录2上传信息或者下发信息3广播位置
//
//        if(aShort== Const.LoginCommand){
//            System.out.println("--------收到"+sender+"的登录协议协议---------");
//        }else if(aShort==Const.PositionCommand){
//            System.out.println("--------收到"+sender+"的下发信息协议协议---------");
//            BaseCommand.PositionCommand positionCommand = BaseCommand.PositionCommand.parseFrom(positionByte);
//            System.out.println("收到"+sender+"的下发信息协议协议x坐标："+positionCommand.getPositionX());
//            System.out.println("收到"+sender+"的下发信息协议协议y坐标："+positionCommand.getPositionY());
//            System.out.println("收到"+sender+"的下发信息协议协议z坐标："+positionCommand.getPositionZ());
//        }else if(aShort==Const.GameStartCommand){
//            System.out.println("--------收到"+sender+"的广播坐标协议---------");
//
//        }else if(aShort==Const.LogoutCommand){
//
//        }

    }
}
