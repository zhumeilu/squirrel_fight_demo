package com.lemeng.game.manager;

import com.lemeng.common.SystemManager;
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
public class TeamManagerImpl implements ITeamManager {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeamMapper teamMapper;
    public Team createTeam(Integer userId) {
        User user = userMapper.selectById(userId);
        if(user!= null){
            //Team的生成策略以后再实现
            Team team = new Team();
            team.setId(userId);
            team.setHeader(user);
            team.getUserList().add(user);
            SystemManager.getInstance().getTeamMap().put(team.getId(),team);
            return team;
        }
        return null;
    }
}
