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


/**创建组队
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("InviteJoinGameRequestService")
public class InviteJoinGameRequestService extends AbstractService {

    @Autowired
    private ITeamManager teamManager;
    @Autowired
    private UserMapper userMapper;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            GameCommand.InviteJoinGameRequestCommand inviteJoinGameRequestCommand= GameCommand.InviteJoinGameRequestCommand.parseFrom(bodyBytes);
            int userId = inviteJoinGameRequestCommand.getUserId();
            int invitedId = inviteJoinGameRequestCommand.getInvitedId();
            int teamId = inviteJoinGameRequestCommand.getTeamId();
            //构建转发消息
            User inviter = userMapper.selectById(userId);

            GameCommand.BeInvitedJoinGameRequestCommand.Builder builder = GameCommand.BeInvitedJoinGameRequestCommand.newBuilder();
            builder.setInviterId(userId);
            builder.setInviterName(inviter.getNickname());
            builder.setTeamId(teamId);

            SquirrelFightTcpMessage retMessage = new SquirrelFightTcpMessage();
            byte[] body = builder.build().toByteArray();
            retMessage.setBody(body);
            retMessage.setLength(body.length);
            retMessage.setCmd(Const.BeInvitedJoinGameRequestCommand);
            //获取invitee的handler,将消息写入刷新
            Channel channel = (Channel)SystemManager.getInstance().getUserChannelMap().get(invitedId);
            channel.writeAndFlush(retMessage);
//            channel.writeAndFlush(null);
        } catch (InvalidProtocolBufferException e) {
            logStackTrace(e);
        }
    }
}
