package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Room;
import com.lemeng.game.manager.IRoomManager;
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
@Component("CreateRoomRequestService")
public class CreateRoomRequestService extends AbstractService {

    @Autowired
    private IRoomManager roomManager;
    @Autowired
    private JedisClusterUtil jedisClusterUtil;
    public void run() {

        SquirrelFightTcpMessage tcpMessage = (SquirrelFightTcpMessage) this.message;
        byte[] bodyBytes = tcpMessage.getBody();
        try {
            GameCommand.CreateRoomRequestCommand createRoomRequestCommand = GameCommand.CreateRoomRequestCommand.parseFrom(bodyBytes);
            int userId = createRoomRequestCommand.getUserId();
            //创建房间
            Room room = roomManager.createRoom(userId);
            //返回创建房间结果
            GameCommand.CreateRoomResponseCommand.Builder builder = GameCommand.CreateRoomResponseCommand.newBuilder();
            if(room!=null){
                builder.setStatue(1);
                builder.setMsg("创建成功");
                builder.setRoomId(room.getId());
            }else{
                builder.setStatue(2);
                builder.setMsg("创建失败");
            }
            builder.setUserId(userId);
            SquirrelFightTcpMessage retMessage = new SquirrelFightTcpMessage();
            byte[] body = builder.build().toByteArray();
            retMessage.setBody(body);
            retMessage.setLength(body.length);
            retMessage.setCmd(Const.CreateRoomResponseCommand);
            channel.writeAndFlush(retMessage);
        } catch (InvalidProtocolBufferException e) {
            logStackTrace(e);
        }
    }
}
