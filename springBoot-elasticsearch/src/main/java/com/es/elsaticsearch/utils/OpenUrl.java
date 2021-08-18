package com.es.elsaticsearch.utils;

import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * @author : Latent
 * @createdDate : 2020/6/8
 * @updatedDate
 */
public class OpenUrl {
    public static String str="taskkill /F /IM firefox.exe";
    //,这里firefox也可以改为iexplore或者chrome等等也就是指定打开网页的浏览器，后面这些参数就是网址，
    //实际上有文件来代替更好，主要是本人博客不多也懒得折腾了
    public static String str1="cmd /c start firefox "
            + "https://www.cnblogs.com/ywbmaster/p/13026742.html ";

    //我这里把要访问的网址分成了两部分，一次性访问大概二十个左右，浏览器不敢一次打开得太多，怕爆炸
    public static String str2="cmd /c start firefox "
            + "https://www.cnblogs.com/ywbmaster/p/12788012.html ";

    public static ArrayList<String> strList=new ArrayList<String>();


    public OpenUrl(){
        strList.add(str1);
        strList.add(str2);
    }

    public static void main(String args[]) {
        // defaultBrowserOpenUrl();
        OpenUrl openUrl=new OpenUrl();
        while(true){
            int i = 0;
            String strUrl = "";
            while (i < 2) {
                strUrl = strList.get(i);
                openFirefoxBrowser(strUrl, str);
                //每关闭一次浏览器，等待大概30s再重启，太过频繁浏览器会爆炸
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                i++;
            }
            //遍历一次睡一个小时，一天可以跑个二十二二十三次左右3600000
            try {
                Thread.sleep(36);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //使用指定的浏览器打开
    public static void openFirefoxBrowser(String start,String stop) {
        // 启用cmd运行firefox的方式来打开网址。
        try {
            Runtime.getRuntime().exec(start);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Runtime.getRuntime().exec(stop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //使用操作系统默认的浏览器打开
    private static void defaultBrowserOpenUrl() {
        // ...
        try {
            Desktop.getDesktop().browse(new URI("http://blog.csdn.net/u012062455/article/details/52369288"));
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 网址被屏蔽了，手动加网址试一下。
    }




}
