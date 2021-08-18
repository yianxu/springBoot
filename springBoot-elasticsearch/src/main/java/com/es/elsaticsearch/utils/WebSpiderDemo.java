package com.es.elsaticsearch.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Latent
 * @createdDate : 2020/6/1
 * @updatedDate
 */
public class WebSpiderDemo {
    public static void main(String[] args) {
        System.out.println("开始");
        long start = System.currentTimeMillis();
        /**
         *  从json中获取到的 url
         *  请获取后手动填写
         */
        String lastUrl = "https://music.163.com/";
        //自定义文件名称
        String fileName = "a.mp4";
        downloadMovie(lastUrl, fileName);

        long end = System.currentTimeMillis();
        System.out.println("完成 ");
        System.err.println("总共耗时：" + (end - start) / 1000 + "s");
    }

    public static void downloadMovie(String BLUrl, String fileName) {
        InputStream inputStream = null;
        try {
            URL url = new URL(BLUrl);
            URLConnection urlConnection = url.openConnection();
            // 填需要爬取的av号
            urlConnection.setRequestProperty("Referer", "https://www.bilibili.com/video/av69924947");
            urlConnection.setRequestProperty("Sec-Fetch-Mode", "no-cors");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
            urlConnection.setConnectTimeout(10 * 1000);
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //定义路径
        String path = "E:\\rob\\" + fileName;
        File file = new File(path);
        int i = 1;
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = bis.read(bys)) != -1) {
                bos.write(bys, 0, len);
                System.out.println("写入第 " + i++ + "次");
            }
            bis.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
