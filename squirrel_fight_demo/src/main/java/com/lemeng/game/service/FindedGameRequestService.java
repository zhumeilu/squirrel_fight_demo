package com.lemeng.game.service;

import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.game.domain.*;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**获取匹配Team，然后对Team里面的玩家进行广播
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:25
 */
public class FindedGameRequestService implements Runnable {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    public void run() {
        try {

            //获取2-6个匹配Team
            List<Team> matchTeam = SystemManager.getInstance().getMatchTeam();

            if(matchTeam==null)
                return;
            //将对局的所有玩家的信息都广播给对局的玩家
            //初始化地图，坚果，天气，箱子等信息

            //初始化坚果集合


            //初始化箱子集合
            String mapName = "";
            String weather = "";
            //初始化game
            Game game = new Game();
            game.setId(SystemManager.getInstance().getIdGenertor().generateGameId());
            game.setBeginDate(new Date());
            game.setMap(mapName);
            game.setWeather(weather);
            List<Player> allPlayer = new ArrayList<Player>();
            for (Team team : matchTeam) {
                allPlayer.addAll(team.getPlayerList());
            }
            game.setPlayerList(allPlayer);
            List<Nut> nutList = initNut();
            List<Box> boxList = initBox();
            game.setNutList(nutList);
            game.setBoxList(boxList);
            //构建消息
            GameCommand.FindedGameRequestCommand.Builder gameBuilder = GameCommand.FindedGameRequestCommand.newBuilder();
            gameBuilder.setWeather(game.getWeather());
            gameBuilder.setMapName(game.getMap());
            for (Nut nut : nutList) {
                GameCommand.NutCommand.Builder nutBuilder = GameCommand.NutCommand.newBuilder();
                nutBuilder.setGameId(game.getId());
                nutBuilder.setPositionX(nut.getPositionX());
                nutBuilder.setPositionY(nut.getPositionY());
                nutBuilder.setPositionZ(nut.getPositionZ());
                nutBuilder.setRotX(nut.getRotX());
                nutBuilder.setRotY(nut.getRotY());
                nutBuilder.setRotZ(nut.getRotZ());
                nutBuilder.setId(nut.getId());
                nutBuilder.setName(nut.getName());
                gameBuilder.addNut(nutBuilder);

            }
            for (Box box : boxList) {
                GameCommand.BoxCommand.Builder boxBuilder = GameCommand.BoxCommand.newBuilder();
                boxBuilder.setId(box.getId());
                boxBuilder.setGameId(game.getId());
                boxBuilder.setPositionX(box.getPositionX());
                boxBuilder.setPositionY(box.getPositionY());
                boxBuilder.setPositionZ(box.getPositionZ());
                boxBuilder.setRotX(box.getRotX());
                boxBuilder.setRotY(box.getRotY());
                boxBuilder.setRotZ(box.getRotZ());
                gameBuilder.addBox(boxBuilder);
            }
//


            for (Player player: allPlayer) {
                //构建消息
                GameCommand.FullPlayerInfoCommand.Builder fullPlayerInfoBulder = GameCommand.FullPlayerInfoCommand.newBuilder();
                fullPlayerInfoBulder.setActionName(player.getActionName());

                fullPlayerInfoBulder.setAssistNum(player.getAssistNum());
                fullPlayerInfoBulder.setGameId(game.getId());
                gameBuilder.addPlayerInfoList(fullPlayerInfoBulder);
            }
            byte[] retBody = gameBuilder.build().toByteArray();
            SquirrelFightTcpMessage tcpMessage = new SquirrelFightTcpMessage();
            tcpMessage.setCmd(Const.FindedGameRequestCommand);
            tcpMessage.setLength(retBody.length);
            tcpMessage.setBody(retBody);

            //发送消息
            for (Player player: allPlayer) {
                Integer userId = player.getUserId();
                Channel channel = (Channel) SystemManager.getInstance().getUserChannelMap().get(userId);
                channel.writeAndFlush(tcpMessage);
            }

        } catch (Exception e) {
            logStackTrace(e);
        }
    }

    //初始化箱子
    private List<Box> initBox() {
        return null;
    }

    //初始化坚果
    private List<Nut> initNut() {
        return null;
    }

    protected void logStackTrace( Exception e ) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        // e.printStackTrace();
        logger.error(writer.toString());
    }
}
