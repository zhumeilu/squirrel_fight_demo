package com.lemeng.user.manager.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lemeng.user.domain.*;
import com.lemeng.user.manager.IUserManager;
import com.lemeng.user.mapper.UserMapper;
import com.lemeng.user.mapper.UserVerifyCodeMapper;
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
    @Autowired
    private UserVerifyCodeMapper userVerifyCodeMapper;
    public User login(String mobile, String password) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("mobile",mobile);
        entityWrapper.eq("password",password);
        User user = this.selectOne(entityWrapper);
        return user;
    }

    public User regist(String mobile, String password, String verifyCode) {
        //查询验证码
        EntityWrapper<UserVerifyCode> entityWrapper = new EntityWrapper<UserVerifyCode>();
        entityWrapper.eq("mobile",mobile);
        entityWrapper.eq("verifyCode",verifyCode);
        List<UserVerifyCode> userVerifyCodes = userVerifyCodeMapper.selectList(entityWrapper);
        UserVerifyCode userVerifyCode = null;
        if(userVerifyCodes!=null&&userVerifyCodes.size()==1){
            userVerifyCode = userVerifyCodes.get(0);
            if(System.currentTimeMillis()-userVerifyCode.getCreateTime().getTime()<5*60*1000){
                //保存用户
                User user = new User();
                user.setMobile(mobile);
                user.setPassword(password);
                user.setGemstone(0);
                user.setGoldCoin(0);
                user.setLevel(1);
                user.setStatue(1);
                userMapper.insert(user);
                return user;
            }
        }
        //保存用户信息
        return null;
    }

    public boolean initUser(Integer userId, String nickname) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("nickname",nickname);
        List<User> users = this.selectList(entityWrapper);
        if(users!=null&&users.size()>0){
            return false;
        }
        User user = this.selectById(userId);
        if(user!=null){
            user.setNickname(nickname);
            return true;
        }
        return false;
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
