package com.lemeng.test.service;

import com.lemeng.common.util.ConvertUtil;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.AbstractUdpService;
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
@Component("QuitGameTestService")
public class QuitGameTestUdpService extends AbstractUdpService {
    public void run() {


        SquirrelFightUdpMessage udpMessage = (SquirrelFightUdpMessage) this.message;
        try{
            //更新本地
            InetSocketAddress sender = udpMessage.getSender();
            TestSystemManager.getInstance().removePlayer(sender);

            //广播到所有客户端
            Enumeration allSender = TestSystemManager.getInstance().getAllSender();
            while (allSender.hasMoreElements()){
                channel.write(new DatagramPacket(Unpooled.copiedBuffer(ConvertUtil.getBytes(udpMessage.getCmd()),udpMessage.getBody()),sender));
            }
            channel.flush();
        }catch (Exception e){
            logStackTrace(e);
        }
    }
}
