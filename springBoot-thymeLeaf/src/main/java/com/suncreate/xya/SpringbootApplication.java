package com.suncreate.xya;

import com.suncreate.xya.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringbootApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringbootApplication.class,args);

        User user = configurableApplicationContext.getBean(User.class);
        System.out.println(user.toString());
    }
}
