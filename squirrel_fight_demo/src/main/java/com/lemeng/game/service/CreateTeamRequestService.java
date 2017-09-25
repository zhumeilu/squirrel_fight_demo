package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.game.domain.Team;
import com.lemeng.game.manager.ITeamManager;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.server.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**创建组队
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("CreateTeamRequestService")
public class CreateTeamRequestService extends AbstractService {

    @Autowired
    private ITeamManager teamManager;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        //解析数据，获取moible
        try {
            GameCommand.CreateTeamRequestCommand createTeamRequestCommand = GameCommand.CreateTeamRequestCommand.parseFrom(bodyBytes);
            int userId = createTeamRequestCommand.getUserId();
            //创建组队
            Team team = teamManager.createTeam(userId);
            //返回创建组队结果
            //
            GameCommand.CreateTeamResponseCommand.Builder builder = GameCommand.CreateTeamResponseCommand.newBuilder();
            builder.setStatue(1);
            builder.setMsg("创建成功");
            builder.setTeamId(team.getId());
            builder.setUserId(userId);
            SquirrelFightTcpMessage retMessage = new SquirrelFightTcpMessage();
            byte[] body = builder.build().toByteArray();
            retMessage.setBody(body);
            retMessage.setLength(body.length);
            retMessage.setCmd(Const.CreateTeamResponseCommand);
            channel.writeAndFlush(retMessage);
        } catch (InvalidProtocolBufferException e) {
            logStackTrace(e);
        }
    }
}
