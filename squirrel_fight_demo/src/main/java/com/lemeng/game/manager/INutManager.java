package com.lemeng.game.manager;

import com.lemeng.game.domain.Box;
import com.lemeng.game.domain.Nut;

import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 13:36
 */
public interface INutManager {

    List<Nut> getNutListByMap(String map);
}
