package com.lemeng.game.manager.impl;

import com.lemeng.game.domain.Team;
import com.lemeng.game.manager.ITeamManager;
import com.lemeng.game.mapper.TeamMapper;
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

        return null;
    }
}
