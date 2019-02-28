package com.dynamic.prox;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author qll
 * @create 2019-02-28 21:35
 * @desc
 **/
public class MyProxy<T> implements InvocationHandler {
    T target;
    public MyProxy(T target){
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("开始");
        Object invoke = method.invoke(target, args);
        System.out.println("结束");
        return invoke;
    }
}
