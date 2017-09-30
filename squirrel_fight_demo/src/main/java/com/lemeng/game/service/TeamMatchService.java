package com.lemeng.game.service;

import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Player;
import com.lemeng.game.domain.Room;
import com.lemeng.game.domain.Team;
import com.lemeng.user.domain.User;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**匹配服务，将多个room组合成一个Team，存入到TeamQuenue
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 11:45
 */
public class TeamMatchService implements Runnable{

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JedisClusterUtil jedisClusterUtil;
    private static final int MAX_THREAD_NUM = 50;

    private ExecutorService executorService =
            Executors.newFixedThreadPool(MAX_THREAD_NUM);

    public void run() {
        //一直循环，随机一个方式组合生成一个Team
        //1+1+1+1+1,1+1+1+2,1+1+3,1+4,1+2+2,2+3,5

        while(true){

            //随机选择哪种方式进行分配组成队伍（需优化）
            int random = RandomUtils.nextInt(1, 7);
            switch (random){
                case 1:
                    Team match1 = match1();
                    if(match1!=null){
                        try {
                            SystemManager.getInstance().getTeamQuenue().put(match1);
                        } catch (InterruptedException e) {
                            logStackTrace(e);
                        }
                    }
                    break;

                case 2:
                    Team match2 = match2();
                    if(match2!=null){
                        try {
                            SystemManager.getInstance().getTeamQuenue().put(match2);
                        } catch (InterruptedException e) {
                            logStackTrace(e);

                        }
                    }
                    break;
                case 3:
                    Team match3 = match3();
                    if(match3!=null){
                        try {
                            SystemManager.getInstance().getTeamQuenue().put(match3);
                        } catch (InterruptedException e) {
                            logStackTrace(e);
                        }
                    }
                    break;
                case 4:
                    Team match4 = match4();
                    if(match4!=null){
                        try {
                            SystemManager.getInstance().getTeamQuenue().put(match4);
                        } catch (InterruptedException e) {
                            logStackTrace(e);
                        }
                    }
                    break;
                case 5:
                    Team match5 = match5();
                    if(match5!=null){
                        try {
                            SystemManager.getInstance().getTeamQuenue().put(match5);
                        } catch (InterruptedException e) {
                            logStackTrace(e);
                        }
                    }
                    break;
                case 6:
                    Team match6 = match6();
                    if(match6!=null){
                        try {
                            SystemManager.getInstance().getTeamQuenue().put(match6);
                        } catch (InterruptedException e) {
                            logStackTrace(e);
                        }
                    }
                    break;
                case 7:
                    Team match7 = match7();
                    if(match7!=null){
                        try {
                            SystemManager.getInstance().getTeamQuenue().put(match7);
                        } catch (InterruptedException e) {
                            logStackTrace(e);
                        }
                    }
                    break;
            }
            }

    }



    private Player initPlayer(User user,Integer teamId){
        Player player = new Player();
        player.setId(SystemManager.getInstance().getIdGenertor().generatePlayerId());
        player.setUserId(user.getId());
        player.setNickname(user.getNickname());

        player.setTeamId(teamId);

        return player;
    }

    //1+1+1+1+1,
    private Team match1(){
        BlockingQueue<Room> oneRoomQueue = SystemManager.getInstance().getOneRoomQueue();
        HashSet<Integer> playerList = new HashSet<Integer>();
        if(oneRoomQueue.size()>=5){
            try {
                Room room1 = oneRoomQueue.take();
                //初始化队伍
                Team team = new Team();
                team.setPlayerList(playerList);
                team.setAssistNum(0);
                team.setDeathNum(0);
                team.setKillNum(0);
                team.setId(SystemManager.getInstance().getIdGenertor().generateTeamId());
                //初始化玩家
                HashSet<Integer> playerList1 = initPlayer(room1,team.getId());
                playerList.addAll(playerList1);

//                jedisClusterUtil.setObject(Const.TeamPrefix+team.getId(),team);
                SystemManager.getInstance().getTeamConcurrentHashMap().put(team.getId(),team);
                return team;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
    //5
    private Team match7() {
        BlockingQueue<Room> fiveQueue = SystemManager.getInstance().getFiveRoomQueue();
        HashSet<Integer> playerList = new HashSet<Integer>();
        if(fiveQueue.size()>=1){
            try {
                Room room1 = fiveQueue.take();
                //初始化队伍
                Team team = new Team();
                team.setPlayerList(playerList);
                team.setAssistNum(0);
                team.setDeathNum(0);
                team.setKillNum(0);
                team.setId(SystemManager.getInstance().getIdGenertor().generateTeamId());
                //初始化玩家
                HashSet<Integer> playerList1 = initPlayer(room1,team.getId());
                playerList.addAll(playerList1);

//                jedisClusterUtil.setObject(Const.TeamPrefix+team.getId(),team);
                SystemManager.getInstance().getTeamConcurrentHashMap().put(team.getId(),team);
                return team;
            } catch (InterruptedException e) {
                logStackTrace(e);
            }
        }
        return null;
    }
    //2+3
    private Team match6() {
        BlockingQueue<Room> twoRoomQueue = SystemManager.getInstance().getTwoRoomQueue();
        BlockingQueue<Room> threeRoomQueue = SystemManager.getInstance().getThreeRoomQueue();
        HashSet<Integer> playerList = new HashSet<Integer>();
        if(twoRoomQueue.size()>=1&&threeRoomQueue.size()>=1){
            try {
                Room room1 = twoRoomQueue.take();
                Room room2 = threeRoomQueue.take();
                //初始化队伍
                Team team = new Team();
                team.setPlayerList(playerList);
                team.setAssistNum(0);
                team.setDeathNum(0);
                team.setKillNum(0);
                team.setId(SystemManager.getInstance().getIdGenertor().generateTeamId());
                //初始化玩家
                HashSet<Integer> playerList1 = initPlayer(room1,team.getId());
                HashSet<Integer> playerList2 = initPlayer(room2,team.getId());
                playerList.addAll(playerList1);
                playerList.addAll(playerList2);

//                jedisClusterUtil.setObject(Const.TeamPrefix+team.getId(),team);
                SystemManager.getInstance().getTeamConcurrentHashMap().put(team.getId(),team);
                return team;
            } catch (InterruptedException e) {
                logStackTrace(e);
            }
        }
        return null;
    }

     //1+4,
    private Team match5() {
        BlockingQueue<Room> oneRoomQueue = SystemManager.getInstance().getOneRoomQueue();
        BlockingQueue<Room> fourRoomQueue = SystemManager.getInstance().getFourRoomQueue();
        HashSet<Integer> playerList = new HashSet<Integer>();
        if(oneRoomQueue.size()>=1&&fourRoomQueue.size()>=1){
            try {
                Room room1 = oneRoomQueue.take();
                Room room2 = fourRoomQueue.take();
                //初始化队伍
                Team team = new Team();
                team.setPlayerList(playerList);
                team.setAssistNum(0);
                team.setDeathNum(0);
                team.setKillNum(0);
                team.setId(SystemManager.getInstance().getIdGenertor().generateTeamId());
                //初始化玩家
                HashSet<Integer> playerList1 = initPlayer(room1,team.getId());
                HashSet<Integer> playerList2 = initPlayer(room2,team.getId());
                playerList.addAll(playerList1);
                playerList.addAll(playerList2);

//                jedisClusterUtil.setObject(Const.TeamPrefix+team.getId(),team);
                SystemManager.getInstance().getTeamConcurrentHashMap().put(team.getId(),team);
                return team;
            } catch (InterruptedException e) {
                logStackTrace(e);
            }
        }
        return null;
    }

    //1+2+2,
    private Team match4() {
        BlockingQueue<Room> oneRoomQueue = SystemManager.getInstance().getOneRoomQueue();
        BlockingQueue<Room> twoRoomQueue = SystemManager.getInstance().getTwoRoomQueue();
        HashSet<Integer> playerList = new HashSet<Integer>();
        if(oneRoomQueue.size()>=1&&twoRoomQueue.size()>=2){
            try {
                Room room1 = oneRoomQueue.take();
                Room room2 = twoRoomQueue.take();
                Room room3 = twoRoomQueue.take();
                //初始化队伍
                Team team = new Team();
                team.setPlayerList(playerList);
                team.setAssistNum(0);
                team.setDeathNum(0);
                team.setKillNum(0);
                team.setId(SystemManager.getInstance().getIdGenertor().generateTeamId());
                //初始化玩家
                HashSet<Integer> playerList1 = initPlayer(room1,team.getId());
                HashSet<Integer> playerList2 = initPlayer(room2,team.getId());
                HashSet<Integer> playerList3 = initPlayer(room3,team.getId());
                playerList.addAll(playerList1);
                playerList.addAll(playerList2);
                playerList.addAll(playerList3);

//                jedisClusterUtil.setObject(Const.TeamPrefix+team.getId(),team);
                SystemManager.getInstance().getTeamConcurrentHashMap().put(team.getId(),team);
                return team;
            } catch (InterruptedException e) {
                logStackTrace(e);
            }
        }
        return null;
    }

    //1+1+3,
    private Team match3() {

        BlockingQueue<Room> oneRoomQueue = SystemManager.getInstance().getOneRoomQueue();
        BlockingQueue<Room> threeRoomQueue = SystemManager.getInstance().getThreeRoomQueue();
        HashSet<Integer> playerList = new HashSet<Integer>();
        if(oneRoomQueue.size()>=3&&threeRoomQueue.size()>=1){
            try {
                Room room1 = oneRoomQueue.take();
                Room room2 = oneRoomQueue.take();
                Room room3 = threeRoomQueue.take();
                //初始化队伍
                Team team = new Team();
                team.setPlayerList(playerList);
                team.setAssistNum(0);
                team.setDeathNum(0);
                team.setKillNum(0);
                team.setId(SystemManager.getInstance().getIdGenertor().generateTeamId());
                //初始化玩家
                HashSet<Integer> playerList1 = initPlayer(room1,team.getId());
                HashSet<Integer> playerList2 = initPlayer(room2,team.getId());
                HashSet<Integer> playerList3 = initPlayer(room3,team.getId());
                playerList.addAll(playerList1);
                playerList.addAll(playerList2);
                playerList.addAll(playerList3);

//                jedisClusterUtil.setObject(Const.TeamPrefix+team.getId(),team);
                SystemManager.getInstance().getTeamConcurrentHashMap().put(team.getId(),team);
                return team;
            } catch (InterruptedException e) {
                logStackTrace(e);
            }
        }
        return null;
    }



    //1+1+1+2,
    private Team match2() {
        BlockingQueue<Room> oneRoomQueue = SystemManager.getInstance().getOneRoomQueue();
        BlockingQueue<Room> twoRoomQueue = SystemManager.getInstance().getTwoRoomQueue();
        HashSet<Integer> playerList = new HashSet<Integer>();
        if(oneRoomQueue.size()>=3&&twoRoomQueue.size()>=1){
            try {
                Room room1 = oneRoomQueue.take();
                Room room2 = oneRoomQueue.take();
                Room room3 = oneRoomQueue.take();
                Room room4 = twoRoomQueue.take();
                //初始化队伍
                Team team = new Team();
                team.setPlayerList(playerList);
                team.setAssistNum(0);
                team.setDeathNum(0);
                team.setKillNum(0);
                team.setId(SystemManager.getInstance().getIdGenertor().generateTeamId());
                //初始化玩家
                HashSet<Integer> playerList1 = initPlayer(room1,team.getId());
                HashSet<Integer> playerList2 = initPlayer(room2,team.getId());
                HashSet<Integer> playerList3 = initPlayer(room3,team.getId());
                HashSet<Integer> playerList4 = initPlayer(room4,team.getId());
                playerList.addAll(playerList1);
                playerList.addAll(playerList2);
                playerList.addAll(playerList3);
                playerList.addAll(playerList4);

//                jedisClusterUtil.setObject(Const.TeamPrefix+team.getId(),team);
                SystemManager.getInstance().getTeamConcurrentHashMap().put(team.getId(),team);
                return team;
            } catch (InterruptedException e) {
                logStackTrace(e);
            }
        }
        return null;

    }
    //初始化房间里面的所有玩家
    private HashSet<Integer> initPlayer(Room room1,Integer teamId) {
        HashSet<Integer> playerList = new HashSet<Integer>();
        List<User> userList = room1.getUserList();
        for (User user : userList) {
            Player player = new Player();
            player.setId(SystemManager.getInstance().getIdGenertor().generatePlayerId());
            player.setUserId(user.getId());
            player.setNickname(user.getNickname());

            player.setTeamId(teamId);

            playerList.add(player.getId());
            //保存到redis中
//            jedisClusterUtil.setObject(Const.PlayerPrefix+player.getId(),player);
            SystemManager.getInstance().getPlayerConcurrentHashMap().put(player.getId(),player);
        }
        return playerList;
    }
    protected void logStackTrace( Exception e ) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        // e.printStackTrace();
        logger.error(writer.toString());
    }
}
