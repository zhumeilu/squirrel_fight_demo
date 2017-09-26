package com.lemeng.game.manager;

import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Room;
import com.lemeng.game.domain.Team;
import com.lemeng.game.mapper.TeamMapper;
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
    @Autowired
    JedisClusterUtil jedisClusterUtil;
    public Room createRoom(Integer userId) {
        User user = userMapper.selectById(userId);
        if(user!= null){
            //Team的生成策略以后再实现
            Room room = new Room();
            room.setHeader(user);
            room.setId(user.getId());
            room.getUserList().add(user);
            jedisClusterUtil.setObject(room.getId().toString(),room);
            return room;
        }
        return null;
    }
}
