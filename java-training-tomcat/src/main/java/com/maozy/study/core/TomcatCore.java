package com.maozy.study.core;

import com.maozy.study.servlet.MyServletIn;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by maozy on 2018/4/24.
 */
public class TomcatCore {

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        System.out.println("Hellow,I'm MyTomcat ,and I'm in the initialization... ");
        Thread.sleep(3000);
        System.out.println("Now,I have finished the initialization .");
        Thread.sleep(1000);
        System.out.println("Now I will start  the Servlet.");

        //读取配置文件获取Servlet的项目路径名及类包路径名
        FileReader reader = new FileReader("F:\\maozy\\Study\\java-training\\java-training-tomcat\\target\\classes\\TomcatConf.txt");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String path = bufferedReader.readLine();
        String className = bufferedReader.readLine();

        bufferedReader.close();
        reader.close();

        //根据已获取Servlet的项目路径名及类包路径名通过URL类加载器加载文件系统中的某个.class
        File file = new File(path);
        URL url = file.toURI().toURL(); //这里取文件系统的URL地址
        //创建持有我们所部署的"web项目"路径的URL类加载器，以使Tomcat之外的"web"纳入Tomcat的classpath之中。
        URLClassLoader loader = new URLClassLoader(new URL[] {url});
        //利用反射加载类
        Class<?> tidyClazz = loader.loadClass(className);
        //转化为Servlet接口执行service操作。
        MyServletIn servlet = (MyServletIn)tidyClazz.newInstance();
        //当然，实际的tomcat并不在这里调用service，而仅仅是进入事件循环，在有浏览器请求时才调用service。
        servlet.service();

    }

}
