package com.miaosha.demo.service.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "商品名称不能为空")
    private String title;

    // 商品价格 --- 这个地方用的BigDecimal, 用Double传到前端会有一个精度问题, 导致资损
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格必须大于0")
    private BigDecimal price;

    // 库存
    @NotNull(message = "库存不能不填")
    private Integer stock;

    // 描述
    @NotBlank(message = "商品描述信息不能为空")
    private String description;

    // 销量
    private Integer sales;

    // 描述图片
    @NotBlank(message = "图片信息不能为空")
    private String imgUrl;

    // 使用聚合模型, 如果promoModel不为空, 则表示其拥有还未结束的秒杀活动
    private PromoModel promoModel;
}
