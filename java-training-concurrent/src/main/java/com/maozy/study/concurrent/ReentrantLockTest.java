package com.maozy.study.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    public static void main(String[] args) {

        MyService myService = new MyService();

        MyThread myThread1 = new MyThread(myService);
        MyThread myThread2 = new MyThread(myService);
        MyThread myThread3 = new MyThread(myService);
        MyThread myThread4 = new MyThread(myService);
        MyThread myThread5 = new MyThread(myService);

        myThread1.start();
        myThread2.start();
        myThread3.start();
        myThread4.start();
        myThread5.start();

    }

    public static class MyService {
        private Lock lock = new ReentrantLock();

        public void testMethod() {
            lock.lock();
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("ThreadName=" + Thread.currentThread().getName() + " " + (i+1));
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static class MyThread extends Thread {

        private MyService myService;

        public MyThread(MyService myService) {
            super();
            this.myService = myService;
        }

        @Override
        public void run() {
            myService.testMethod();
        }
    }

}
