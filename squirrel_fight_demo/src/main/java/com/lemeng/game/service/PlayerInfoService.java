package com.lemeng.game.service;

import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Game;
import com.lemeng.game.domain.Player;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.AbstractTcpService;
import com.lemeng.server.service.AbstractUdpService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**玩家信息同步
 * Description:
 * User: zhumeilu
 * Date: 2017/9/27
 * Time: 11:54
 */
public class PlayerInfoService extends AbstractUdpService {

    @Autowired
    private JedisClusterUtil jedisClusterUtil;

    public void run() {
        //接收玩家信息，更新并广播给游戏中其他玩家
        SquirrelFightUdpMessage udpMessage = this.message;
        byte[] bodyBytes = udpMessage.getBody();
        try {
            GameCommand.SimplePlayerInfoCommand simplePlayerInfoCommand = GameCommand.SimplePlayerInfoCommand.parseFrom(bodyBytes);
            int gameId = simplePlayerInfoCommand.getGameId();
            String actionName = simplePlayerInfoCommand.getActionName();
            float positionX = simplePlayerInfoCommand.getPositionX();
            float positionZ = simplePlayerInfoCommand.getPositionZ();
            float positionY = simplePlayerInfoCommand.getPositionY();


            Game game = (Game) jedisClusterUtil.getObject(Const.GamePrefix + gameId);
            Set<Integer> playerIdList = game.getPlayerList();
            for (Integer playerId : playerIdList) {
                Player player = jedisClusterUtil.getObject(Const.PlayerPrefix+playerId);
                Channel channel = (Channel) SystemManager.getInstance().getUserChannelMap().get(player.getUserId());
                channel.writeAndFlush(udpMessage);
            }

        } catch (Exception e) {
            logStackTrace(e);
        }


    }
}
