package com.lemeng.server.session;

import com.lemeng.common.SystemManager;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/22
 * Time: 14:52
 */
public class NettyTcpSession {

    public static final AttributeKey<Long> channel_session_id = AttributeKey
            .valueOf("channel_session_id");

    @Getter
    private Long sessionId;

    protected volatile Channel channel;

    public NettyTcpSession(Channel channel){

        SessionIdGenerator sessionIdGenerator = (SessionIdGenerator) SystemManager.getInstance().getContext().getBean("SessionIdGenerator");
        sessionId = sessionIdGenerator.generateId();
        channel.attr(channel_session_id).set(sessionId);
        this.channel = channel;

    }
}
