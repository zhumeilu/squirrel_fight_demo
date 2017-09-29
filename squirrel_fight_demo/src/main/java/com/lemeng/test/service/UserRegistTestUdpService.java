package com.lemeng.test.service;

import com.lemeng.common.Const;
import com.lemeng.common.util.ConvertUtil;
import com.lemeng.game.domain.Player;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.command.UserCommand;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.AbstractUdpService;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 14:11
 */
@Component("UserRegistTestService")
public class UserRegistTestUdpService extends AbstractUdpService {

    public void run() {


        SquirrelFightUdpMessage udpMessage = (SquirrelFightUdpMessage) this.message;
        byte[] bodyBytes = udpMessage.getBody();

        try {
            UserCommand.RegistRequestCommand loginCommand = UserCommand.RegistRequestCommand.parseFrom(bodyBytes);

            String mobile=loginCommand.getMobile();
            String password=loginCommand.getPassword();
            String verifyCode = loginCommand.getVerifyCode();
            logger.info("----------mobile:"+mobile);
            logger.info("-----------password:"+password);
            logger.info("--------------verifyCode:"+verifyCode);
            //随机成功用户信息
            Player player = new Player();
//            GameCommand.PlayerInfoCommand.Builder retBuilder = GameCommand.PlayerInfoCommand.newBuilder();

            Integer id = TestSystemManager.getInstance().getId();
            player.setId(id);
            player.setAttack(10);
            player.setGunRoll(0f);
            player.setNickname(mobile);
            player.setGunRot(0f);
            player.setPositionX(-5f);
            player.setPositionY(RandomUtils.nextFloat(16,84));
            player.setPositionZ(RandomUtils.nextFloat(0,80)-60);
            player.setRotX(0f);
            player.setRotY(0f);
            player.setRotZ(0f);
            player.setHp(100);
            player.setTeamId(id);
            logger.info("-------------随机生成player"+player.toString());
            //存储sender
            InetSocketAddress sender = udpMessage.getSender();
            logger.info("--------sender:"+sender);
            TestSystemManager.getInstance().savePlayer(sender,player);
            //构建返回信息
//            retBuilder.setId(player.getId());
//            retBuilder.setAttack(player.getAttack());
//            retBuilder.setGunRoll(player.getGunRoll());
//            retBuilder.setGunRot(player.getGunRot());
//            retBuilder.setHp(player.getHp());
//            retBuilder.setNickname(player.getNickname());
//            retBuilder.setPositionX(player.getPositionX());
//            retBuilder.setPositionY(player.getPositionY());
//            retBuilder.setPositionZ(player.getPositionZ());
//            retBuilder.setRotX(player.getRotX());
//            retBuilder.setRotY(player.getRotY());
//            retBuilder.setRotZ(player.getRotZ());
//            retBuilder.setTeamId(player.generateTeamId());
            //返回初始化后的信息
            System.out.println("-----------发送PlayerInfoCommand");
//            DatagramPacket datagramPacket = new DatagramPacket(Unpooled.copiedBuffer(ConvertUtil.getBytes(Const.PlayerInfoCommand),retBuilder.build().toByteArray()),sender);
//            channel.write(datagramPacket);
            //向其他玩家广播
            Enumeration allSender = TestSystemManager.getInstance().getAllSender();
            while (allSender.hasMoreElements()){
                System.out.println("----------发送NewPlayerJoinGameRequestCommand");
//                channel.write(new DatagramPacket(Unpooled.copiedBuffer(ConvertUtil.getBytes(Const.NewPlayerJoinGameRequestCommand),retBuilder.build().toByteArray()),(InetSocketAddress) allSender.nextElement()));
            }
            //向该玩家推送所有在线玩家的信息
            GameCommand.FindedGameRequestCommand.Builder builder = GameCommand.FindedGameRequestCommand.newBuilder();
            Collection allPlayer = TestSystemManager.getInstance().getAllPlayer();
            Iterator iterator = allPlayer.iterator();
            System.out.println("--------"+allPlayer.size());
            int i = 0;
            while (iterator.hasNext()){
                Player next = (Player)iterator.next();
//                GameCommand.PlayerInfoCommand.Builder builder1 = GameCommand.PlayerInfoCommand.newBuilder();
//                builder1.setId(next.getId());
//                builder1.setAttack(next.getAttack());
//                builder1.setGunRoll(next.getGunRoll());
//                builder1.setGunRot(next.getGunRot());
//                builder1.setHp(next.getHp());
//                builder1.setNickname(next.getNickname());
//                builder1.setPositionX(next.getPositionX());
//                builder1.setPositionY(next.getPositionY());
//                builder1.setPositionZ(next.getPositionZ());
//                builder1.setRotX(next.getRotX());
//                builder1.setRotY(next.getRotY());
//                builder1.setRotZ(next.getRotZ());
//                builder1.setTeamId(next.generateTeamId());
//                builder.addPlayerInfoList(builder1);
                i++;
            }
            System.out.println("j-----------发送FindedGameRequestCommand");
            channel.write(new DatagramPacket(Unpooled.copiedBuffer(ConvertUtil.getBytes(Const.FindedGameRequestCommand),builder.build().toByteArray()),sender));

            channel.flush();
        } catch (Exception e) {
            logStackTrace(e);
        }
    }
}
