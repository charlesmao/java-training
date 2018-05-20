package com.maozy.dubbo.demo.provider;

import com.alibaba.dubbo.rpc.RpcContext;
import com.maozy.dubbo.demo.DemoSerivce;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by maozy on 2018/5/20.
 */
public class DemoServiceImpl implements DemoSerivce{
    @Override
    public String sayHello(String name) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name + ", response from provider: " + RpcContext.getContext().getLocalAddress();
    }
}
