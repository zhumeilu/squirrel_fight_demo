package com.lemeng.game.service;

import com.lemeng.server.message.SquirrelFightUdpMessage;
import com.lemeng.server.service.AbstractService;

/**比赛中途退出游戏
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 19:22
 */
public class QuitGameService extends AbstractService{

    public void run() {

        try{
            SquirrelFightUdpMessage udpMessage = (SquirrelFightUdpMessage) this.message;
            byte[] bodyBytes = udpMessage.getBody();



        }catch (Exception e){
            logStackTrace(e);
        }


    }
}
