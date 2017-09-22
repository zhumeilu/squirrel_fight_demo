package com.lemeng.user.manager.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lemeng.user.domain.Pet;
import com.lemeng.user.domain.Skill;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IPetManager;
import com.lemeng.user.mapper.PetMapper;
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
public class PetManagerImpl extends ServiceImpl<PetMapper, Pet>
        implements IPetManager{

    @Autowired
    private PetMapper petMapper;
    @Autowired
    private UserMapper userMapper;
    public boolean buyPet(Integer userId, String type) {
        EntityWrapper<Pet> entityWrapper = new EntityWrapper<Pet>();
        entityWrapper.eq("type",type);
        Pet pet = this.selectOne(entityWrapper);
        User user = userMapper.selectById(userId);
        if(pet!=null&&user!=null){
            if(user.getGoldCoin()>=pet.getPrice()) {
                user.setGoldCoin(user.getGoldCoin()-pet.getPrice());
                userMapper.updateById(user);
                petMapper.buyPet(userId,pet.getId());
                return true;
            }
        }
        return false;
    }



}
