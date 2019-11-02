package com.miaosha.demo.dao;

import com.miaosha.demo.dataobject.ItemDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemDoMapper {
    // sales desc
    List<ItemDo> listItem();

    int deleteByPrimaryKey(Integer id);

    int insert(ItemDo record);

    int insertSelective(ItemDo record);

    ItemDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDo record);

    int updateByPrimaryKey(ItemDo record);

    int increaseSales(@Param("id")Integer itemId, @Param("amount")Integer amount);
}