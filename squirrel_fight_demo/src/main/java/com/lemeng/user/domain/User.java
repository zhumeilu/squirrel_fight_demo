package com.lemeng.user.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Description:用户
 * User: zhumeilu
 * Date: 2017/9/19
 * Time: 10:10
 */
@Getter
@Setter
@ToString
public class User extends BaseDomain {

    private String mobile;    //手机号
    private String nickname;    //昵称
    private String password;    //密码

    private Integer level;      //等级
    private Integer statue ;        //用户状态，在线，游戏中，离线
    private Integer gemstone; //宝石
    private Integer goldCoin;  //金币
    private Integer footPrintId; //脚印id


}
