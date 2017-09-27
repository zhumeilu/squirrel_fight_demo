package com.lemeng.server.encoder;

import com.lemeng.server.message.SquirrelFightUdpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by jiangwenping on 17/2/8.
 */
public class UdpMessageEncoder extends MessageToMessageEncoder<SquirrelFightUdpMessage> {

    public ByteBuf writeByteBuf(SquirrelFightUdpMessage netMessage) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer(1024);
        //head
        byteBuf.writeShort(netMessage.getHead());
        byte[] body = netMessage.getBody();
        //length
        byteBuf.writeInt(body.length);
        //cmd
        byteBuf.writeShort(netMessage.getCmd());
        //version
        byteBuf.writeByte(netMessage.getVersion());
        System.out.println(body.length);
        //body
        byteBuf.writeBytes(netMessage.getBody());
        System.out.println("--------encoder_length:"+byteBuf.readableBytes());
        return byteBuf;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, SquirrelFightUdpMessage squirrelFightUdpMessage, List<Object> list) throws Exception {
        ByteBuf byteBuf = writeByteBuf(squirrelFightUdpMessage);
        list.add(new DatagramPacket(byteBuf,squirrelFightUdpMessage.getSender()));
    }
}
