package com.maozy.study.spi;


import java.util.ServiceLoader;

/**
 * Created by maozy on 2018/3/26.
 */
public class SpiServiceTest {

    public static void main(String[] args) {

        ServiceLoader<DogService> loaders = ServiceLoader.load(DogService.class);
        for (DogService dogService : loaders) {
            dogService.sleep();
        }

    }

}
