package com.lemeng.user.manager.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lemeng.user.domain.Skill;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.ISkillManager;
import com.lemeng.user.mapper.SkillMapper;
import com.lemeng.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SkillManagerImpl extends ServiceImpl<SkillMapper, Skill>
        implements ISkillManager {

    @Autowired
    private SkillMapper skillMapper;
    @Autowired
    private UserMapper userMapper;
    public boolean buySkill(Integer userId, String type) {
        //查询技能
        EntityWrapper<Skill> skillEntityWrapper = new EntityWrapper<Skill>();
        skillEntityWrapper.eq("type",type);
        Skill skill = this.selectOne(skillEntityWrapper);
        //查询用户
        User user = userMapper.selectById(userId);
        if(skill!=null&&user!=null){
            //判断用户金币是否足够
            if(user.getGoldCoin()>=skill.getPrice()){
                user.setGoldCoin(user.getGoldCoin()-skill.getPrice());
                userMapper.updateById(user);
                skillMapper.buySkill(userId,skill.getId());
                return true;
            }
        }
        return false;
    }
}
