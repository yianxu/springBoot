package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MabatisApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MabatisApplication.class,args);
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user = userMapper.Sel(1);
        System.out.println(user);
    }
}
