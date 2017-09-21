package com.lemeng.server.service;

import com.lemeng.server.message.SquirrelFightUdpMessage;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/14
 * Time: 16:23
 */
@Getter
@Setter
public abstract class AbstractService implements Runnable{
    protected Channel channel;
    protected SquirrelFightUdpMessage message;


}
