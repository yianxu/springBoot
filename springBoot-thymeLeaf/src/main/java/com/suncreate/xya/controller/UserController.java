package com.suncreate.xya.controller;

import com.suncreate.xya.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    User user;

    @GetMapping(value = "/test")
    public ModelAndView test() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);
        mv.setViewName("/user/show.html");
        return mv;
    }
}
