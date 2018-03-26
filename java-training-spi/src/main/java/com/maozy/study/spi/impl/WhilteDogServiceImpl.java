package com.maozy.study.spi.impl;

import com.maozy.study.spi.DogService;

/**
 * Created by maozy on 2018/3/26.
 */
public class WhilteDogServiceImpl implements DogService {
    public void sleep() {
        System.out.println("白色dog。。。呼呼大睡觉...");
    }
}
