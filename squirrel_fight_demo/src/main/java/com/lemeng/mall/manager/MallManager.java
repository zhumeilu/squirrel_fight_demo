package com.lemeng.mall.manager;

import com.lemeng.user.domain.User;
import com.lemeng.user.mapper.SkillMapper;
import com.lemeng.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/22
 * Time: 16:57
 */
@Service
@Transactional
public class MallManager implements IMallManager{

    @Autowired
    private UserMapper userMapper;
    public boolean rechargeGemstone(Integer userId, Integer count) {
        //查询用户
        User user = userMapper.selectById(userId);
        if(user!=null){
            user.setGemstone(user.getGemstone()+count);
            userMapper.updateById(user);
            return true;
        }
        return false;
    }


}
