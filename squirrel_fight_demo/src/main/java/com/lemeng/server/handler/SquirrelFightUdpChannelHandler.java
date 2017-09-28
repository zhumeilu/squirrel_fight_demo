package com.lemeng.server.handler;

import com.lemeng.common.SystemManager;
import com.lemeng.common.util.ConvertUtil;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.UdpHandlerService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/18
 * Time: 19:11
 */
public class SquirrelFightUdpChannelHandler extends SimpleChannelInboundHandler<SquirrelFightUdpMessage> {

    Logger logger = LoggerFactory.getLogger(getClass());


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SquirrelFightUdpMessage squirrelFightUdpMessage) throws Exception {

        logger.info("---------收到命令："+squirrelFightUdpMessage.getCmd()+"-------------");

        System.out.println("---------封装message-------------");
        UdpHandlerService handlerService = (UdpHandlerService) SystemManager.getInstance().getContext().getBean("UdpHandlerService");
        System.out.println("-----handlerService:"+ handlerService);
        handlerService.submit(channelHandlerContext,squirrelFightUdpMessage);


    }
}
