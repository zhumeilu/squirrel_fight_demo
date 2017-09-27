package com.lemeng.game.service;

import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Room;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.domain.User;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**匹配中，玩家拒绝游戏
 * Description:
 * User: zhumeilu
 * Date: 2017/9/27
 * Time: 13:57
 */
public class RefuseGameService extends AbstractService{

    @Autowired
    private JedisClusterUtil jedisClusterUtil;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        try{
            GameCommand.RefuseGameCommand refuseGameRequestCommand = GameCommand.RefuseGameCommand.parseFrom(bodyBytes);
            int roomId = refuseGameRequestCommand.getRoomId();
            //从匹配队列中删除这个房间
            //暂未实现

            //向房间内所有玩家推送拒绝游戏
            Room room = (Room) jedisClusterUtil.getObject(Const.RoomPrefix + roomId);
            List<User> userList = room.getUserList();
            //循环推送消息
            for (User user : userList) {
                Channel roomTeamChannel = (Channel) SystemManager.getInstance().getUserChannelMap().get(user.getId());
                roomTeamChannel.writeAndFlush(tcpMessage);
            }

        }catch (Exception e){

        }
    }
}
