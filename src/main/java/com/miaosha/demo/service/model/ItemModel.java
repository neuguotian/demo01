package com.miaosha.demo.service.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname ItemModel
 * @Description TODO
 * @Date 2019/10/23 8:27
 * @Author gt
 */
@Data
public class ItemModel {
    private Integer id;

    // 商品名称
    private String title;

    // 商品价格
    private BigDecimal price;

    // 库存
    private Integer stock;

    // 描述
    private String description;

    // 销量
    private Integer sales;

    // 描述图片
    private String imgUrl;
}
