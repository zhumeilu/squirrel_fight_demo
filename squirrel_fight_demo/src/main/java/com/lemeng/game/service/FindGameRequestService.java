package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Room;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.server.service.AbstractUdpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**寻找游戏
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("FindGameRequestService")
public class FindGameRequestService extends AbstractTcpService {

    @Autowired
    private JedisClusterUtil jedisClusterUtil;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        try {
            GameCommand.FindGameRequestCommand findGameRequestCommand= GameCommand.FindGameRequestCommand.parseFrom(bodyBytes);
            int roomId = findGameRequestCommand.getRoomId();        //队伍id
            Room room = (Room) jedisClusterUtil.getObject(Const.RoomPrefix + roomId);

            //加入匹配队列，进行匹配（创建一个list，循环list查询水平相近的3对玩家，包括已经在对局中的？？）
            SystemManager.getInstance().addQuene(room);

        } catch (InvalidProtocolBufferException e) {
            logStackTrace(e);
        }
    }
}
