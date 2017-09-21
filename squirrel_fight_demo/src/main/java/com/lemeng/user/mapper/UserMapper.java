package com.lemeng.user.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lemeng.user.domain.FootPrint;
import com.lemeng.user.domain.Pet;
import com.lemeng.user.domain.Skill;
import com.lemeng.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:42
 */
@Repository
public interface UserMapper extends BaseMapper<User> {


    List<Pet> getPetByUserId(Integer userId);
    List<Skill> getSkillByUserId(Integer userId);
    List<FootPrint> getFootPrintByUserId(Integer userId);

}
