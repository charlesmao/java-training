package com.sample.designPattern.strategy;

/**
 * Created by maozy on 2018/5/17.
 */
public class Context {

    private OperationStrategy operationStrategy;

    public Context(OperationStrategy operationStrategy) {
        this.operationStrategy = operationStrategy;
    }

    public int executeStrategy(int num1, int num2) {
        return this.operationStrategy.doOperation(num1, num2);
    }

}
