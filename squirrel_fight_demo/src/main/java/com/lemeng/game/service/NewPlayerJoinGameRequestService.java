package com.lemeng.game.service;

import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**新玩家加入游戏
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("NewPlayerJoinGameRequestService")
public class NewPlayerJoinGameRequestService extends AbstractService {

    @Autowired
    private UserMapper userMapper;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            //加入房间，广播给所有玩家



        } catch (Exception e) {
            logStackTrace(e);
        }
    }
}
