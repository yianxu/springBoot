package com.suncreate.xya.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@ConfigurationProperties(prefix = "user")
@Component
public class User {
    private String loginName;
    private String passWord;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public User() {
    }

    public User(String loginName, String passWord) {
        this.loginName = loginName;
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "User{" +
                "loginName='" + loginName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }
}
