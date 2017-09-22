package com.lemeng.user.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lemeng.user.domain.Skill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:42
 */
@Repository
public interface SkillMapper extends BaseMapper<Skill> {

    void buySkill(@Param("userId") Integer userId,@Param("skillId") Integer skillId);
}
