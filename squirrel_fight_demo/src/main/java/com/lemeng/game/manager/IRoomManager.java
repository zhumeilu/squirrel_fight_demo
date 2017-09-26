package com.lemeng.game.manager;

import com.lemeng.game.domain.Room;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:36
 */
public interface IRoomManager {
    Room createRoom(Integer userId);
}
