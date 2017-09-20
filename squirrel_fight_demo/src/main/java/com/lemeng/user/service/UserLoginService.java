package com.lemeng.user.service;

import com.lemeng.server.service.AbstractService;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:登录服务
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 10:36
 */
@Component("UserLoginService")
public class UserLoginService extends AbstractService{

    @Autowired
    private IUserManager userManager;

    public void run() {

        byte[] bodyBytes = message.getBody();
        String username="zml";
        String password="123";
        User login = userManager.login(username, password);
        if(login!=null){
            //返回登录信息

            System.out.println("---------登录成功--------");

        }else{
            //返回登录失败
            System.out.println("---------登录失败--------");

        }


    }
}
