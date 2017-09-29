package com.lemeng.game.service;

import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Game;
import com.lemeng.game.domain.Player;
import com.lemeng.game.domain.Room;
import com.lemeng.game.domain.Team;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.AbstractUdpService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

/**比赛中途退出游戏
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 19:22
 */
public class QuitGameService extends AbstractUdpService {

    @Autowired
    private JedisClusterUtil jedisClusterUtil;
    public void run() {

        try{
            SquirrelFightUdpMessage udpMessage = this.message;
            byte[] bodyBytes = udpMessage.getBody();
            //game对象中删除player，

            GameCommand.QuitGameCommand quitGameCommand = GameCommand.QuitGameCommand.parseFrom(bodyBytes);
            int id = quitGameCommand.getId();
            Player player = (Player) jedisClusterUtil.getObject(Const.PlayerPrefix + id);
            //删除player
//            jedisClusterUtil.delete(Const.PlayerPrefix + id);
            SystemManager.getInstance().getPlayerConcurrentHashMap().remove(id);

            //删除tema中的player
            Integer teamId = player.getTeamId();
//            Team team = (Team) jedisClusterUtil.getObject(Const.TeamPrefix + teamId);
            Team team = SystemManager.getInstance().getTeamConcurrentHashMap().get(teamId);
            team.getPlayerList().remove(player);
            jedisClusterUtil.setObject(Const.TeamPrefix+teamId,team);
            //广播该消息

            Integer gameId = player.getGameId();
            Game game = (Game) jedisClusterUtil.getObject(Const.GamePrefix + gameId);
            game.getPlayerList();


        }catch (Exception e){
            logStackTrace(e);
        }


    }
}
