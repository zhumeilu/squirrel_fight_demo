package com.lemeng.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 11:42
 */
public class SystemManager {

    private static SystemManager instance = new SystemManager();
    @Getter
    @Setter
    private ApplicationContext context ;
    @Getter
    private HashMap<Short,String> orderHandlerMap;           //存储命令和服务对应map
    private SystemManager(){

        orderHandlerMap = new HashMap<Short, String>();
        orderHandlerMap.put((short)0,"HeartBreak");  //心跳
        orderHandlerMap.put((short)1,"UserLoginService");  //登录
        orderHandlerMap.put((short)2,"Quit");  //退出

        orderHandlerMap.put((short)100,"UpdateRole");  //更新角色信息
        orderHandlerMap.put((short)101,"GetRoleList");  //获取所有角色信息

        orderHandlerMap.put((short)201,"Move");  //移动
        orderHandlerMap.put((short)202,"Turn");  //转向
        orderHandlerMap.put((short)203,"Fire");  //开火
        orderHandlerMap.put((short)204,"Hit");  //击中

    }

    public static SystemManager getInstance(){
        return instance;
    }



}
