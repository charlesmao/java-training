package com.sample.designPattern.strategy;

/**
 * 策略模式Demo
 *
 * Created by maozy on 2018/5/17.
 */
public class StrategyPatternDemo {

    public static void main(String[] args) {
        Context context = new Context(new AddOperationStrategy());
        System.out.println("10 + 5 = " + context.executeStrategy(10, 5));

        context = new Context(new SubstractOperationStrategy());
        System.out.println("10 - 5 = " + context.executeStrategy(10, 5));

        context = new Context(new MultiplyOperationStrategy());
        System.out.println("10 * 5 = " + context.executeStrategy(10, 5));

    }


}
