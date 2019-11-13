package com.miaosha.demo.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @Classname PromoModel
 * @Description TODO
 * @Date 2019/11/2 14:27
 * @Author gt
 */
@Data
public class PromoModel {
    private Integer id;

    // 秒杀活动状态, 此字段和数据库没有关系 1没开始 2进行中 3已结束
    private Integer status;

    // 秒杀活动名称
    private String promoName;

    // 秒杀活动开始时间 --- 没有使用java api. maven引入joda-time依赖
    private DateTime startDate;

    // 秒杀活动的结束时间
    private DateTime endDate;

    // 秒杀活动的适用商品
    private Integer itemId;

    // 秒杀活动的商品价格
    private BigDecimal promoItemPrice;

}
