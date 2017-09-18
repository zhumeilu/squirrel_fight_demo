package com.lemeng.server.bootstrap;

import com.lemeng.Const;
import com.lemeng.model.Role;
import com.lemeng.proto.BaseCommand;
import com.lemeng.util.ConvertUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.Map;
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
        if(aShort==Const.LoginCommand){
            //登录
            logger.info("----------收到登录协议，随机生成信息返回id----------");
            Role role = (Role) Const.connections.get(sender);
            if(role==null){
                //随机生成一个角色
                Role newRole = new Role();
                newRole.setId(Const.id.getAndIncrement());
                newRole.setPosition_x(RandomUtils.nextFloat(0f,10f));
                newRole.setPosition_y(RandomUtils.nextFloat(0f,10f));
                newRole.setPosition_z(RandomUtils.nextFloat(0f,10f));
                Const.connections.put(sender,newRole);
                //返回生成的信息

                BaseCommand.UserInfoCommand.Builder builder = BaseCommand.UserInfoCommand.newBuilder();
                builder.setId(newRole.getId());
                BaseCommand.UserInfoCommand build = builder.build();

                byte[] command = ConvertUtil.getBytes((short) Const.UserInfoCommand);
                DatagramPacket datagramPacketRet = new DatagramPacket(Unpooled.copiedBuffer(command,build.toByteArray()),sender);
                channelHandlerContext.writeAndFlush(datagramPacketRet);
            }

        }else if(aShort== Const.BattleRequestCommand){
            logger.info("----------收到"+sender+"申请战斗----------");
            //申请战斗
            //如果在线人数大于或等于2人，则广播战斗协议
            int size = Const.connections.size();
            if(size>=2){
                logger.info("-----广播给所有用户开始战斗----");
                Set set = Const.connections.entrySet();
                BaseCommand.GameStartCommand.Builder builder = BaseCommand.GameStartCommand.newBuilder();
                for (Object object:set) {
                    Map.Entry<InetSocketAddress,Role> entry = (Map.Entry<InetSocketAddress,Role>)object;
                    Role role = entry.getValue();
                    BaseCommand.PositionCommand.Builder builder1 = BaseCommand.PositionCommand.newBuilder();
                    builder1.setPositionZ(role.getPosition_z());
                    builder1.setPositionY(role.getPosition_y());
                    builder1.setPositionX(role.getPosition_x());
                    builder1.setId(role.getId());
                    builder.addPositionList(builder1.build());
                }
                byte[] command = ConvertUtil.getBytes((short) Const.GameStartCommand);
                DatagramPacket datagramPacketRet = new DatagramPacket(Unpooled.copiedBuffer(command,builder.build().toByteArray()),sender);

                Enumeration keys = Const.connections.keys();
                logger.info("-------遍历所有连接信息，广播坐标-----");
                while (keys.hasMoreElements()){
                    InetSocketAddress inetSocketAddress = (InetSocketAddress)keys.nextElement();
                    logger.info("获取key为："+inetSocketAddress+"--------");
                    channelHandlerContext.write(datagramPacketRet);
                }

            }else{
                logger.info("-----人数不足2人，无法开始战斗----");
            }

        }else if(aShort==Const.GameEndCommand) {
            //退出战斗
            logger.info("----------收到"+sender+"退出战斗协议----------");
            //广播给所有人
            Enumeration keys = Const.connections.keys();
            logger.info("遍历所有连接信息");
            while (keys.hasMoreElements()){
                InetSocketAddress inetSocketAddress = (InetSocketAddress)keys.nextElement();
                logger.info("获取key为："+inetSocketAddress+"--------");
                channelHandlerContext.write(datagramPacket);
            }
            channelHandlerContext.flush();

        }else if(aShort==Const.PositionCommand){
            logger.info("----------收到"+sender+"获取上传坐标协议，返回广播协议----------");
           //上传坐标，广播给所有用户
            byte[] command = ConvertUtil.getBytes((short) Const.PositionCommand);
            byte[] positionByte = new byte[data.readableBytes()];
            data.readBytes(positionByte);
            BaseCommand.PositionCommand positionCommand = BaseCommand.PositionCommand.parseFrom(positionByte);
            //将数据更新到服务器
            Role oldRole = (Role)Const.connections.get(sender);
            oldRole.setPosition_x(positionCommand.getPositionX());
            oldRole.setPosition_y(positionCommand.getPositionY());
            oldRole.setPosition_z(positionCommand.getPositionZ());
            Const.connections.put(sender,oldRole);
            Enumeration keys = Const.connections.keys();
            logger.info("遍历所有连接信息");
            while (keys.hasMoreElements()){
                InetSocketAddress inetSocketAddress = (InetSocketAddress)keys.nextElement();
                logger.info("获取key为："+inetSocketAddress+"--------");
                DatagramPacket datagramPacket1 = new DatagramPacket(Unpooled.copiedBuffer(command, positionCommand.toByteArray()), inetSocketAddress);
                channelHandlerContext.write(datagramPacket1);
            }

            channelHandlerContext.flush();

        }else if(aShort==Const.LogoutCommand){

            logger.info("----------收到"+sender+"退出游戏协议----------");
            Const.connections.remove(sender);

            Enumeration keys = Const.connections.keys();
            logger.info("遍历所有连接信息");
            while (keys.hasMoreElements()){
                InetSocketAddress inetSocketAddress = (InetSocketAddress)keys.nextElement();
                logger.info("获取key为："+inetSocketAddress+"--------");
                channelHandlerContext.write(datagramPacket);
            }

            channelHandlerContext.flush();

        }

    }

}
