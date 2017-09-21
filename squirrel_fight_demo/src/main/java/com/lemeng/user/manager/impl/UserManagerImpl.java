package com.lemeng.user.manager.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lemeng.user.domain.FootPrint;
import com.lemeng.user.domain.Pet;
import com.lemeng.user.domain.Skill;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IUserManager;
import com.lemeng.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("username",username);
        entityWrapper.eq("password",password);
        User user = this.selectOne(entityWrapper);
        return user;
    }

    public List<Pet> getPetListByUserId(Integer userId) {
        return userMapper.getPetByUserId(userId);
    }

    public List<Skill> getSkillListByUserId(Integer userId) {
        return userMapper.getSkillByUserId(userId);
    }

    public List<FootPrint> getFootPrintListByUserId(Integer userId) {
        return userMapper.getFootPrintByUserId(userId);
    }
}
