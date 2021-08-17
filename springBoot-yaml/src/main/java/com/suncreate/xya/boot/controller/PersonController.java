package com.suncreate.xya.boot.controller;

import com.suncreate.xya.boot.beans.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    Person person;

    @GetMapping("/person")
    public Person hello(){
        return person;
    }
}
