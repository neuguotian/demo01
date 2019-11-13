package com.miaosha.demo.controller;

import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.response.CommonReturnType;
import com.miaosha.demo.service.OrderService;
import com.miaosha.demo.service.model.OrderModel;
import com.miaosha.demo.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname orderController
 * @Description TODO
 * @Date 2019/11/1 14:36
 * @Author gt
 */
@RestController("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // 封装下单请求
    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "promoId", required = false)Integer promoId,
                                        @RequestParam(name = "amount")Integer amount) throws BusinessException {
        // login成功的时候, 把登录成功信息价到了 session中
        final Boolean is_login = (Boolean)httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (is_login == null || !is_login.booleanValue()) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户还没登录不能下单");
        }
        // 获取用户的登录信息
        final UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        // 确认用户已经登录, 下单
        final OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, promoId, amount);


        return CommonReturnType.create(orderModel);

    }
}
