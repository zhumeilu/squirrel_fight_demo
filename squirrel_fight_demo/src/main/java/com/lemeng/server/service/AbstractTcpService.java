package com.lemeng.server.service;

import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/14
 * Time: 16:23
 */
@Getter
@Setter
public abstract class AbstractTcpService implements Runnable{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Channel channel;
    protected SquirrelFightTcpMessage message;

    /** 记录异常调用
     * @param e
     */
    protected void logStackTrace( Exception e ) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        // e.printStackTrace();
        logger.error(writer.toString());
    }

}
