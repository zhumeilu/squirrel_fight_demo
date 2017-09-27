package com.lemeng.game.service;

import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Room;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.domain.User;
import com.lemeng.user.mapper.UserMapper;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**退出房间请求
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("QuitRoomService")
public class QuitRoomService extends AbstractService {

    @Autowired
    private JedisClusterUtil jedisClusterUtil;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        try {
            GameCommand.QuitRoomCommand quitRoomRequestCommand = GameCommand.QuitRoomCommand.parseFrom(bodyBytes);
            int roomId = quitRoomRequestCommand.getRoomId();
            Room room = (Room) jedisClusterUtil.getObject(Const.RoomPrefix + roomId);
            List<User> userList = room.getUserList();

            //退出房间，转发给房间里面的所有人
            //转发消息

            for (User user: userList) {
                Channel roomTeamChannel = (Channel) SystemManager.getInstance().getUserChannelMap().get(user.getId());
                roomTeamChannel.writeAndFlush(tcpMessage);

            }

        } catch (Exception e) {
            logStackTrace(e);
        }
    }
}
