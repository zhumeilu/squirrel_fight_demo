package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Description:玩家
 * User: zhumeilu
 * Date: 2017/9/19
 * Time: 10:10
 */
@Setter
@Getter
@ToString
public class Player extends BaseDomain{

    private String nickname;    //昵称（唯一，特殊字符限制）
    private Integer hp;         //血量
    private Integer attack;     //攻击力
    private Integer exp;        //经验值
    private Integer level;      //等级
    private Integer speed;      //移动速度
    private Integer propSpeed;  //道具速度
    private Integer armor;  //护甲
    private Float  criticalStrikeChance; //暴击几率
    private Float  criticalStrikeDamage; //暴击伤害

    private Integer pickUpDistance;     //拾取距离

    private Integer antiDizzy;          //眩晕免疫

    private Integer recover;        //恢复能力（吸血）

    private Float positionX;   //坐标
    private Float positionY;   //坐标
    private Float positionZ;   //坐标
    private Float rotX;   //坐标
    private Float rotY;   //坐标
    private Float rotZ;   //坐标
    private Float gunRot;   //坐标
    private Float gunRoll;   //坐标

    private String actionName;      //当前动作名称

    private Integer userId;     //所属用户id
    private Integer gameId;     //所属游戏
    private Integer teamId;        //所属组队
    private Integer roomId;        //所属房间

    private Integer killNum;        //杀人数
    private Integer deathNum;       //死亡数
    private Integer assistNum;      //助攻数
    private Integer statue;         //状态  1正常 2死亡
    private Date lastDead;      //上一次死亡时间

}
