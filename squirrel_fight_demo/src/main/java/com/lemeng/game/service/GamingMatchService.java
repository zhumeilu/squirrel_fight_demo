package com.lemeng.game.service;

import com.lemeng.common.SystemManager;
import com.lemeng.game.domain.Game;
import com.lemeng.game.domain.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**对于游戏中的匹配服务，将多个room组合成一个Team，存入到TeamQuenue
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 11:45
 */
public class GamingMatchService implements Runnable{

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int MAX_THREAD_NUM = 50;

    private ExecutorService executorService =
            Executors.newFixedThreadPool(MAX_THREAD_NUM);

    public void run() {

        while(true){
            //获取可加入的游戏队列
            BlockingQueue<Game> gameQuenue = SystemManager.getInstance().getCanJoinGameQuenue();
            BlockingQueue<Team> teamQuenue = SystemManager.getInstance().getTeamQuenue();
            try {
                Game gameTake = gameQuenue.take();
                //游戏开始时间小于7分钟
                if(System.currentTimeMillis()-gameTake.getBeginDate().getTime()<=7*60*1000){
                    int size = gameTake.getTeamList().size();
                    for (int i =0;i<6-size;i++){
                        Team teamTake = teamQuenue.take();
                        //取出来加入到gameTake中

                        //向游戏中的玩家广播新玩家加入

                        //向匹配中的玩家广播找到游戏
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



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
