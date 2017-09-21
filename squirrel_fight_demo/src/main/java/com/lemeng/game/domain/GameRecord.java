package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Description:个人游戏记录
 * User: zhumeilu
 * Date: 2017/9/21
 * Time: 11:53
 */
@Getter
@Setter
public class GameRecord extends BaseDomain{


    private Integer usreId;     //所属用户
    private Integer gameId;     //所属游戏id

    private Date createTime;    //创建时间
    private Date gameStartTime; //游戏开始时间
    private Date gameOverTime;  //游戏结束时间
}
