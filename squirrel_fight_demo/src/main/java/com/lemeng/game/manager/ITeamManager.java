package com.lemeng.game.manager;

import com.lemeng.game.domain.Team;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:36
 */
public interface ITeamManager {
    Team createTeam(Integer userId);
}
