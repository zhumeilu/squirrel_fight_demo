package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import com.lemeng.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:房间
 * User: zhumeilu
 * Date: 2017/9/21
 * Time: 11:29
 */
@Setter
@Getter
public class Room extends BaseDomain{

    private User header;    //队长
    private List<User> userList = new ArrayList<User>();    //队员
    private Integer score;      //分数
}
