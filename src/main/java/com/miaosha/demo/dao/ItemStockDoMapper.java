package com.miaosha.demo.dao;

import com.miaosha.demo.dataobject.ItemStockDo;
import org.apache.ibatis.annotations.Param;

public interface ItemStockDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDo record);

    int insertSelective(ItemStockDo record);

    ItemStockDo selectByPrimaryKey(Integer id);

    ItemStockDo selectByItemId(Integer itemId);

    int updateByPrimaryKeySelective(ItemStockDo record);

    int updateByPrimaryKey(ItemStockDo record);

    int decreaseStock(@Param("itemId")Integer itemId, @Param("amount")Integer amount);
}