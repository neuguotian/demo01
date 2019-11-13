package com.miaosha.demo.service;

import com.miaosha.demo.service.model.PromoModel;

/**
 * @Classname PromoService
 * @Description TODO
 * @Date 2019/11/2 15:34
 * @Author gt
 */
public interface PromoService {
    // 根据itemid获取即将进行的或者正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}
