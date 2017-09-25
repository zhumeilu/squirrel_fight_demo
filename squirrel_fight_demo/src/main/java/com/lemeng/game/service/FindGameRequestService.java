package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.game.manager.ITeamManager;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.domain.User;
import com.lemeng.user.mapper.UserMapper;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**寻找游戏
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("FindGameRequestService")
public class FindGameRequestService extends AbstractService {

    @Autowired
    private UserMapper userMapper;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            GameCommand.FindGameRequestCommand findGameRequestCommand= GameCommand.FindGameRequestCommand.parseFrom(bodyBytes);
            int headerId = findGameRequestCommand.getHeaderId();    //队长id
            int teamId = findGameRequestCommand.getTeamId();        //队伍id
            //加入匹配队列，进行匹配（创建一个list，循环list查询水平相近的3对玩家，包括已经在对局中的？？）



        } catch (InvalidProtocolBufferException e) {
            logStackTrace(e);
        }
    }
}
