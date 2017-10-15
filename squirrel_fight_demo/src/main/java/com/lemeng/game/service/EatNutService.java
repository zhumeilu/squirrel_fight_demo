package com.lemeng.game.service;

import com.lemeng.common.SystemManager;
import com.lemeng.game.domain.Game;
import com.lemeng.game.domain.Nut;
import com.lemeng.game.domain.Player;
import com.lemeng.game.domain.Team;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.service.AbstractUdpService;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

/**吃坚果
 * Description:
 * User: zhumeilu
 * Date: 2017/9/27
 * Time: 11:58
 */
public class EatNutService extends AbstractUdpService {

    //当坚果被吃掉时，广播给其他玩家做同步

    public void run() {

        try{
            GameCommand.EatCommand eatCommand = GameCommand.EatCommand.parseFrom(message.getBody());
            int id = eatCommand.getId();        //玩家id
            String name = eatCommand.getName(); //坚果类型
            int nutId = 1;
            //获取坚果对象
            Nut nut = SystemManager.getInstance().getNutConcurrentHashMap().get(nutId);
            //构建返回消息


            Player player = SystemManager.getInstance().getPlayerConcurrentHashMap().get(id);
            //根据坚果类型，给player添加经验，更新player
            player.setExp(player.getExp()+nut.getExp());


            Integer gameId = player.getGameId();
            Game game = SystemManager.getInstance().getGameConcurrentHashMap().get(gameId);
            List<Player> playerList = new ArrayList<Player>();
            List<Team> teamList = game.getTeamList();
            for (Team team :    teamList) {
                playerList.addAll(team.getPlayerList());
            }
            for (Player player1 :    playerList) {
                Channel channel = (Channel) SystemManager.getInstance().getUserChannelMap().get(player1.getUserId());
                channel.writeAndFlush(null);
            }

            //广播给玩家

        }catch (Exception e){

        }


    }
}
