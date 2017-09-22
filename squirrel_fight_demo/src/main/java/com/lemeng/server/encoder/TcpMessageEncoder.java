package com.lemeng.server.encoder;

import com.lemeng.server.message.SquirrelFightTcpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by jiangwenping on 17/2/8.
 */
public class TcpMessageEncoder extends MessageToByteEncoder<SquirrelFightTcpMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, SquirrelFightTcpMessage msg, ByteBuf byteBuf) throws Exception {
        writeByteBuf(byteBuf,msg);
    }

    public void writeByteBuf(ByteBuf byteBuf,SquirrelFightTcpMessage netMessage) throws Exception {
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
    }

}
