package com.miaosha.demo.service;

import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.service.model.OrderModel;

/**
 * @Classname OrderService
 * @Description TODO
 * @Date 2019/11/1 8:53
 * @Author gt
 */
public interface OrderService {
    // 谁买了什么, 买了多少
    OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException;
}
