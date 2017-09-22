package com.lemeng.mall.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.mall.manager.IMallManager;
import com.lemeng.server.command.MallCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
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
public class BuySkillService extends AbstractService{

    @Autowired
    private ISkillManager skillManager;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            MallCommand.BuySkillCommand rechargeGemstoneCommand = MallCommand.BuySkillCommand.parseFrom(bodyBytes);
            int userId = rechargeGemstoneCommand.getUserId();
            String skillName = rechargeGemstoneCommand.getSkillName();
            boolean b = skillManager.buySkill(userId, skillName);
            MallCommand.BuySkillResultCommand.Builder builder = MallCommand.BuySkillResultCommand.newBuilder();
            SquirrelFightTcpMessage returnTcpMessage = new SquirrelFightTcpMessage();
            returnTcpMessage.setCmd(Const.BuySkillResultCommand);
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
