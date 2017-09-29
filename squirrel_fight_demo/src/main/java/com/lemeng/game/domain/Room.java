package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import com.lemeng.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:房间
 * User: zhumeilu
 * Date: 2017/9/21
 * Time: 11:29
 */
@Setter
@Getter
@ToString
public class Room extends BaseDomain{

    private User header;    //队长
    private List<User> userList = new ArrayList<User>();    //队员
    private Integer score;      //分数


    //重写equals，在从RoomQueue中删除Room的时候需要用到
    //因为是从redis中取出来的对象，所以和RoomQueue中存储的不一样，所以需要重写equals方法
    @Override
    public boolean equals(Object obj) {
        Room other = (Room)obj;
        return this.getId().equals(other.getId());
    }
    public Room(Integer id,Integer score){

        this.setId(id);
        this.score = score;
    }
    public Room(){

    }
}
