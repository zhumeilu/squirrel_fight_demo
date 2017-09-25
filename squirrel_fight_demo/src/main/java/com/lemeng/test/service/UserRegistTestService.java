package com.lemeng.test.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.util.ConvertUtil;
import com.lemeng.game.domain.Player;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.command.UserCommand;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.AbstractService;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Enumeration;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 14:11
 */
@Component("UserRegistTestService")
public class UserRegistTestService extends AbstractService {

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
            GameCommand.PlayerInfoCommand.Builder retBuilder = GameCommand.PlayerInfoCommand.newBuilder();

            Integer id = TestSystemManager.getInstance().getId();
            player.setId(id);
            player.setAttack(10);
            player.setGunRoll(0f);
            player.setNickname("player_"+id);
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
            TestSystemManager.getInstance().savePlayer(sender,player);
            //构建返回信息
            retBuilder.setId(player.getId());
            retBuilder.setAttack(player.getAttack());
            retBuilder.setGunRoll(player.getGunRoll());
            retBuilder.setGunRot(player.getGunRot());
            retBuilder.setHp(player.getHp());
            retBuilder.setNickname(player.getNickname());
            retBuilder.setPositionX(player.getPositionX());
            retBuilder.setPositionY(player.getPositionY());
            retBuilder.setPositionZ(player.getPositionZ());
            retBuilder.setRotX(player.getRotX());
            retBuilder.setRotY(player.getRotY());
            retBuilder.setRotZ(player.getRotZ());
            retBuilder.setTeamId(player.getTeamId());
            System.out.println("Const.PlayerInfoCommand-----"+Const.PlayerInfoCommand+"----Const.NewPlayerJoinGameRequestCommand---"+Const.NewPlayerJoinGameRequestCommand);
            //返回初始化后的信息
            DatagramPacket datagramPacket = new DatagramPacket(Unpooled.copiedBuffer(ConvertUtil.getBytes(Const.PlayerInfoCommand),retBuilder.build().toByteArray()),sender);
            channel.write(datagramPacket);
            //向其他玩家广播
            Enumeration allSender = TestSystemManager.getInstance().getAllSender();
            while (allSender.hasMoreElements()){
                channel.write(new DatagramPacket(Unpooled.copiedBuffer(ConvertUtil.getBytes(Const.NewPlayerJoinGameRequestCommand),retBuilder.build().toByteArray()),(InetSocketAddress) allSender.nextElement()));
            }

            channel.flush();
        } catch (InvalidProtocolBufferException e) {
            logStackTrace(e);
        }
    }
}
