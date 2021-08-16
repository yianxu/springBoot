package com.suncreate.xya.boot.config;

import com.suncreate.xya.boot.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这是一个配置类，相当于配置文件
 */
@Configuration
public class MyConfig {

    @Bean
    public User user01(){
        return new User("xiewei",31);
    }

    @Bean("haha")
    public User user02(){
        return new User("hhaa",10);
    }
}
