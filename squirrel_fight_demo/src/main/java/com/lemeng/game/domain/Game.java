package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/19
 * Time: 15:10
 */
@Getter
@Setter
public class Game extends BaseDomain{

    private List<Player> playerList;


}
