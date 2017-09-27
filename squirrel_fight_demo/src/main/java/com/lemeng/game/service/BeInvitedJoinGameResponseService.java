package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Room;
import com.lemeng.game.domain.Team;
import com.lemeng.game.manager.ITeamManager;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.domain.User;
import com.lemeng.user.mapper.UserMapper;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**被邀请加入房间响应
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("BeInvitedJoinGameResponseService")
public class BeInvitedJoinGameResponseService extends AbstractService {

    @Autowired
    private ITeamManager teamManager;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisClusterUtil jedisClusterUtil;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            GameCommand.BeInvitedJoinGameResponseCommand beInvitedJoinGameResponseCommand= GameCommand.BeInvitedJoinGameResponseCommand.parseFrom(bodyBytes);
            Integer statue = beInvitedJoinGameResponseCommand.getStatue();
            Integer roomId = beInvitedJoinGameResponseCommand.getRoomId();
            String msg = beInvitedJoinGameResponseCommand.getMsg();
            //从reids中获取房间
            Room room = (Room) jedisClusterUtil.getObject(Const.RoomPrefix+roomId.toString());

            //获取队长channel
            Integer headerId = room.getHeader().getId();
            Channel channel = (Channel) SystemManager.getInstance().getUserChannelMap().get(headerId);
            //构建返回消息
            // 给房主响应结果
            GameCommand.InviteJoinGameResponseCommand.Builder builder = GameCommand.InviteJoinGameResponseCommand.newBuilder();
            builder.setStatue(statue);
            builder.setMsg(msg);
            //设置个人信息
            byte[] body = builder.build().toByteArray();

            SquirrelFightTcpMessage retMessage = new SquirrelFightTcpMessage();
            retMessage.setCmd(Const.InviteJoinGameResponseCommand);     //邀请加入游戏响应
            retMessage.setLength(body.length);
            retMessage.setBody(body);
            channel.writeAndFlush(retMessage);
            //如果同意，则给房间里面所有人推送消息
            SquirrelFightTcpMessage roomTeamMessage = new SquirrelFightTcpMessage();
            roomTeamMessage.setCmd(Const.RoomUpdateCommand);
            User header = userMapper.selectById(headerId);
            if(statue==1){
                GameCommand.RoomUpdateCommand.Builder roomUpdateBuilder = GameCommand.RoomUpdateCommand.newBuilder();
                roomUpdateBuilder.setRoomId(roomId);
                GameCommand.SimpleUserInfoCommand.Builder simpleUserInfoBuilder = GameCommand.SimpleUserInfoCommand.newBuilder();
                simpleUserInfoBuilder.setId(header.getId());
                simpleUserInfoBuilder.setNickname(header.getNickname());
                roomUpdateBuilder.setHader(simpleUserInfoBuilder);
                List<User> userList = room.getUserList();
                for (User user :userList) {
                    GameCommand.SimpleUserInfoCommand.Builder userBuilder = GameCommand.SimpleUserInfoCommand.newBuilder();
                    userBuilder.setNickname(user.getNickname());
                    userBuilder.setId(user.getId());
                    roomUpdateBuilder.addUserInfoList(userBuilder);
                }
                //给该队伍的所有人发送队伍信息
                byte[] roomTeamBody = roomUpdateBuilder.build().toByteArray();
                roomTeamMessage.setBody(roomTeamBody);
                roomTeamMessage.setLength(roomTeamBody.length);
                for (User user : userList) {
                    Channel roomTeamChannel = (Channel) SystemManager.getInstance().getUserChannelMap().get(user.getId());
                    roomTeamChannel.writeAndFlush(roomTeamMessage);
                }

            }

        } catch (InvalidProtocolBufferException e) {
            logStackTrace(e);
        }
    }
}
