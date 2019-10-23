package com.miaosha.demo.controller.viewobject;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname ItemVO
 * @Description TODO
 * @Date 2019/10/23 12:13
 * @Author gt
 */
@Data
public class ItemVO {
    private Integer id;

    // 商品名称
    private String title;

    // 商品价格 --- 这个地方用的BigDecimal, 用Double传到前端会有一个精度问题, 导致资损, service
    // 层的ItemModel中BigDecimal, 数据库中ItemDo用的Double, 两个领域模型之间需要手动转换
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
