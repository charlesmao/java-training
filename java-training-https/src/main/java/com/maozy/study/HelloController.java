package com.maozy.study;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by maozy on 2018/3/17.
 */
@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello World2!";
    }

    @GetMapping("/templates")
    public String testHtml() {
        return "test01";
    }

}
