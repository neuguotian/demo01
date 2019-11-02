package com.miaosha.demo.service.impl;

import com.miaosha.demo.dao.OrderDoMapper;
import com.miaosha.demo.dao.SequenceDoMapper;
import com.miaosha.demo.dataobject.OrderDo;
import com.miaosha.demo.dataobject.SequenceDo;
import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.service.ItemService;
import com.miaosha.demo.service.OrderService;
import com.miaosha.demo.service.UserService;
import com.miaosha.demo.service.model.ItemModel;
import com.miaosha.demo.service.model.OrderModel;
import com.miaosha.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Classname OrderServiceImpl
 * @Description TODO
 * @Date 2019/11/1 8:54
 * @Author gt
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        // 1. 校验下单状态, 下单的商品是否存在, 用户是否是合法, 购买的数量是否正确
        final ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        final UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }

        if (amount <=0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }

        // 2.落单减库存(存在下单了不支付.下单成功，这部分库存被占有 -- 拿到货),
        // （支付减库存 --- 有超卖的风险, 下单不减库存，支付减库存 无法保证对应的库存还有，可能要退款 ）
        final boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        // 订单入库
        final OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));

        // 生成交易流水号
        orderModel.setId(generateOrderNo());
        // 插入数据库
        final OrderDo orderDo = convertFromOrderModel(orderModel);
        orderDoMapper.insertSelective(orderDo);
        // 加上商品的销量, 返回前端
        itemService.increaseSales(itemId, amount);

        return orderModel;
    }

    private OrderDo convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null){
            return null;
        }
        final OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel, orderDo);
        // 数据库中Double, OrderModel中BigDecimal。 手动处理一下,要不不能插入数据库
        orderDo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDo.setOrderPrice(orderModel.getOrderPrice().doubleValue());

        return orderDo;
    }

    // 只要执行完这段代码, 外部事物回滚, 此事物不受影响, 对应的sequence消耗掉
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNo() {
        // 订单号16位,
        // 前8位为年月日,
        final StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        final String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        // 中间6位为自增序列,
        // 获取当前sequence
        int sequence = 0;
        final SequenceDo sequenceDo = sequenceDoMapper.getSequenceByName("order_info");
        sequence = sequenceDo.getCurrentValue();
        sequenceDo.setCurrentValue(sequence + sequenceDo.getStep());
        sequenceDoMapper.updateByPrimaryKeySelective(sequenceDo);

        final String sequenceStr = String.valueOf(sequence);
        // sequence_info 可以加两个字段, init_val, max_val. 否则无限跳会超过6位. 暂时不作考虑
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }

        stringBuilder.append(sequenceStr);


        // 最后2位为分库分表, 暂时写死
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

}
