package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.server.service.AbstractUdpService;
import com.lemeng.user.domain.User;
import com.lemeng.user.mapper.UserMapper;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**邀请玩家加入游戏
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("InviteJoinGameRequestService")
public class InviteJoinGameRequestService extends AbstractTcpService {

    @Autowired
    private UserMapper userMapper;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        try {
            GameCommand.InviteJoinGameRequestCommand inviteJoinGameRequestCommand= GameCommand.InviteJoinGameRequestCommand.parseFrom(bodyBytes);
            //邀请人
            int userId = inviteJoinGameRequestCommand.getUserId();
            //被邀请人
            int invitedId = inviteJoinGameRequestCommand.getInvitedId();
            //房间id
            int roomId = inviteJoinGameRequestCommand.getRoomId();
            //构建转发消息
            User inviter = userMapper.selectById(userId);

            GameCommand.BeInvitedJoinGameRequestCommand.Builder builder = GameCommand.BeInvitedJoinGameRequestCommand.newBuilder();
            builder.setInviterId(userId);
            builder.setInviterName(inviter.getNickname());
            builder.setRoomId(roomId);

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
