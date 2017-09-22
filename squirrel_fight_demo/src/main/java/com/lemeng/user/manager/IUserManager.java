package com.lemeng.user.manager;

import com.lemeng.user.domain.FootPrint;
import com.lemeng.user.domain.Pet;
import com.lemeng.user.domain.Skill;
import com.lemeng.user.domain.User;

import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:55
 */
public interface IUserManager {
    User login(String mobile,String password);
    User regist(String mobile,String password,String verifyCode);
    boolean initUser(Integer userId,String nickname);
    List<Pet> getPetListByUserId(Integer userId);
    List<Skill> getSkillListByUserId(Integer userId);
    List<FootPrint> getFootPrintListByUserId(Integer userId);
}
