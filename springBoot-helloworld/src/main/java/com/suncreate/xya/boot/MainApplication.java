package com.suncreate.xya.boot;

import com.suncreate.xya.boot.bean.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MainApplication.class, args);
//      for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
//           System.out.println(beanDefinitionName);
//       }

        ApplicationContext ctx = new ClassPathXmlApplicationContext("/beans.xml ");
        User user = (User) ctx.getBean("user01");
        System.out.println(user);

        User user01 = (User) applicationContext.getBean("user01");
        System.out.println(user01);

        User user02 = (User) applicationContext.getBean("haha");
        System.out.println(user02);
    }
}
