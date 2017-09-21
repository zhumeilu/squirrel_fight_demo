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
        short head = byteBuf.readShort();
        int length = byteBuf.readInt();
        short cmd = byteBuf.readShort();
        byte[] body = new byte[byteBuf.readableBytes()];

        SquirrelFightTcpMessage message = new SquirrelFightTcpMessage();
        message.setHead(head);
        message.setLength(length);
        message.setCmd(cmd);
        message.setBody(body);

        return  message;
    }
}
