package com.lemeng.server.decoder;

import com.lemeng.server.message.SquirrelFightTcpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by jiangwenping on 17/2/8.
 */
public class TcpMessageDecoder extends MessageToMessageDecoder<ByteBuf> {


    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        SquirrelFightTcpMessage message = parseByteBuf(byteBuf);
        list.add(message);
    }

    private SquirrelFightTcpMessage parseByteBuf(ByteBuf byteBuf) {
        System.out.println("-------decoder_length:"+byteBuf.readableBytes());
        //head
        short head = byteBuf.readShort();
        //length
        int length = byteBuf.readInt();
        //cmd
        short cmd = byteBuf.readShort();
        short version = byteBuf.readByte();
        System.out.println("---------解析的head："+head+"------head:"+length+"--cmd:"+cmd+"---version:"+version);
        byte[] body = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(), body);
//        byteBuf.readBytes(body);
        System.out.println("---body:"+body.length);
        SquirrelFightTcpMessage message = new SquirrelFightTcpMessage();
        message.setHead(head);
        message.setLength(length);
        message.setCmd(cmd);
        message.setBody(body);

        return  message;
    }
}
