package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * //坚果
 * Description:
 * User: zhumeilu
 * Date: 2017/9/26
 * Time: 18:26
 */
@Getter
@Setter
public class Nut extends BaseDomain{
    private String name;        //坚果类型，大坚果，小坚果
    private Float positionX;   //坐标
    private Float positionY;   //坐标
    private Float positionZ;   //坐标
    private Float rotX;   //坐标
    private Float rotY;   //坐标
    private Float rotZ;   //坐标
    private Integer gameId; //所属游戏id
    private Integer statue;     //状态
}
