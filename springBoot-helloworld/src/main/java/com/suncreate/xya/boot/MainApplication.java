package com.suncreate.xya.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MainApplication.class, args);

        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }
}
