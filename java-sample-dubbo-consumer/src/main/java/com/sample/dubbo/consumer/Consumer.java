package com.sample.dubbo.consumer;

import com.sample.dubbo.api.PayService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by maozy on 2018/5/20.
 */
public class Consumer {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");

        PayService payService = (PayService)context.getBean("payService"); //Proxy

        System.out.println(payService.doPay("maomao"));
    }

}
