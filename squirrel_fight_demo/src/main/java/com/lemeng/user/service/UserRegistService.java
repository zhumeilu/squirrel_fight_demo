package com.lemeng.user.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
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
@Component("UserRegistService")
public class UserRegistService extends AbstractTcpService {

    @Autowired
    private IUserManager userManager;

    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        try {
            UserCommand.RegistRequestCommand registCommand = UserCommand.RegistRequestCommand.parseFrom(bodyBytes);

            String mobile=registCommand.getMobile();
            String password=registCommand.getPassword();
            String verifyCode=registCommand.getVerifyCode();
            logger.info("--------接收到------mobile:"+mobile+"------password:"+password);
            User registUser = null;
            try{
                registUser = userManager.regist(mobile, password,verifyCode);

            }catch (Exception e){
                e.printStackTrace();
            }
            if(registUser!=null){
                //注册成功
                UserCommand.LoginResponseCommand.Builder builder = UserCommand.LoginResponseCommand.newBuilder();
                builder.setCode(1);
                builder.setMsg("注册成功");
                UserCommand.UserInfoCommand.Builder userBuilder = UserCommand.UserInfoCommand.newBuilder();
                userBuilder.setGemstone(registUser.getGemstone());
                userBuilder.setLevel(registUser.getLevel());

                userBuilder.setGoldCoin(registUser.getGoldCoin());
                userBuilder.setMobile(userBuilder.getMobile());
                userBuilder.setNickname(userBuilder.getNickname());
                userBuilder.setStatue(userBuilder.getStatue());

                //查询用户的宠物列表
                List<Pet> petList = userManager.getPetListByUserId(registUser.getId());
                for (int i = 0 ;i<petList.size();i++){
                    userBuilder.setPetList(i,petList.get(i).getType());
                }
                //查询用户的脚印列表
                List<FootPrint> footPrintList = userManager.getFootPrintListByUserId(registUser.getId());
                for (int i = 0 ;i<footPrintList.size();i++){
                    userBuilder.setFootPrintList(i,footPrintList.get(i).getType());
                }
                //查询用户的技能列表
                List<Skill> skillList = userManager.getSkillListByUserId(registUser.getId());
                for (int i = 0 ;i<skillList.size();i++){
                    userBuilder.setSkillList(i,skillList.get(i).getType());
                }

                builder.setUserInfo(userBuilder);

                SquirrelFightTcpMessage returnMessage = new SquirrelFightTcpMessage();
                returnMessage.setBody(builder.build().toByteArray());
                returnMessage.setCmd(Const.LoginResponseCommand);
                returnMessage.setLength(returnMessage.getBody().length);
                channel.writeAndFlush(returnMessage);
                logger.info("---------注册成功--------");

            }else{
                //返回注册失败
                logger.info("---------注册失败--------");
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }

}
