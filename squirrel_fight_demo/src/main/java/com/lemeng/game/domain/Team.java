package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import com.lemeng.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    private List<Player> playerList = new ArrayList<Player>();    //队员
    //杀人数
    private Integer killNum;
    //死亡数
    private Integer deathNum;
    //助攻数
    private Integer assistNum;
    //mvp
    private Player mvp;

    private Integer gameId;

}
