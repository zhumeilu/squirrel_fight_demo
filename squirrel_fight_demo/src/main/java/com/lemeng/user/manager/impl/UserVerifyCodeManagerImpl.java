package com.lemeng.user.manager.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lemeng.user.domain.UserVerifyCode;
import com.lemeng.user.manager.IUserVerifyCodeManager;
import com.lemeng.user.mapper.UserVerifyCodeMapper;
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
public class UserVerifyCodeManagerImpl extends ServiceImpl<UserVerifyCodeMapper, UserVerifyCode>
        implements IUserVerifyCodeManager{


    public UserVerifyCode save(UserVerifyCode userVerifyCode) {
        return this.save(userVerifyCode);
    }
}
