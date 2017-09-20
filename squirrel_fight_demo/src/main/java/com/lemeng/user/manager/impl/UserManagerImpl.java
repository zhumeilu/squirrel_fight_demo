package com.lemeng.user.manager.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IUserManager;
import com.lemeng.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:55
 */
@Service
@Transactional
public class UserManagerImpl extends ServiceImpl<UserMapper, User>
        implements IUserManager{


    public User login(String username, String password) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("username",username);
        entityWrapper.eq("password",password);
        User user = this.selectOne(entityWrapper);
        return user;
    }
}
