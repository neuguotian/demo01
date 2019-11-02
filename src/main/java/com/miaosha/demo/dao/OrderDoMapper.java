package com.miaosha.demo.dao;

import com.miaosha.demo.dataobject.OrderDo;

public interface OrderDoMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderDo record);

    int insertSelective(OrderDo record);

    OrderDo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderDo record);

    int updateByPrimaryKey(OrderDo record);
}