package com.lemeng.game.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Game;
import com.lemeng.game.domain.Player;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.AbstractService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**玩家信息同步
 * Description:
 * User: zhumeilu
 * Date: 2017/9/27
 * Time: 11:54
 */
public class PlayerInfoService extends AbstractService{

    @Autowired
    private JedisClusterUtil jedisClusterUtil;

    public void run() {
        //接收玩家信息，更新并广播给游戏中其他玩家
        SquirrelFightUdpMessage udpMessage = (SquirrelFightUdpMessage) this.message;
        byte[] bodyBytes = udpMessage.getBody();
        try {
            GameCommand.SimplePlayerInfoCommand simplePlayerInfoCommand = GameCommand.SimplePlayerInfoCommand.parseFrom(bodyBytes);
            int gameId = simplePlayerInfoCommand.getGameId();
            String actionName = simplePlayerInfoCommand.getActionName();
            float positionX = simplePlayerInfoCommand.getPositionX();
            float positionZ = simplePlayerInfoCommand.getPositionZ();
            float positionY = simplePlayerInfoCommand.getPositionY();


            Game game = (Game) jedisClusterUtil.getObject(Const.GamePrefix + gameId);
            List<Player> playerList = game.getPlayerList();
            for (Player player : playerList) {
                Channel channel = (Channel) SystemManager.getInstance().getUserChannelMap().get(player.getUserId());
                channel.writeAndFlush(udpMessage);
            }

        } catch (Exception e) {
            logStackTrace(e);
        }


    }
}
