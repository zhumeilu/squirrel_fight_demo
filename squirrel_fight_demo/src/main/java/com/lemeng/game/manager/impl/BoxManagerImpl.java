package com.lemeng.game.manager.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lemeng.game.domain.Box;
import com.lemeng.game.manager.IBoxManager;
import com.lemeng.game.mapper.BoxMapper;
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
public class BoxManagerImpl implements IBoxManager {
    @Autowired
    private BoxMapper boxMapper;


    public List<Box> getBoxListByMap(String map) {

        EntityWrapper<Box> entityWrapper = new EntityWrapper<Box>();
        entityWrapper.eq("map",map);
        List<Box> boxList = boxMapper.selectList(entityWrapper);
        return boxList;
    }
}
