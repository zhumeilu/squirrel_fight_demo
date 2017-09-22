package com.lemeng.server.message;

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

    private InetSocketAddress sender;
    private short cmd;
    private byte[] body;


    public SquirrelFightUdpMessage(short cmd,byte[] body,InetSocketAddress sender){
        this.body = body;
        this.cmd = cmd;
        this.sender = sender;
    }


    public SquirrelFightUdpMessage(){

    }
}
