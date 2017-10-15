package com.lemeng.game.manager.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lemeng.game.domain.Nut;
import com.lemeng.game.manager.INutManager;
import com.lemeng.game.mapper.NutMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:37
 */
@Service
public class NutManagerImpl implements INutManager {
    @Autowired
    private NutMapper nutMapper;


    public List<Nut> getNutListByMap(String map) {

        EntityWrapper<Nut> entityWrapper = new EntityWrapper<Nut>();
        entityWrapper.eq("map",map);
        List<Nut> nutList = nutMapper.selectList(entityWrapper);
        return nutList;
    }
}
