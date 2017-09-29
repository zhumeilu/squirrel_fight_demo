package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/19
 * Time: 15:10
 */
@Getter
@Setter
public class Game extends BaseDomain{

    //玩家
    private HashSet<Integer> playerList;
    //队伍
    private List<Team> teamList;
    //地图
    private String map;
    //坚果
    private List<Nut> nutList;
    //箱子
    private List<Box> boxList;
    //天气
    private String weather;

    private Date beginDate;     //游戏开始时间

    private Date endDate;     //游戏结束时间
}
