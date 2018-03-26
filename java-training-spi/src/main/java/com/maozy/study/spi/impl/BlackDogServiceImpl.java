package com.maozy.study.spi.impl;

import com.maozy.study.spi.DogService;

/**
 * Created by maozy on 2018/3/26.
 */
public class BlackDogServiceImpl implements DogService {
    public void sleep() {
        System.out.println("黑色dog。。。汪汪叫，不睡觉...");
    }
}
