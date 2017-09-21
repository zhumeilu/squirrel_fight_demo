package com.lemeng.server.bootstrap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jiangwenping on 16/11/18.
 * 服务器关闭调用的方法
 */
public class ShutdownHook implements Runnable {

    ClassPathXmlApplicationContext classPathXmlApplicationContext;

    public ShutdownHook(ClassPathXmlApplicationContext classPathXmlApplicationContext) {
        this.classPathXmlApplicationContext = classPathXmlApplicationContext;
    }

    public void run() {  //重写Runnable中的run方法并在此销毁bean
        this.classPathXmlApplicationContext.destroy();
    }
}
