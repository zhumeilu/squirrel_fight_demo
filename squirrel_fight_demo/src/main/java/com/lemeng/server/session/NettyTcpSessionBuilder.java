package com.lemeng.server.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Service;

/**
 * Created by jwp on 2017/2/9.
 * 创造tcpsession 同时标记channel上的sessionId
 */
@Service
public class NettyTcpSessionBuilder{

    public static final AttributeKey<Long> channel_session_id = AttributeKey
            .valueOf("channel_session_id");

    public NettyTcpSession buildSession(Channel channel) {
        NettyTcpSession nettyTcpSession = new NettyTcpSession(channel);
        channel.attr(channel_session_id).set(nettyTcpSession.getSessionId());
        return nettyTcpSession ;
    }
}
