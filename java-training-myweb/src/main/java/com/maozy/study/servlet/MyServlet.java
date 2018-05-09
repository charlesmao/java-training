package com.maozy.study.servlet;

/**
 * Created by maozy on 2018/4/24.
 */
public class MyServlet implements MyServletIn{
    public void service() {

        System.out.println("Hellow I'm the servlet.");
        System.out.println("Now let's start our web http travel.");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DAOOperator dao = new DAOOperator("Mysql");
        try {
            dao.operate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Now I will beleave the web world...");
        System.out.println("Bye-bye.");

    }
}
