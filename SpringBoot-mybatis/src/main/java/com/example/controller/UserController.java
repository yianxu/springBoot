package com.example.controller;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testBoot")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("getUser/{id}")
    public String getUser(@PathVariable int id){
        return this.userService.Sel(id).toString();
    }
}
