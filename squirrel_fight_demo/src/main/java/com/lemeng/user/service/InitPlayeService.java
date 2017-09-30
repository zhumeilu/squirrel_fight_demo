package com.lemeng.user.service;

import com.lemeng.common.Const;
import com.lemeng.server.command.UserCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.server.service.AbstractUdpService;
import com.lemeng.user.manager.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:登录服务
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:36
 */
@Component("InitPlayerService")
public class InitPlayeService extends AbstractTcpService {

    @Autowired
    private IUserManager userManager;
    public void run() {

        SquirrelFightTcpMessage tcpMessage =  this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取nickname
        try {
            UserCommand.InitPlayerRequestCommand initPlayerCommand = UserCommand.InitPlayerRequestCommand.parseFrom(bodyBytes);
            String nickname = initPlayerCommand.getNickname();
            Integer userId = initPlayerCommand.getUserId();

            boolean b = userManager.initUser(userId, nickname);
            UserCommand.InitPlayerResponseCommand.Builder builder = UserCommand.InitPlayerResponseCommand.newBuilder();
            SquirrelFightTcpMessage retTcpMessage = new SquirrelFightTcpMessage();
            retTcpMessage.setCmd(Const.InitPlayerResponseCommand);
            if (b) {
                builder.setCode(1);
                builder.setMsg("初始化成功");
                //设置UserInfo

                System.out.println("------成功-------");
            }else{
                builder.setCode(2);
                builder.setMsg("初始化失败");
                System.out.println("------失败-------");
            }
            byte[] retBody = builder.build().toByteArray();
            retTcpMessage.setLength(retBody.length);
            retTcpMessage.setBody(retBody);
            channel.writeAndFlush(retTcpMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
