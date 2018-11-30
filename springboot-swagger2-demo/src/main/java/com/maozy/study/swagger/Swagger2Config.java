package com.maozy.study.swagger;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                //不生效，有时间再研究
                //.host("localhost:8080/api")
                //.protocols(Sets.newHashSet("https", "http"))
                .select().apis(RequestHandlerSelectors.basePackage("com.maozy.study.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Spring Boot中使用Swagger2构建RESTful APIs")
                .description("springboot 整合swaager2 构建api文档")
                .contact(new Contact("charles", "https://github.com/charlesmao", "maozy@126.com"))
                .version("v1.0").build();
    }

}
