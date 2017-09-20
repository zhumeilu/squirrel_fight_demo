package com.lemeng.user.manager;

import com.lemeng.user.domain.User;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:55
 */
public interface IUserManager {
    User login(String username,String password);
}
