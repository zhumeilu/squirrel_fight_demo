package com.lemeng.game.manager.impl;

import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Room;
import com.lemeng.game.manager.IRoomManager;
import com.lemeng.user.domain.User;
import com.lemeng.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:37
 */
@Service
public class RoomManagerImpl implements IRoomManager {
    @Autowired
    private UserMapper userMapper;
    public Room createRoom(Integer userId) {
        User user = userMapper.selectById(userId);
        if(user!= null){
            //Team的生成策略以后再实现
            Room room = new Room();
            room.setHeader(user);
            room.setId(user.getId());
            room.getUserList().add(user);
            //存储到内存中
            SystemManager.getInstance().getRoomConcurrentHashMap().put(room.getId(),room);
            return room;
        }
        return null;
    }
}
