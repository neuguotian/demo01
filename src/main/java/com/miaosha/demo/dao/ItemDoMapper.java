package com.miaosha.demo.dao;

import com.miaosha.demo.dataobject.ItemDo;

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
}