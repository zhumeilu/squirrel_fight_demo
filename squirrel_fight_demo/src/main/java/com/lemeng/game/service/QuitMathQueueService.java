package com.lemeng.game.service;

import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Player;
import com.lemeng.game.domain.Room;
import com.lemeng.game.domain.Team;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.user.domain.User;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**退出匹配队列
 * Description:
 * User: zhumeilu
 * Date: 2017/9/27
 * Time: 13:57
 */
public class QuitMathQueueService extends AbstractTcpService {

    public void run() {

        SquirrelFightTcpMessage tcpMessage =  this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        try{
            GameCommand.RefuseGameCommand refuseGameRequestCommand = GameCommand.RefuseGameCommand.parseFrom(bodyBytes);
            int roomId = refuseGameRequestCommand.getRoomId();
            int userId = refuseGameRequestCommand.getUserId();
            User user = SystemManager.getInstance().getOnlineUserMap().get(userId);
            //已组成队列team(在生成team的时，就已经将team的id设置到user中)
            if(user.getTeamId()!=null){
                //将该队伍从TeamQueue中移除
                Integer teamId = user.getTeamId();
                Team team = SystemManager.getInstance().getTeamConcurrentHashMap().get(teamId);
                BlockingQueue<Team> teamQuenue = SystemManager.getInstance().getTeamQuenue();
                teamQuenue.remove(team);
                //获取room列表
                List<Room> roomList = team.getRoomList();
                for (Room room :    roomList) {
                    //如果是该房间的玩家退出匹配队列，则向该房间玩家广播该消息
                    if(room.getId().equals(roomId)){
                        //todo
                        for (User roomUser : room.getUserList()) {
                            Channel roomTeamChannel = (Channel) SystemManager.getInstance().getUserChannelMap().get(roomUser.getId());
                            roomTeamChannel.writeAndFlush(tcpMessage);
                        }

                    }else{
                        //否则将该room加入到响应的RoomQueue
                        BlockingQueue<Room> roomQueueByRoomSize = SystemManager.getInstance().getRoomQueueByRoomSize(room.getUserList().size());
                        roomQueueByRoomSize.put(room);
                    }
                }


            }else{
                //还未组成team，在roomQueue中删除room
                Room room = SystemManager.getInstance().getRoomConcurrentHashMap().get(roomId);
                int size = room.getUserList().size();
                BlockingQueue<Room> roomQueueByRoomSize = SystemManager.getInstance().getRoomQueueByRoomSize(size);
                roomQueueByRoomSize.remove(room);
            }

        }catch (Exception e){

        }
    }
}
