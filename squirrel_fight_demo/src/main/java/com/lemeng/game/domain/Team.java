package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import com.lemeng.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Description:队伍
 * User: zhumeilu
 * Date: 2017/9/21
 * Time: 11:29
 */
@Setter
@Getter
public class Team extends BaseDomain{

    private User header;    //队长
    private List<User> userList;    //队员
}
