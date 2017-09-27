package com.lemeng.server.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;

/**
 * Description: udp消息
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:18
 */
@Getter
@Setter
public class SquirrelFightUdpMessage implements IMessage{
    public static final short MESSAGE_HEADER_FLAG = 0x2425;
    private short head;
    private int length;
    private short cmd;
    private byte version;
    private byte[] body;

    private InetSocketAddress sender;


    public SquirrelFightUdpMessage(){
        this.head = MESSAGE_HEADER_FLAG;
        this.version = 0x01;
    }

    public ByteBuf getByteBuf(){

        ByteBuf byteBuf = Unpooled.buffer(256);

        byteBuf.writeShort(head);
        byteBuf.writeInt(length);
        byteBuf.writeShort(cmd);   //cmd
        byteBuf.writeShort(version);    //version
        byteBuf.writeBytes(body);

        return byteBuf;
    }

}
