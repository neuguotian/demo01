package com.miaosha.demo.service.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname OrderModel
 * @Description TODO
 * @Date 2019/11/1 8:38
 * @Author gt
 */
// 用户下单的交易模型
@Data
public class OrderModel {
    // 企业级订单号, 用String
    private String id;

    // 用户id
    private Integer userId;

    // 商品id
    private Integer itemId;

    //若非空，则表示以秒杀商品的方式下单
    private Integer promoId;

    // 购买商品的单价 --- 秒杀商品, price会不断变化. 若promoId非空, 则表示秒杀商品的价格
    private BigDecimal itemPrice;

    // 购买数量
    private Integer amount;

    // 购买金额. 若promoId非空, 则表示秒杀商品的价格
    private BigDecimal orderPrice;
}
