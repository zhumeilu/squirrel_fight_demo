package com.lemeng.server.encoder;

import com.lemeng.server.message.SquirrelFightTcpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by jiangwenping on 17/2/8.
 */
public class TcpMessageEncoder extends MessageToMessageEncoder<SquirrelFightTcpMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, SquirrelFightTcpMessage msg, List<Object> out) throws Exception {
        ByteBuf netMessageBuf = createByteBuf(msg);
        out.add(netMessageBuf);
    }

    public ByteBuf createByteBuf(SquirrelFightTcpMessage netMessage) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer(256);
        //编写head
        //长度
        byteBuf.writeInt(0);
        //设置内容
        byteBuf.writeByte(netMessage.getVersion());
        byteBuf.writeShort(netMessage.getCmd());
        //编写body

        byteBuf.writeBytes(netMessage.getBody());

        return byteBuf;
    }
}
