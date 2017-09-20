package com.lemeng.server.handler;

import com.lemeng.common.SystemManager;
import com.lemeng.common.util.ConvertUtil;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.HandlerService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/18
 * Time: 19:11
 */
public class SquirrelFightUdpChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    Logger logger = LoggerFactory.getLogger(getClass());


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

        ByteBuf data = datagramPacket.content();
        //获取命令
        byte[] orderByte = new byte[2];
        data.readBytes(orderByte);
        short order = ConvertUtil.getShort(orderByte);
        logger.info("---------收到命令："+order+"-------------");

        System.out.println("---------收到命令："+order+"-------------");
        System.out.println("---------当前handler："+this+"-------------");
        byte[] bodyByte = new byte[data.readableBytes()];
        data.readBytes(bodyByte);
        SquirrelFightUdpMessage message = new SquirrelFightUdpMessage(order,bodyByte,datagramPacket.sender());
        System.out.println("---------封装message-------------");
        HandlerService handlerService = (HandlerService) SystemManager.getInstance().getContext().getBean("HandlerService");
        System.out.println("-----handlerService:"+ handlerService);
        handlerService.submit(channelHandlerContext,message);


    }
}
