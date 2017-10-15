package com.lemeng.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 13:23
 */
public class IdGenertor {

    private AtomicInteger teamId = new AtomicInteger(1);
    private AtomicInteger playerId = new AtomicInteger(1);
    private AtomicInteger gameId = new AtomicInteger(1);
    private AtomicInteger assistId = new AtomicInteger(1);
    private AtomicInteger boxId = new AtomicInteger(1);
    private AtomicInteger nutId = new AtomicInteger(1);

    public Integer generateTeamId(){
        return teamId.getAndIncrement();
    }
    public Integer generatePlayerId(){
        return playerId.getAndIncrement();
    }
    public Integer generateGameId(){
        return gameId.getAndIncrement();
    }
    public Integer generateAssistId(){
        return assistId.getAndIncrement();
    }
    public Integer generateBoxId(){
        return boxId.getAndIncrement();
    }
    public Integer generateNutId(){
        return nutId.getAndIncrement();
    }


}
