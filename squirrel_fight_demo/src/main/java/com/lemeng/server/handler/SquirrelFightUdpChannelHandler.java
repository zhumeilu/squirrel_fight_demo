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
        SquirrelFightUdpMessage message = new SquirrelFightUdpMessage();
        message.setSender(datagramPacket.sender());
        message.setCmd(order);
        message.setBody(bodyByte);
        System.out.println("---------封装message-------------");
        UdpHandlerService handlerService = (UdpHandlerService) SystemManager.getInstance().getContext().getBean("UdpHandlerService");
        System.out.println("-----handlerService:"+ handlerService);
        handlerService.submit(channelHandlerContext,message);


    }
}
