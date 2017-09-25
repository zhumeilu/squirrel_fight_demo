package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**退出房间请求
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("QuitTeamRequestService")
public class QuitTeamRequestService extends AbstractService {

    @Autowired
    private UserMapper userMapper;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            //退出房间，取消房间的队列匹配，转发给房间里面的所有人



        } catch (Exception e) {
            logStackTrace(e);
        }
    }
}
