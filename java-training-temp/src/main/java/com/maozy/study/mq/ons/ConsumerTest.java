package com.maozy.study.mq.ons;

import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

/**
 * Created by maozy on 2018/4/16.
 */
public class ConsumerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, "CID_GBSS_PROM_API_DEV_MAOZY");
        //properties.put(PropertyKeyConst.AccessKey, "请输入AccessKey");
        properties.put(PropertyKeyConst.AccessKey, "06b037002fa24f6995b5047bad22a21d");
        //properties.put(PropertyKeyConst.SecretKey, "请输入SecretKey");
        properties.put(PropertyKeyConst.SecretKey, "5PQeuKjOGX0zilKTKE0A4qhGYvM=");
        //properties.put(PropertyKeyConst.ONSAddr, "http://onsaddr-internal.aliyun.com:8080/rocketmq/nsaddr4client-internal");//此处以公有云生产环境为例
        properties.put(PropertyKeyConst.ONSAddr, "http://mq.server-test.infinitus.com.cn/rocketmq/nsaddr4broker-internal");//此处以公有云生产环境为例
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("GBSS_PROM_API_DEV_MAOZY", "TagB", new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                System.out.println("Receive Message Body:" + new String(message.getBody()));
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer Started");
    }

}
