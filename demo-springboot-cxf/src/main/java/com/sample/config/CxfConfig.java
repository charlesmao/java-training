package com.sample.config;

import com.sample.webservice.CommonService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * Created by maozy on 2018/5/22.
 */
@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Autowired
    CommonService commonService;

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, commonService);
        //这里相当于把Commonservice接口发布在了路径/services/CommonService下,wsdl文档路径为
        //http://localhost:8080/services/CommonService?wsdl
        endpoint.publish("/CommonService");
        return endpoint;
    }


    /*
    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/test/*");
    }

    @Bean
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public CommonService commonService() {
        return new CommonServiceImpl();
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), commonService());
        //这里相当于把Commonservice接口发布在了路径/test/common,wsdl文档路径为
        //http://localhost:8080/test/common?wsdl
        endpoint.publish("/common");
        return endpoint;
    }
    */


}
