package com.lemeng.user.manager.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lemeng.user.domain.FootPrint;
import com.lemeng.user.domain.Pet;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IFootPrintManager;
import com.lemeng.user.mapper.FootPrintMapper;
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
public class FootPrintManagerImpl extends ServiceImpl<FootPrintMapper, FootPrint>
        implements IFootPrintManager {

    @Autowired
    private FootPrintMapper footPrintMapper;
    @Autowired
    private UserMapper userMapper;
    public boolean buyFootPrint(Integer userId, String type) {
        EntityWrapper<FootPrint> entityWrapper = new EntityWrapper<FootPrint>();
        entityWrapper.eq("type",type);
        FootPrint footPrint = this.selectOne(entityWrapper);
        User user = userMapper.selectById(userId);
        if(footPrint!=null&&user!=null){
            if(user.getGoldCoin()>=footPrint.getPrice()) {
                user.setGoldCoin(user.getGoldCoin()-footPrint.getPrice());
                userMapper.updateById(user);
                footPrintMapper.buyFootPrint(userId,footPrint.getId());
                return true;
            }
        }
        return false;
    }
}
