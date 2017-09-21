package com.lemeng.user.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.server.command.UserCommand;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.domain.FootPrint;
import com.lemeng.user.domain.Pet;
import com.lemeng.user.domain.Skill;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IUserManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:登录服务
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:36
 */
@Component("UserLoginService")
public class UserLoginService extends AbstractService{

    @Autowired
    private IUserManager userManager;

    public void run() {

        byte[] bodyBytes = message.getBody();
        try {
            UserCommand.LoginCommand loginCommand = UserCommand.LoginCommand.parseFrom(bodyBytes);

            String mobile=loginCommand.getMobile();
            String password=loginCommand.getPassword();
            User login = userManager.login(mobile, password);
            if(login!=null){
                //返回登录信息
                UserCommand.LoginResultCommand.Builder builder = UserCommand.LoginResultCommand.newBuilder();
                builder.setCode(1);
                builder.setMsg("登录成功");
                UserCommand.UserInfoCommand.Builder userBuilder = UserCommand.UserInfoCommand.newBuilder();
                userBuilder.setGemstone(login.getGemstone());
                userBuilder.setLevel(login.getLevel());

                userBuilder.setGoldCoin(login.getGoldCoin());
                userBuilder.setMobile(userBuilder.getMobile());
                userBuilder.setNickname(userBuilder.getNickname());
                userBuilder.setStatue(userBuilder.getStatue());

                List<Pet> petList = userManager.getPetListByUserId(login.getId());
                for (int i = 0 ;i<petList.size();i++){
                    userBuilder.setPetList(i,petList.get(i).getType());
                }
                List<FootPrint> footPrintList = userManager.getFootPrintListByUserId(login.getId());
                for (int i = 0 ;i<footPrintList.size();i++){
                    userBuilder.setFootPrintList(i,footPrintList.get(i).getType());
                }

                List<Skill> skillList = userManager.getSkillListByUserId(login.getId());
                for (int i = 0 ;i<skillList.size();i++){
                    userBuilder.setSkillList(i,skillList.get(i).getType());
                }

                builder.setUserInfo(userBuilder);

                byte[] header = buildHeader();
                channel.write(new DatagramPacket(Unpooled.copiedBuffer(header,builder.build().toByteArray()),message.getSender()));
                System.out.println("---------登录成功--------");

            }else{
                //返回登录失败
                System.out.println("---------登录失败--------");

            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }



    }

    private byte[] buildHeader() {
        return new byte[1];
    }
}
