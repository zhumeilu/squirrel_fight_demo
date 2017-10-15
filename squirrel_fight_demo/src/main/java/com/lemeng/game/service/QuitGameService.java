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
import io.netty.handler.codec.spdy.SpdySynReplyFrame;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**比赛中途退出游戏
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 19:22
 */
public class QuitGameService extends AbstractUdpService {

    public void run() {

        try{
            SquirrelFightUdpMessage udpMessage = this.message;
            byte[] bodyBytes = udpMessage.getBody();

            GameCommand.QuitGameCommand quitGameCommand = GameCommand.QuitGameCommand.parseFrom(bodyBytes);
            int id = quitGameCommand.getId();
            //map中删除player
            Player player = SystemManager.getInstance().getPlayerConcurrentHashMap().remove(id);

            //是否有结算，如果有，则进行结算
            //todo

            //删除team中的player
            Integer teamId = player.getTeamId();
            Team team = SystemManager.getInstance().getTeamConcurrentHashMap().get(teamId);
            team.getPlayerList().remove(player);
            //广播该消息
            Integer gameId = player.getGameId();
            Game game = SystemManager.getInstance().getGameConcurrentHashMap().get(gameId);
            List<Player> playerList = new ArrayList<Player>();
            List<Team> teamList = game.getTeamList();
            for (Team teamTemp :    teamList) {
                playerList.addAll(teamTemp.getPlayerList());
            }



        }catch (Exception e){
            logStackTrace(e);
        }


    }
}
