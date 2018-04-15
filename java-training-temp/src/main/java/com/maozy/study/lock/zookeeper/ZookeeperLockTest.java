package com.maozy.study.lock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Random;
import java.util.concurrent.CountDownLatch;


/**
 * Created by maozy on 2018/4/12.
 */
public class ZookeeperLockTest {

    //创建重试策略，第一个参数(baseSleepTimeMs)为每次获取客户端链接的间隔时间，第二个参数(maxRetries)为重试次数
    private static final RetryPolicy RETRY_POLICY = new ExponentialBackoffRetry(1000, 3);
    private static final String CONNECT_STRING = "192.168.20.120:2181,192.168.20.121:2182,192.168.20.123:2183";

    //MILLIS毫秒内REQUESTS并发量
    private static final Integer MILLIS = 1000; //测试一定时间内的并发（毫秒）
    private static final Integer REQUESTS = 5; //并发数

    //假设业务方法执行时间
    private static final Long EXECUTE_TIME = 10000L; //毫秒

    //全局共享资源
    private static Integer STOCK = 80; //库存



    public static void main(String[] args) throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(REQUESTS);

        Long start = System.currentTimeMillis();
        //模拟1000个线程并发
        for (int i = 0; i < REQUESTS; i++) {
            new BizThread(i, countDownLatch).start();
        }

        //等待子线程结束
        countDownLatch.await();

        Long end = System.currentTimeMillis();

        System.out.println(MILLIS/1000 + "秒内并发数：" + REQUESTS + "，共花费时间：" + (end -start) + "毫秒");


        System.out.println("执行后库存为：" + STOCK);
    }

    private static class BizThread extends Thread {

        private CountDownLatch countDownLatch;

        public BizThread(Integer number, CountDownLatch countDownLatch) {
            super("线程-" + number);
            this.countDownLatch = countDownLatch;
        }


        @Override
        public void run() {

            //模拟在10秒内1000个请求陆续发出
            Random random = new Random();
            Long millis = (long)random.nextInt(MILLIS); //MILLIS毫秒内任意时间的请求
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            CuratorFramework client = null;
            try {
                //创建zookeeper客户端(应加非空判断)
                System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + ":创建客户端...");
                client = CuratorFrameworkFactory.newClient(CONNECT_STRING, RETRY_POLICY);
                client.start();
                //创建分布式锁，锁空间的根节点为/curator/lock
                InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
                //尝试获取锁，没有返回值，此位置获取锁失败时，会抛出一个异常
                System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：尝试获取锁...");
                mutex.acquire();
                //尝试在设定的时间内获取锁，有返回值boolean
                //mutex.acquire(3, TimeUnit.SECONDS);
                System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：得到锁，开始执行业务代码...");
                //业务代码
                doSomething();
                System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：业务代码执行结束,释放锁...");
                //释放锁
                mutex.release();
            } catch (Exception e) {
                System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：发生异常...");
                e.printStackTrace();
            } finally {
                if (client != null) {
                    //关闭客户端
                    System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：关闭客户端连接...");
                    client.close();
                }

                //递减锁存器计算
                countDownLatch.countDown();

            }
        }
    }

    private static void doSomething() {

        System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：执行业务代码...");

        if (STOCK > 0) {
            //额外操作
            doeXtraSomething();
            //扣减库存
            STOCK--;
            System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：扣减库存成功...");
        } else {
            System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：扣减库存失败...");
        }

        System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：当前库存为" + STOCK);

        try {
            Thread.sleep(EXECUTE_TIME); //单位毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void doeXtraSomething() {

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
