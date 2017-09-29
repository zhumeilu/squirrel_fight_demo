package com.lemeng.mall.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.mall.manager.IMallManager;
import com.lemeng.server.command.MallCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.server.service.AbstractUdpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:登录服务
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:36
 */
@Component("RechargeGemstoneService")
public class RechargeGemstoneUdpService extends AbstractTcpService {

    @Autowired
    private IMallManager mallManager;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            MallCommand.RechargeGemstoneRequestCommand rechargeGemstoneCommand = MallCommand.RechargeGemstoneRequestCommand.parseFrom(bodyBytes);
            int userId = rechargeGemstoneCommand.getUserId();
            int count = rechargeGemstoneCommand.getCount();
            boolean b = mallManager.rechargeGemstone(userId, count);
            MallCommand.RechargeGemstoneResponseCommand.Builder builder = MallCommand.RechargeGemstoneResponseCommand.newBuilder();
            SquirrelFightTcpMessage returnTcpMessage = new SquirrelFightTcpMessage();
            returnTcpMessage.setCmd(Const.RechargeGemstoneResponseCommand);
            if(b){
                builder.setCode(1);
                builder.setMsg("充值成功");
            }else{
                builder.setCode(2);
                builder.setMsg("充值失败");
            }
            byte[] retBody = builder.build().toByteArray();
            returnTcpMessage.setBody(retBody);
            returnTcpMessage.setLength(retBody.length);

            channel.writeAndFlush(returnTcpMessage);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }



    }
}
