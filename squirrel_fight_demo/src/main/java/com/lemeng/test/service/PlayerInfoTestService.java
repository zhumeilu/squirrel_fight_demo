package com.lemeng.test.service;

import com.lemeng.common.util.ConvertUtil;
import com.lemeng.game.domain.Player;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.AbstractService;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Enumeration;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 14:11
 */
@Component("PlayerInfoTestService")
public class PlayerInfoTestService extends AbstractService {
    public void run() {


        SquirrelFightUdpMessage udpMessage = (SquirrelFightUdpMessage) this.message;
        byte[] bodyBytes = udpMessage.getBody();
        logger.info("----------------收到playerInfo同步信息cmd----------------");
        try{
            GameCommand.PlayerInfoCommand playerInfoCommand = GameCommand.PlayerInfoCommand.parseFrom(bodyBytes);
            //更新本地
            InetSocketAddress sender = udpMessage.getSender();
            Player player = TestSystemManager.getInstance().getPlayer(sender);
            player.setPositionY(playerInfoCommand.getPositionY());
            player.setPositionX(playerInfoCommand.getPositionX());
            player.setPositionZ(playerInfoCommand.getPositionZ());

            //广播到所有客户端
            Enumeration allSender = TestSystemManager.getInstance().getAllSender();
            while (allSender.hasMoreElements()){
                channel.write(new DatagramPacket(Unpooled.copiedBuffer(ConvertUtil.getBytes(udpMessage.getCmd()),udpMessage.getBody()),(InetSocketAddress) allSender.nextElement()));
            }
            channel.flush();
        }catch (Exception e){

            logStackTrace(e);
        }
    }
}
