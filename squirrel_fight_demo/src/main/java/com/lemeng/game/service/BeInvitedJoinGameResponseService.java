package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
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


/**被邀请组队响应
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
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            GameCommand.BeInvitedJoinGameResponseCommand beInvitedJoinGameResponseCommand= GameCommand.BeInvitedJoinGameResponseCommand.parseFrom(bodyBytes);
            int statue = beInvitedJoinGameResponseCommand.getStatue();
            int teamId = beInvitedJoinGameResponseCommand.getTeamId();
            String msg = beInvitedJoinGameResponseCommand.getMsg();
            //同意
            if(statue==1){



            }else{
                //拒绝
                Team team = (Team)SystemManager.getInstance().getTeamMap().get(teamId);

                //获取队长channel
                Integer headerId = team.getHeader().getId();
                Channel channel = (Channel) SystemManager.getInstance().getUserChannelMap().get(headerId);
                //向队长发送响应
                GameCommand.InviteJoinGameResponseCommand.Builder builder = GameCommand.InviteJoinGameResponseCommand.newBuilder();
                builder.setStatue(2);
                builder.setMsg(msg);
                //设置个人信息

                byte[] body = builder.build().toByteArray();

                SquirrelFightTcpMessage retMessage = new SquirrelFightTcpMessage();
                retMessage.setCmd(Const.InviteJoinGameResponseCommand);     //邀请加入游戏响应
                retMessage.setLength(body.length);
                retMessage.setBody(body);
                channel.writeAndFlush(retMessage);

            }

        } catch (InvalidProtocolBufferException e) {
            logStackTrace(e);
        }
    }
}
