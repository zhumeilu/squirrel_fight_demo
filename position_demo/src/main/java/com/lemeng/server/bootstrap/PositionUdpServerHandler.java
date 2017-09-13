package com.lemeng.server.bootstrap;

import com.lemeng.Const;
import com.lemeng.model.Role;
import com.lemeng.proto.BaseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.commons.lang3.RandomUtils;

import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;



/**
 * Created by zhumeilu on 17/9/10.
 */
public class PositionUdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

        InetSocketAddress sender = datagramPacket.sender();
        ByteBuf data = datagramPacket.content();
        //获取命令
        byte[] orderByte = new byte[2];
        data.readBytes(orderByte);
        short aShort = getShort(orderByte);
        //1登录2上传信息或者下发信息3广播位置
        if(aShort==1){
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

                BaseCommand.PositionCommand.Builder builder = BaseCommand.PositionCommand.newBuilder();
                builder.setId(newRole.getId());
                builder.setPositionX(newRole.getPosition_x());
                builder.setPositionY(newRole.getPosition_y());
                builder.setPositionZ(newRole.getPosition_z());
                BaseCommand.PositionCommand build = builder.build();

                byte[] command = getBytes((short) 2);
                DatagramPacket datagramPacketRet = new DatagramPacket(Unpooled.copiedBuffer(command,build.toByteArray()),sender);
                channelHandlerContext.writeAndFlush(datagramPacketRet);

            }

        }else if(aShort==2){
           //上传坐标或者下发坐标
            byte[] command = getBytes((short) 2);
            byte[] positionByte = new byte[data.readableBytes()];
            data.readBytes(positionByte);
            BaseCommand.PositionCommand positionCommand = BaseCommand.PositionCommand.parseFrom(positionByte);
            Enumeration keys = Const.connections.keys();
            while (keys.hasMoreElements()){
                InetSocketAddress inetSocketAddress = (InetSocketAddress)keys.nextElement();
                DatagramPacket datagramPacket1 = new DatagramPacket(Unpooled.copiedBuffer(command, positionCommand.toByteArray()), inetSocketAddress);
                channelHandlerContext.write(datagramPacket1);
            }

            channelHandlerContext.flush();

        }else if(aShort==3){

            //广播地址
            Set set = Const.connections.entrySet();

            BaseCommand.PositionListCommand.Builder builder = BaseCommand.PositionListCommand.newBuilder();

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
            byte[] command = getBytes((short) 2);
            DatagramPacket datagramPacketRet = new DatagramPacket(Unpooled.copiedBuffer(command,builder.build().toByteArray()),sender);
            channelHandlerContext.writeAndFlush(datagramPacketRet);

        }

    }

    public short getShort(byte[] bytes){
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public byte[] getBytes(short data){
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }
}
