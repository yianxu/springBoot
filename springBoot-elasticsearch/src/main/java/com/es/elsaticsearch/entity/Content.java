package com.es.elsaticsearch.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : ywb
 * @createdDate : 2020/5/31
 * @updatedDate
 */
@Data
public class Content implements Serializable {
    /**
     * 标题
     */
    private String title;

    /**
     * 价格
     */
    private String prices;

    /**
     * 图片
     */
    private String img;
}
