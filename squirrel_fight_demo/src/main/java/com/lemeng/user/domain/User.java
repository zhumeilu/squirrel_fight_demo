package com.lemeng.user.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Description:用户
 * User: zhumeilu
 * Date: 2017/9/19
 * Time: 10:10
 */
@Getter
@Setter
public class User extends BaseDomain {

    private String username;    //用户名
    private String password;    //密码
    private Integer level;      //等级
    private Integer statue ;        //用户状态，在线，游戏中，离线
    private Integer gemstone; //宝石
    private Integer goldCoins;  //金币
    private Integer footPrintsId; //脚印id
    private List<Integer> skillList;        //用户拥有的技能
    private List<Integer> petList;  //用户拥有的宠物

}
