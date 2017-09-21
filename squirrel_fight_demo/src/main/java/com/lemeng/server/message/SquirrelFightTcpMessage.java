package com.lemeng.server.message;

import lombok.Getter;
import lombok.Setter;

/**
 * Description: tcp消息
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:18
 */
@Getter
@Setter
public class SquirrelFightTcpMessage {

    private short head;
    private int length;
    private short cmd;
    private byte version;
    private byte[] body;


    public SquirrelFightTcpMessage(short cmd, byte[] body){
        this.body = body;
        this.cmd = cmd;
    }


    public SquirrelFightTcpMessage(){

    }
}
