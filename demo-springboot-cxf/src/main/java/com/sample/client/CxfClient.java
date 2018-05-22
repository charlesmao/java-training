package com.sample.client;

import com.sample.webservice.CommonService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * Created by maozy on 2018/5/22.
 */
public class CxfClient {

    public static void main(String[] args) {
        //cl1();

        cl2();
    }


    /**
     *  方式1：代理类工厂的方式，需要拿到对方的接口
     */
    private static void cl1() {

        //接口地址
        String address = "http://localhost:8080/services/CommonService?wsdl";
        //代理工厂
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        //设置代理地址
        jaxWsProxyFactoryBean.setAddress(address);
        jaxWsProxyFactoryBean.setServiceClass(CommonService.class);
        //创建一个代理接口实现
        CommonService commonService = (CommonService) jaxWsProxyFactoryBean.create();
        //准备数据
        String userName = "maozy";
        String result = commonService.sayHello(userName);
        System.out.println("返回结果" + result);


    }

    /**
     * 方式2：动态调用方式
     */
    private static void cl2() {

        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
        Client client = factory.createClient("http://localhost:8080/services/CommonService?wsdl");

        //需要密码的情况需要加上用户名和密码
        //client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME, PASS_WORD));

        try {
            Object[] result = client.invoke("sayHello", "maozy2");
            if (result != null) {
                System.out.println("返回数据：" + result[0]);
            } else {
                System.out.println("result == null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
