package com.lemeng.user.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.server.command.UserCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.server.service.AbstractUdpService;
import com.lemeng.user.domain.FootPrint;
import com.lemeng.user.domain.Pet;
import com.lemeng.user.domain.Skill;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:tcp登录服务
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:36
 */
@Component("UserLoginService")
public class UserLoginUdpService extends AbstractTcpService {

    @Autowired
    private IUserManager userManager;

    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        try {
            UserCommand.LoginRequestCommand loginCommand = UserCommand.LoginRequestCommand.parseFrom(bodyBytes);

            String mobile=loginCommand.getMobile();
            String password=loginCommand.getPassword();
            System.out.println("--------接收到------mobile:"+mobile+"------password:"+password);
            User login = null;
            try{
                login = userManager.login(mobile, password);

            }catch (Exception e){
                e.printStackTrace();
            }
            if(login!=null){
                System.out.println("------构建返回消息----------");
                //返回登录信息
                UserCommand.LoginResponseCommand.Builder builder = UserCommand.LoginResponseCommand.newBuilder();
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

                SquirrelFightTcpMessage returnMessage = new SquirrelFightTcpMessage();
                returnMessage.setBody(builder.build().toByteArray());
                returnMessage.setCmd(Const.LoginResponseCommand);
                returnMessage.setLength(returnMessage.getBody().length);
                channel.writeAndFlush(returnMessage);
                System.out.println("---------登录成功--------");
                //保存user_channel
                SystemManager.getInstance().getUserChannelMap().put(login.getId(),channel);
            }else{
                //返回登录失败
                System.out.println("---------登录失败--------");

            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }

}
