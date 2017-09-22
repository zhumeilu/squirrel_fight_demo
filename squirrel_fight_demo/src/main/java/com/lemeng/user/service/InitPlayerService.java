package com.lemeng.user.service;

import com.lemeng.common.Const;
import com.lemeng.server.command.UserCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
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
public class InitPlayerService extends AbstractService{

    @Autowired
    private IUserManager userManager;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取nickname
        try {
            UserCommand.InitPlayerCommand initPlayerCommand = UserCommand.InitPlayerCommand.parseFrom(bodyBytes);
            String nickname = initPlayerCommand.getNickname();
            Integer userId = initPlayerCommand.getUserId();

            boolean b = userManager.initUser(userId, nickname);
            UserCommand.InitPlayerResultCommand.Builder builder = UserCommand.InitPlayerResultCommand.newBuilder();
            SquirrelFightTcpMessage retTcpMessage = new SquirrelFightTcpMessage();
            retTcpMessage.setCmd(Const.InitPlayerCommand);
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
