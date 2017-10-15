package com.lemeng.game.manager;

import com.lemeng.game.domain.Box;

import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:36
 */
public interface IBoxManager {

    List<Box> getBoxListByMap(String map);
}
