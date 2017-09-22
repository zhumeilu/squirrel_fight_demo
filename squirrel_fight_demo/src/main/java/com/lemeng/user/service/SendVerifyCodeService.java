package com.lemeng.user.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.util.LCSendVerifycode;
import com.lemeng.common.util.RandomUtil;
import com.lemeng.server.command.UserCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.domain.UserVerifyCode;
import com.lemeng.user.manager.IUserManager;
import com.lemeng.user.manager.IUserVerifyCodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Description:登录服务
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:36
 */
@Component("SendVerifyCodeService")
public class SendVerifyCodeService extends AbstractService{

    @Autowired
    IUserVerifyCodeManager userVerifyCodeManager;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        String mobile ="";
        try {
            UserCommand.SendVerifyCodeCommand sendVerifyCodeCommand = UserCommand.SendVerifyCodeCommand.parseFrom(bodyBytes);
            mobile = sendVerifyCodeCommand.getMobile();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }


        String randomNumber = RandomUtil.getRandomNumber(6);
        UserVerifyCode userVerifyCode = new UserVerifyCode();
        userVerifyCode.setCreateTime(new Date());
        userVerifyCode.setMobile(mobile);
        userVerifyCode.setVerifyCode(randomNumber);
        //保存到数据库
        userVerifyCodeManager.save(userVerifyCode);
        try {
            LCSendVerifycode.batchSendVerifycode(mobile,randomNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}