package com.maozy.study.designPattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by maozy on 2018/4/15.
 */
public class ProxyFactory {
    private Object target;
    public ProxyFactory(Object target) {
        this.target = target;
    }
    public Object getProxyInstance() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("开启事务...");
                        //System.out.println("获取分布式锁...");
                        Object returnValue = method.invoke(target, args);
                        System.out.println("提交事务...");
                        //System.out.println("释放分布式锁...");
                        return returnValue;
                    }
                });
    }
}
