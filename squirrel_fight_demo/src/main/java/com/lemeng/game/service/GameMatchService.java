package com.lemeng.game.service;

import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.game.domain.*;
import com.lemeng.game.manager.IBoxManager;
import com.lemeng.game.manager.INutManager;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import com.lemeng.user.domain.User;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**将所有队列中的Team匹配，开始一场对局
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 11:45
 */
@Component
public class GameMatchService implements Runnable{

    @Autowired
    private IBoxManager boxManager;
    @Autowired
    private INutManager nutManager;


    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int MAX_THREAD_NUM = 50;

    private ExecutorService executorService =
            Executors.newFixedThreadPool(MAX_THREAD_NUM);

    public void run() {

        while(true){
            //获取gameQuenue
            BlockingQueue<Team> teamQuenue = SystemManager.getInstance().getTeamQuenue();
            try {

                if(teamQuenue.size()>=5) {
                    Team team1 = teamQuenue.take();
                    Team team2 = teamQuenue.take();
                    Team team3 = teamQuenue.take();
                    Team team4 = teamQuenue.take();
                    Team team5 = teamQuenue.take();
                    Game game = new Game();
                    game.getTeamList().add(team1);
                    game.getTeamList().add(team2);
                    game.getTeamList().add(team3);
                    game.getTeamList().add(team4);
                    game.getTeamList().add(team5);
                    game.setId(SystemManager.getInstance().getIdGenertor().generateGameId());
                    game.setBoxList(null);
                    game.setNutList(null);
                    game.setMap(null);
                    game.setBeginDate(new Date());
                    SystemManager.getInstance().getGameConcurrentHashMap().put(game.getId(),game);
                    //向game中的所有玩家广播游戏找到，

                    //初始化角色
                    initGamePlayer(game);

                    //todo


                }else if(teamQuenue.size()>1){
                    Game game = new Game();
                    for (int i = 0; i < teamQuenue.size(); i++) {
                        game.getTeamList().add(teamQuenue.take());
                    }
                    game.setId(SystemManager.getInstance().getIdGenertor().generateGameId());

                    game.setMap(null);
                    game.setBeginDate(new Date());
                    //队伍数量不足5个，添加到可加入游戏队列。
                    SystemManager.getInstance().getCanJoinGameQuenue().add(game);
                    SystemManager.getInstance().getGameConcurrentHashMap().put(game.getId(),game);
                    //初始化角色
                    initGamePlayer(game);

                    //向game中的所有玩家广播游戏找到，

                    //todo
                }else{
                    //如果没有，则等待1s
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }

    }

    /**
     * 初始化游戏内的所有角色
     */
    private void initGamePlayer(Game game) {
        List<Team> teamList = game.getTeamList();
        for (Team team :    teamList) {
            List<User> userList = team.getUserList();
            team.setPlayerList(initPlayerList(userList));
        }
    }

    /**
     * 生成player
     * @param userList
     * @return
     */
    private List<Player> initPlayerList(List<User> userList){
        List<Player> playerList = new ArrayList<Player>();
        for (User user :    userList) {
            Player player = new Player();
            player.setUserId(user.getId());
            player.setId(user.getId());

            player.setAssistNum(0);
            player.setKillNum(0);
            player.setDeathNum(0);

            playerList.add(player);
        }
        return playerList;
    }

    //初始化箱子
    private List<Box> initBox(Game game) {
        List<Box> boxListByMap = boxManager.getBoxListByMap(game.getMap());
        for (Box box : boxListByMap) {
            box.setGameId(game.getId());
            box.setStatue(1);
            box.setId(SystemManager.getInstance().getIdGenertor().generateBoxId());
        }
        return boxListByMap;
    }

    //初始化坚果
    private List<Nut> initNut(Game game) {

        List<Nut> nutList = nutManager.getNutListByMap(game.getMap());
        for (Nut nut : nutList) {
            nut.setGameId(game.getId());
            nut.setStatue(1);
            nut.setId(SystemManager.getInstance().getIdGenertor().generateNutId());
        }
        return nutList;

    }

    private Game initGame(Team... teams){
        Game game = new Game();
        for (Team team :    teams) {
            game.getTeamList().add(team);
        }
        game.setId(SystemManager.getInstance().getIdGenertor().generateGameId());
        String map = "";
        String weather = "";
        game.setWeather(weather);
        game.setMap(map);
        game.setBeginDate(new Date());
        List<Nut> nutList = initNut(game);
        List<Box> boxList = initBox(game);
        game.setNutList(nutList);
        game.setBoxList(boxList);

        return game;
    }

    private void buildMessage(Game game){

        //构建消息
        GameCommand.FindedGameRequestCommand.Builder gameBuilder = GameCommand.FindedGameRequestCommand.newBuilder();
        gameBuilder.setWeather(game.getWeather());
        gameBuilder.setMapName(game.getMap());
        for (Nut nut : game.getNutList()) {
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
        for (Box box : game.getBoxList()) {
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

        //获取对局中所有的玩家
        List<Player> allPlayer = new ArrayList<Player>();
        for (Team team :    game.getTeamList()) {
            allPlayer.addAll(team.getPlayerList());
        }

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
    }
    protected void logStackTrace( Exception e ) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        // e.printStackTrace();
        logger.error(writer.toString());
    }
}
