package com.miaosha.demo.service;

import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.service.model.ItemModel;

import java.util.List;

/**
 * @Classname ItemService
 * @Description TODO
 * @Date 2019/10/23 11:23
 * @Author gt
 */
public interface ItemService {
    // 创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    // 商品列表
    List<ItemModel> listItem();

    // 商品详情浏览
    ItemModel getItemById(Integer id);
}
