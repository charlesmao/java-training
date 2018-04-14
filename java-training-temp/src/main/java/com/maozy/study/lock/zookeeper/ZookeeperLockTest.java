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
    private static final String CONNECT_STRING = "172.20.71.128:2181,172.20.71.185:2182,172.20.71.186:2183";

    //10秒内10万并发量
    private static final Integer MILLIS = 1000; //10s
    private static final Integer REQUESTS = 100; //10万

    //假设业务方法执行时间
    private static final Long EXECUTE_TIME = 100L; //毫秒

    //全局共享资源
    private static Integer STOCK = 90; //假设为1000的库存



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

        System.out.println(MILLIS/1000 + "秒内并发数并发数" + REQUESTS + "共花费时间：" + (end -start) + "毫秒");


        System.out.println("执行后共享资源为：" + STOCK);
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
            Long millis = (long)random.nextInt(MILLIS);
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            CuratorFramework client = null;
            try {
                //创建zookeeper客户端(应加非空判断)
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
                    client.close();
                    System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：关闭客户端连接...");
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
            System.out.println(System.currentTimeMillis() + "-" + Thread.currentThread().getName() + "：扣减库存...");
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
