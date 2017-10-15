package com.lemeng.game.service;

import com.lemeng.common.SystemManager;
import com.lemeng.game.domain.Game;
import com.lemeng.server.service.AbstractTcpService;

/**游戏结束服务
 * Description:
 * User: zhumeilu
 * Date: 2017/10/10
 * Time: 17:11
 */
public class GameOverService extends AbstractTcpService{


    public void run() {
        //解析客户端信息，获取gameid
        Integer gameId = null;
        Game game = SystemManager.getInstance().getGameConcurrentHashMap().get(gameId);
        //判断游戏时长，是否应该结束

        //将协议群发给该游戏里面的所有玩家



    }
}
