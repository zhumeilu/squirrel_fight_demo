package com.lemeng.mall.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.server.command.MallCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.server.service.AbstractUdpService;
import com.lemeng.user.manager.ISkillManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:购买技能服务
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:36
 */
@Component("BuySkillService")
public class BuySkillService extends AbstractTcpService {

    @Autowired
    private ISkillManager skillManager;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            MallCommand.BuySkillRequestCommand rechargeGemstoneCommand = MallCommand.BuySkillRequestCommand.parseFrom(bodyBytes);
            int userId = rechargeGemstoneCommand.getUserId();
            String skillName = rechargeGemstoneCommand.getSkillName();
            //购买
            boolean b = skillManager.buySkill(userId, skillName);
            //构建返回消息
            MallCommand.BuySkillResponseCommand.Builder builder = MallCommand.BuySkillResponseCommand.newBuilder();
            SquirrelFightTcpMessage returnTcpMessage = new SquirrelFightTcpMessage();
            returnTcpMessage.setCmd(Const.BuySkillResponseCommand);
            if(b){
                builder.setCode(1);
                builder.setMsg("购买成功");
            }else{
                builder.setCode(2);
                builder.setMsg("购买失败");
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
