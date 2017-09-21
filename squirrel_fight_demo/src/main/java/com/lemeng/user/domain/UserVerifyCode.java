package com.lemeng.user.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Description:用户验证码
 * User: zhumeilu
 * Date: 2017/9/21
 * Time: 14:13
 */
@Getter
@Setter
public class UserVerifyCode extends BaseDomain{

    private String mobile;
    private String verifyCode;
    private Date createTime;

}
