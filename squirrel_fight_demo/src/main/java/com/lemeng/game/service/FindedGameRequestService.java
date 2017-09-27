package com.lemeng.game.service;

import com.lemeng.common.SystemManager;
import com.lemeng.game.domain.Game;
import com.lemeng.game.domain.Player;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.service.AbstractService;
import com.lemeng.user.mapper.UserMapper;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**找到对局
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
@Component("FindedGameRequestService")
public class FindedGameRequestService extends AbstractService {

    private List<Player> playInfoList ;
    @Autowired
    private UserMapper userMapper;

    public void run() {


        try {
            //将对局的所有玩家的信息都广播给对局的玩家
            //初始化地图，坚果，天气，箱子等信息
            String mapName = "";
            String weather = "";
            //初始化坚果集合


            //初始化箱子集合


            //创建game对象，保存到redis

            Game game = new Game();

            //构建消息
            GameCommand.FindedGameRequestCommand.Builder builder = GameCommand.FindedGameRequestCommand.newBuilder();
            builder.setMapName(mapName);
            builder.setWeather(weather);


            for (Player player :playInfoList) {
                Integer userId = player.getUserId();
                Channel channel = (Channel) SystemManager.getInstance().getUserChannelMap().get(userId);
                channel.writeAndFlush(null);
            }


        } catch (Exception e) {
            logStackTrace(e);
        }
    }
}
