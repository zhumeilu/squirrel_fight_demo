package com.lemeng.user.manager;

import com.lemeng.user.domain.UserVerifyCode;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:55
 */
public interface IUserVerifyCodeManager {
    UserVerifyCode save(UserVerifyCode userVerifyCode);
}
