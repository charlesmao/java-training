package com.sample.designPattern.strategy;

/**
 * Created by maozy on 2018/5/17.
 */
public class AddOperationStrategy implements OperationStrategy{
    @Override
    public int doOperation(int num1, int num2) {
        return num1 + num2;
    }
}
