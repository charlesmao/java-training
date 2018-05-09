package com.maozy.study;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by maozy on 2018/3/17.
 */

@RestController
public class MyRestController {

    @GetMapping("/person/{id}")
    public Person findPerson(@PathVariable("id") Integer id) {
        Person person = new Person();
        person.setId(id);
        person.setName("amao");
        person.setAge(30);
        return person;
    }

}
