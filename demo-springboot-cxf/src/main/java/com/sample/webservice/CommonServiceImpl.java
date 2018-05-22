package com.sample.webservice;

import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by maozy on 2018/5/22.
 */
@WebService(serviceName = "CommonService",  //与接口中指定的name一致
    targetNamespace = "http://webservice.sample.com/", //与接口中的命名空间一致,一般是接口的包名倒写
    endpointInterface = "com.sample.webservice.CommonService" // 接口地址
)
@Component
public class CommonServiceImpl implements CommonService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
