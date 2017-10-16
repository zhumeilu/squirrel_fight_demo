package com.zml.bootstrap;

import com.zml.Const;
import com.zml.command.DemoCommand;
import com.zml.model.Player;
import com.zml.util.ConvertUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by zhumeilu on 17/9/10.
 */
public class PositionUdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

        InetSocketAddress sender = datagramPacket.sender();
        ByteBuf data = datagramPacket.content();
        //获取命令
        byte[] orderByte = new byte[2];
        data.readBytes(orderByte);
        short aShort = ConvertUtil.getShort(orderByte);
        //1登录2上传信息或者下发信息3广播位置
        if(aShort== Const.LoginRequestCommand){
            //登录
            logger.info("----------收到登录协议，随机生成信息返回id----------");
            //随机生成一个角色
           Player player = new Player();
            //返回生成的信息

            player.setId(Const.generatePlayerId());
            player.setPosition_x(0f);
            player.setPosition_z(0f);

            DemoCommand.UserInfoCommand.Builder userInfoBuilder = DemoCommand.UserInfoCommand.newBuilder();
            userInfoBuilder.setId(player.getId().toString());
            userInfoBuilder.setPositionX(player.getPosition_x());
            userInfoBuilder.setPositionZ(player.getPosition_z());
            DemoCommand.LoginResponseCommand.Builder builder = DemoCommand.LoginResponseCommand.newBuilder();
            builder.setUserInfoCommand(userInfoBuilder.build());

            byte[] command = ConvertUtil.getBytes(Const.LoginResponseCommand);
            DatagramPacket datagramPacketRet = new DatagramPacket(Unpooled.copiedBuffer(command,builder.build().toByteArray()),sender);
            channelHandlerContext.writeAndFlush(datagramPacketRet);
            //向该玩家推送所有玩家的信息
            Collection values = Const.connections.values();
            Iterator it = values.iterator();
            DemoCommand.UserInfoListCommand.Builder builder1 = DemoCommand.UserInfoListCommand.newBuilder();
            while (it.hasNext()){
                Player next = (Player)it.next();
                DemoCommand.UserInfoCommand.Builder builder2 = DemoCommand.UserInfoCommand.newBuilder();
                builder2.setId(next.getId().toString());
                builder2.setPositionX(next.getPosition_x());
                builder2.setPositionZ(next.getPosition_z());
                builder1.addUserInfoCommand(builder2);
            }
            byte[] userInfoListBytes = ConvertUtil.getBytes( Const.UserInfoListCommand);
            DatagramPacket userInfoListPkg = new DatagramPacket(Unpooled.copiedBuffer(userInfoListBytes,builder1.build().toByteArray()),sender);
            channelHandlerContext.writeAndFlush(userInfoListPkg);
            //给所有玩家发送UserInfoCommand
            Set set = Const.connections.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
                InetSocketAddress next = (InetSocketAddress)iterator.next();
                byte[] command2 = ConvertUtil.getBytes( Const.UserInfoCommand);
                DatagramPacket datagramPacketRet2 = new DatagramPacket(Unpooled.copiedBuffer(command2,userInfoBuilder.build().toByteArray()),next);
                channelHandlerContext.writeAndFlush(datagramPacketRet2);
            }

            Const.connections.put(sender,player);
            Const.players.put(player.getId(),player);
        }else if(aShort== Const.UserInfoCommand){
            //转发，做位置同步
            logger.info("----------收到位置同步协议----------");
            //申请战斗
            //如果在线人数大于或等于2人，则广播战斗协议
            byte[] body = new byte[data.readableBytes()];
            data.readBytes(body);
            DemoCommand.UserInfoCommand userInfoCommand = DemoCommand.UserInfoCommand.parseFrom(body);
            //更新player
            String id = userInfoCommand.getId();
            float positionX = userInfoCommand.getPositionX();
            float positionZ = userInfoCommand.getPositionZ();
            Player player = (Player) Const.players.get(Long.valueOf(id));
            player.setPosition_z(positionZ);
            player.setPosition_x(positionX);
            //转发
            Set set = Const.connections.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
                InetSocketAddress next = (InetSocketAddress)iterator.next();
                DatagramPacket datagramPacketRet2 = new DatagramPacket(Unpooled.copiedBuffer(orderByte,body),next);
                channelHandlerContext.writeAndFlush(datagramPacketRet2);
            }

        }

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("-------注册了一个新的连接------------");
        super.channelRegistered(ctx);
    }
}
