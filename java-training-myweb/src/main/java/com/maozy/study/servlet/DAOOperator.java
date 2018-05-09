package com.maozy.study.servlet;

/**
 * Created by maozy on 2018/4/24.
 */
public class DAOOperator {

    private String name;
    public DAOOperator(String _name)
    {
        name = _name;
    }
    public void operate() throws InterruptedException
    {
        System.out.println("I will do much work...");
        Thread.sleep(3000);
        System.out.println(name + "DAOOperator exec succeed.");
    }

}
