package com.dynamic.buniss;

import com.dynamic.prox.MyProxy;
import com.dynamic.service.UserService;
import com.dynamic.service.impl.UserServiceImpl2;

import java.lang.reflect.Proxy;

/**
 * @author qll
 * @create 2019-02-28 21:41
 * @desc
 **/
public class UserBussiness {
    public static void main(String[] args) {
        MyProxy myProxy = new MyProxy(new UserServiceImpl2());
        UserService userService = (UserService)Proxy.newProxyInstance(UserService.class.getClassLoader(), new Class[]{UserService.class}, myProxy);
        userService.test();
    }
}
