package com.lemeng.mall.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.server.command.MallCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.user.manager.IFootPrintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:购买脚印服务
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:36
 */
@Component("BuyFootPrintService")
public class BuyFootPrintService extends AbstractTcpService {

    @Autowired
    private IFootPrintManager footPrintManager;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            MallCommand.BuyFootPrintRequestCommand buyFootPrintCommand = MallCommand.BuyFootPrintRequestCommand.parseFrom(bodyBytes);
            int userId = buyFootPrintCommand.getUserId();
            String footPrintName = buyFootPrintCommand.getFootPrintName();
            //购买
            boolean b = footPrintManager.buyFootPrint(userId, footPrintName);
            //构建返回消息
            MallCommand.BuyFootPrintResponseCommand.Builder builder = MallCommand.BuyFootPrintResponseCommand.newBuilder();
            SquirrelFightTcpMessage returnTcpMessage = new SquirrelFightTcpMessage();
            returnTcpMessage.setCmd(Const.BuyFootPrintResponseCommand);
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
