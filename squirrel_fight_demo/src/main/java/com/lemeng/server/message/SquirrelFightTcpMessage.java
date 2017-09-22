package com.lemeng.server.message;

import lombok.Getter;
import lombok.Setter;

/**
 * Description: tcp消息
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:18
 * head+length+cmd+version+body 2+4+2+1
 */
@Getter
@Setter
public class SquirrelFightTcpMessage implements IMessage{
    public static final short MESSAGE_HEADER_FLAG = 0x2425;
    private short head;
    private int length;
    private short cmd;
    private byte version;
    private byte[] body;



    public SquirrelFightTcpMessage(){

        this.head = MESSAGE_HEADER_FLAG;
        this.version = 0x01;
    }
}
